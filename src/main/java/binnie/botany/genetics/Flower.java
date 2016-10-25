package binnie.botany.genetics;

import binnie.botany.api.*;
import binnie.botany.core.BotanyCore;
import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IChromosome;
import forestry.core.genetics.Chromosome;
import forestry.core.genetics.Individual;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class Flower extends Individual implements IFlower {
    public IFlowerGenome genome;
    public IFlowerGenome mate;
    int age;
    boolean wilting;
    boolean flowered;

    public Flower(@Nonnull final NBTTagCompound nbt) {
    	super(nbt);
        if (nbt == null) {
            this.genome = BotanyCore.getFlowerRoot().templateAsGenome(BotanyCore.getFlowerRoot().getDefaultTemplate());
            return;
        }
        if(nbt.hasKey("Age")){
        	this.age = nbt.getInteger("Age");
        }else{
        	 this.age = 0;
        }
        if(nbt.hasKey("Wilt")){
        	this.wilting = nbt.getBoolean("Wilt");
        }else{
        	this.wilting = false;
        }
        if(nbt.hasKey("Flowered")){
        	this.flowered = nbt.getBoolean("Flowered");
        }else{
            if (age > 0) {
                this.flowered = true;
            } else {
                this.flowered = false;
            }
        }
        if (nbt.hasKey("Genome")) {
            this.genome = new FlowerGenome(nbt.getCompoundTag("Genome"));
        }
        if (nbt.hasKey("Mate")) {
            this.mate = new FlowerGenome(nbt.getCompoundTag("Mate"));
        }
    }

    public Flower(final IFlowerGenome genome, final int age) {
        this.age = 0;
        this.wilting = false;
        this.genome = genome;
        this.age = age;
        if (age > 0) {
            this.flowered = true;
        } else {
            this.flowered = false;
        }
    }

    @Override
    public String getDisplayName() {
        final IAlleleFlowerSpecies species = this.getGenome().getPrimary();
        String name = "";
        if (species != null) {
            name += species.getName();
        }
        if (this.age == 0) {
            name += "";
        }
        return name;
    }

    @Override
    public void addTooltip(final List<String> list) {
        final IAlleleFlowerSpecies primary = this.genome.getPrimary();
        final IAlleleFlowerSpecies secondary = this.genome.getSecondary();
        if (!this.isPureBred(EnumFlowerChromosome.SPECIES)) {
            list.add("§9" + primary.getName() + "-" + secondary.getName() + " Hybrid");
        }
        list.add("§6Age: " + this.getAge());
        list.add("§6T: " + this.getGenome().getPrimary().getTemperature() + " / " + this.getGenome().getToleranceTemperature());
        list.add("§6M: " + this.getGenome().getPrimary().getMoisture() + " / " + this.getGenome().getToleranceMoisture());
        list.add("§6pH: " + this.getGenome().getPrimary().getHumidity() + " / " + this.getGenome().getTolerancePH());
        list.add("§6Fert: " + this.getGenome().getFertility() + "x");
    }

    @Override
    public String getIdent() {
        return this.getGenome().getPrimary().getUID();
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound2) {
        NBTTagCompound nbttagcompound = super.writeToNBT(nbttagcompound2);
        nbttagcompound.setInteger("Age", this.age);
        nbttagcompound.setBoolean("Wilt", this.wilting);
        nbttagcompound.setBoolean("Flowered", this.flowered);
        if (this.genome != null) {
            final NBTTagCompound NBTmachine = new NBTTagCompound();
            this.genome.writeToNBT(NBTmachine);
            nbttagcompound.setTag("Genome", NBTmachine);
        }
        if (this.mate != null) {
            final NBTTagCompound NBTmachine = new NBTTagCompound();
            this.mate.writeToNBT(NBTmachine);
            nbttagcompound.setTag("Mate", NBTmachine);
        }
        return nbttagcompound;
    }

    public int getMetadata() {
        return 0;
    }

    @Override
    public IFlowerGenome getGenome() {
        return this.genome;
    }

    private IChromosome inheritChromosome(final Random rand, final IChromosome parent1, final IChromosome parent2) {
        IAllele choice1;
        if (rand.nextBoolean()) {
            choice1 = parent1.getPrimaryAllele();
        } else {
            choice1 = parent1.getSecondaryAllele();
        }
        IAllele choice2;
        if (rand.nextBoolean()) {
            choice2 = parent2.getPrimaryAllele();
        } else {
            choice2 = parent2.getSecondaryAllele();
        }
        if (rand.nextBoolean()) {
            return new Chromosome(choice1, choice2);
        }
        return new Chromosome(choice2, choice1);
    }

    @Override
    public void mate(final IFlower other) {
        this.mate = new FlowerGenome(other.getGenome().getChromosomes());
    }

    @Override
    public IFlowerGenome getMate() {
        return this.mate;
    }

    @Override
    public IFlower copy() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new Flower(nbttagcompound);
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public void age() {
        if (this.age < 15) {
            ++this.age;
        }
    }

    @Override
    public void setAge(final int i) {
        this.age = i;
    }

    @Override
    public int getMaxAge() {
        return this.getGenome().getLifespan();
    }

    @Override
    public boolean isWilted() {
        return this.wilting;
    }

    @Override
    public void setWilted(final boolean wilted) {
        this.wilting = wilted;
    }

    @Override
    public boolean hasFlowered() {
        return this.flowered;
    }

    @Override
    public void setFlowered(final boolean flowered) {
        this.flowered = flowered;
    }

    @Override
    public void removeMate() {
        this.mate = null;
    }

    @Override
    public IFlower getOffspring(final World world, final BlockPos pos) {
        final IChromosome[] chromosomes = new IChromosome[this.genome.getChromosomes().length];
        IChromosome[] parent1 = this.genome.getChromosomes();
        IChromosome[] parent2 = this.mate.getChromosomes();
        parent1 = this.mutateSpecies(world, pos, this.genome, this.mate);
        parent2 = this.mutateSpecies(world, pos, this.mate, this.genome);
        for (int i = 0; i < parent1.length; ++i) {
            if (parent1[i] != null && parent2[i] != null) {
                chromosomes[i] = Chromosome.inheritChromosome(world.rand, parent1[i], parent2[i]);
            }
        }
        return new Flower(new FlowerGenome(chromosomes), 0);
    }

    private IChromosome[] mutateSpecies(final World world, final BlockPos pos, final IFlowerGenome genomeOne, final IFlowerGenome genomeTwo) {
        IChromosome[] parent1 = genomeOne.getChromosomes();
        final IChromosome[] parent2 = genomeTwo.getChromosomes();
        IAlleleFlowerSpecies allele0;
        IAlleleFlowerSpecies allele2;
        IFlowerGenome genome0;
        IFlowerGenome genome2;
        if (world.rand.nextBoolean()) {
            allele0 = (IAlleleFlowerSpecies) parent1[EnumTreeChromosome.SPECIES.ordinal()].getPrimaryAllele();
            allele2 = (IAlleleFlowerSpecies) parent2[EnumTreeChromosome.SPECIES.ordinal()].getSecondaryAllele();
            genome0 = genomeOne;
            genome2 = genomeTwo;
        } else {
            allele0 = (IAlleleFlowerSpecies) parent2[EnumTreeChromosome.SPECIES.ordinal()].getPrimaryAllele();
            allele2 = (IAlleleFlowerSpecies) parent1[EnumTreeChromosome.SPECIES.ordinal()].getSecondaryAllele();
            genome0 = genomeTwo;
            genome2 = genomeOne;
        }
        IFlowerColour colour1 = genome0.getPrimaryColor();
        IFlowerColour colour2 = genome2.getPrimaryColor();
        if (colour1 != colour2) {
            for (final IColourMix mutation : BotanyCore.getFlowerRoot().getColourMixes(true)) {
                if (mutation.isMutation(colour1, colour2) && world.rand.nextFloat() * 100.0f < mutation.getChance()) {
                    parent1[EnumFlowerChromosome.PRIMARY.ordinal()] = new Chromosome(mutation.getResult().getAllele());
                }
            }
        }
        colour1 = genome0.getSecondaryColor();
        colour2 = genome2.getSecondaryColor();
        if (colour1 != colour2) {
            for (final IColourMix mutation : BotanyCore.getFlowerRoot().getColourMixes(true)) {
                if (mutation.isMutation(colour1, colour2) && world.rand.nextFloat() * 100.0f < mutation.getChance()) {
                    parent1[EnumFlowerChromosome.SECONDARY.ordinal()] = new Chromosome(mutation.getResult().getAllele());
                }
            }
        }
        colour1 = genome0.getStemColor();
        colour2 = genome2.getStemColor();
        if (colour1 != colour2) {
            for (final IColourMix mutation : BotanyCore.getFlowerRoot().getColourMixes(true)) {
                if (mutation.isMutation(colour1, colour2) && world.rand.nextFloat() * 100.0f < mutation.getChance()) {
                    parent1[EnumFlowerChromosome.STEM.ordinal()] = new Chromosome(mutation.getResult().getAllele());
                }
            }
        }
        IChromosome[] template = null;
        for (final IFlowerMutation mutation2 : BotanyCore.getFlowerRoot().getMutations(true)) {
            final float chance = mutation2.getChance(world, pos, allele0, allele2, genome0, genome2);
            if (chance > 0.0f && world.rand.nextFloat() * 100.0f < chance && template == null) {
                template = BotanyCore.getFlowerRoot().templateAsChromosomes(mutation2.getTemplate());
            }
        }
        if (template != null) {
            parent1 = template;
        }
        return parent1;
    }
}
