package binnie.extratrees.alcohol;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.FluidStack;

import binnie.core.Constants;
import binnie.core.liquid.FluidContainerType;
import binnie.core.liquid.FluidDefinition;
import binnie.core.liquid.IFluidType;
import binnie.core.util.I18N;

public enum Alcohol implements IFluidType, ICocktailLiquid {
	Apple("cider.apple", 16432700, 0.3, 0.05){
		@Override
		protected void init() {
			addFementation(Juice.Apple);
		}
	},
	Apricot("wine.apricot", 15781686, 0.3, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.Apricot);
		}
	},
	Banana("wine.banana", 14993485, 0.3, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.Banana);
		}
	},
	Cherry("wine.cherry", 11207702, 0.3, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.Cherry);
		}
	},
	Elderberry("wine.elderberry", 9764865, 0.3, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.Elderberry);
		}
	},
	Peach("cider.peach", 15361563, 0.3, 0.05){
		@Override
		protected void init() {
			addFementation(Juice.Peach);
		}
	},
	Pear("ciderpear", 15061095, 0.3, 0.05){
		@Override
		protected void init() {
			addFementation(Juice.Pear);
		}
	},
	Plum("wine.plum", 12063752, 0.3, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.Plum);
		}
	},
	Carrot("wine.carrot", 16219394, 0.3, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.Carrot);
		}
	},
	WhiteWine("wine.white", 15587989, 0.1, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.WhiteGrape);
		}
	},
	RedWine("wine.red", 7670539, 0.2, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.RedGrape);
		}
	},
	SparklingWine("wine.sparkling", 16709566, 0.1, 0.1),
	Agave("wine.agave", 13938276, 0.2, 0.1),
	Potato("fermented.potatoes", 12028240, 0.8, 0.1){
		@Override
		protected void init() {
			addFementation("cropPotato");
		}
	},
	Citrus("wine.citrus", 16776960, 0.2, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.Lemon);
			addFementation(Juice.Lime);
			addFementation(Juice.Orange);
			addFementation(Juice.Grapefruit);
		}
	},
	Cranberry("wine.cranberry", 11599874, 0.2, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.Cranberry);
		}
	},
	Pineapple("wine.pineapple", 14724150, 0.2, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.Pineapple);
		}
	},
	Tomato("wine.tomato", 12458521, 0.2, 0.1){
		@Override
		protected void init() {
			addFementation(Juice.Tomato);
		}
	},
	Fruit("juice", 16432700, 0.2, 0.1),
	Ale("beer.ale", 12991009, 0.7, 0.05),
	Lager("beer.lager", 15301637, 0.7, 0.05),
	WheatBeer("beer.wheat", 14380552, 0.7, 0.05),
	RyeBeer("beer.rye", 10836007, 0.7, 0.05),
	CornBeer("beer.corn", 13411364, 0.7, 0.05),
	Stout("beer.stout", 5843201, 0.8, 0.05),
	Barley("mash.grain", 12991009, 0.9, 0.05),
	Wheat("mash.wheat", 12991009, 0.9, 0.05),
	Rye("mash.rye", 10836007, 0.9, 0.05),
	Corn("mash.corn", 13411364, 0.9, 0.05);

	final List<String> fermentationLiquid;
	final float abv;
	final FluidDefinition definition;
	String fermentationSolid;

	Alcohol(final String ident, final int color, final double transparency, final double abv) {
		this.fermentationLiquid = new ArrayList<>();
		this.fermentationSolid = "";
		this.abv = (float) abv;
		init();
		definition = new FluidDefinition(ident, "extratrees.fluid.alcohol." + this.name().toLowerCase(), color)
			.setTransparency(transparency)
			.setTextures(new ResourceLocation(Constants.EXTRA_TREES_MOD_ID, "blocks/liquids/liquid"))
			.setPlaceHandler((type) -> type == FluidContainerType.GLASS);
	}

	protected void init(){

	}

	@Override
	public FluidDefinition getDefinition() {
		return definition;
	}

	public List<String> getFermentationLiquid() {
		return fermentationLiquid;
	}

	protected void addFementation(final Juice juice) {
		this.fermentationLiquid.add(juice.getIdentifier());
	}

	protected void addFementation(final String oreDict) {
		this.fermentationSolid = oreDict;
	}

	@Override
	public String toString() {
		return this.getDisplayName();
	}

	@Override
	public String getDisplayName() {
		return I18N.localise("extratrees.fluid.alcohol." + this.name().toLowerCase());
	}

	@Override
	public String getIdentifier() {
		return definition.getIdentifier();
	}

	@Override
	public FluidStack get(final int amount) {
		return definition.get(amount);
	}

	@Override
	public int getColor() {
		return definition.getColor();
	}

	@Override
	public int getTransparency() {
		return definition.getTransparency();
	}

	@Override
	public String getTooltip(final int ratio) {
		return ratio + " Part" + ((ratio > 1) ? "s " : " ") + definition.getDisplayName();
	}

	@Override
	public float getABV() {
		return this.abv;
	}


}
