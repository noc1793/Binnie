package binnie.extratrees.alcohol;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.FluidStack;

import binnie.core.Constants;
import binnie.core.liquid.FluidContainerType;
import binnie.core.liquid.FluidDefinition;
import binnie.core.liquid.IFluidType;

public enum Juice implements IFluidType, ICocktailLiquid {
	Apple("Apple Juice", "juice.apple", 16763442, 0.7, "Apple"),
	Apricot("Apricot Juice", "juice.apricot", 16758046, 0.6, "Apricot"),
	Banana("Banana Juice", "juice.banana", 15324291, 0.6, "Banana"),
	Cherry("Cherry Juice", "juice.cherry", 13044511, 0.6, "Cherry"),
	Elderberry("Elderberry Juice", "juice.elderberry", 4204073, 0.6, "Elderberry"),
	Lemon("Lemon Juice", "juice.lemon", 14604882, 0.7, "Lemon"),
	Lime("Lime Juice", "juice.lime", 12177007, 0.6, "Lime"),
	Orange("Orange Juice", "juice.orange", 16359983, 0.8, "Orange"),
	Peach("Peach Juice", "juice.peach", 16434525, 0.7, "Peach"),
	Plum("Plum Juice", "juice.plum", 10559249, 0.7, "Plum"),
	Carrot("Carrot Juice", "juice.carrot", 16485911, 0.7, "Carrot"),
	Tomato("Tomato Juice", "juice.tomato", 12731438, 0.7, "Tomato"),
	Cranberry("Cranberry Juice", "juice.cranberry", 12920629, 0.7, "Cranberry"),
	Grapefruit("Grapefruit Juice", "juice.grapefruit", 15897173, 0.6, "Grapefruit"),
	Olive("Olive Oil", "juice.olive", 16042240, 0.6, "Olive"),
	Pineapple("Pineapple Juice", "juice.pineapple", 15189319, 0.6, "Pineapple"),
	Pear("Pear Juice", "juice.pear", 14204773, 0.6, "Pear"),
	WhiteGrape("White Grape Juice", "juice.white.grape", 16507769, 0.6, "WhiteGrape"),
	RedGrape("Red Grape Juice", "juice.red.grape", 9775412, 0.6, "RedGrape");

	public String squeezing;
	FluidDefinition definition;

	Juice(final String name, final String ident, final int colour, final double transparency, final String squeezing) {
		this.addSqueezing("crop" + squeezing);

		definition = new FluidDefinition(ident, name, colour)
			.setTransparency(transparency)
			.setTextures(new ResourceLocation(Constants.EXTRA_TREES_MOD_ID, "blocks/liquids/liquid"))
			.setShowHandler(type -> type == FluidContainerType.GLASS);
	}

	private void addSqueezing(final String oreDict) {
		this.squeezing = oreDict;
	}

	@Override
	public String toString() {
		return definition.toString();
	}

	@Override
	public String getDisplayName() {
		return definition.getDisplayName();
	}

	@Override
	public String getIdentifier() {
		return definition.getIdentifier();
	}

	@Override
	public int getColor() {
		return definition.getColor();
	}

	@Override
	public FluidStack get(final int amount) {
		return definition.get(amount);
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
		return 0.0f;
	}

	@Override
	public FluidDefinition getDefinition() {
		return definition;
	}
}
