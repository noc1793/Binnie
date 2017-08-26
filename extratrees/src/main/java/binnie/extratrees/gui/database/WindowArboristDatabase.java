package binnie.extratrees.gui.database;

import java.util.stream.Collectors;

import binnie.core.Binnie;
import binnie.core.api.gui.IArea;
import binnie.genetics.api.ITreeBreedingSystem;
import forestry.api.arboriculture.TreeManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import binnie.core.api.gui.IWidget;
import binnie.core.gui.controls.listbox.ControlListBox;
import binnie.core.gui.database.ControlItemStackOption;
import binnie.core.gui.database.DatabaseTab;
import binnie.core.gui.database.IDatabaseMode;
import binnie.core.gui.database.PageBranchOverview;
import binnie.core.gui.database.PageBranchSpecies;
import binnie.core.gui.database.PageBreeder;
import binnie.core.gui.database.PageSpeciesClassification;
import binnie.core.gui.database.PageSpeciesMutations;
import binnie.core.gui.database.PageSpeciesOverview;
import binnie.core.gui.database.PageSpeciesResultant;
import binnie.core.gui.database.WindowAbstractDatabase;
import binnie.core.gui.minecraft.Window;
import binnie.core.util.I18N;
import binnie.extratrees.ExtraTrees;
import binnie.design.api.IDesignMaterial;
import binnie.extratrees.wood.WoodManager;

public class WindowArboristDatabase extends WindowAbstractDatabase {
	public WindowArboristDatabase(final EntityPlayer player, final Side side, final boolean nei) {
		super(player, side, nei, Binnie.GENETICS.getSystem(TreeManager.treeRoot), 120);
	}

	public static Window create(final EntityPlayer player, final Side side, final boolean nei) {
		return new WindowArboristDatabase(player, side, nei);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTabs() {
		new PageSpeciesOverview(this.getInfoPages(Mode.SPECIES), new DatabaseTab(ExtraTrees.instance, "species.overview"));
		new PageSpeciesTreeGenome(this.getInfoPages(Mode.SPECIES), new DatabaseTab(ExtraTrees.instance, "species.genome"));
		new PageSpeciesClassification(this.getInfoPages(Mode.SPECIES), new DatabaseTab(ExtraTrees.instance, "species.classification"));
		new PageSpeciesResultant(this.getInfoPages(Mode.SPECIES), new DatabaseTab(ExtraTrees.instance, "species.resultant"));
		new PageSpeciesMutations(this.getInfoPages(Mode.SPECIES), new DatabaseTab(ExtraTrees.instance, "species.further"));
		new PageBranchOverview(this.getInfoPages(Mode.BRANCHES), new DatabaseTab(ExtraTrees.instance, "branches.overview"));
		new PageBranchSpecies(this.getInfoPages(Mode.BRANCHES), new DatabaseTab(ExtraTrees.instance, "branches.species"));
		new PageBreeder(this.getInfoPages(Mode.BREEDER), this.getUsername(), new DatabaseTab(ExtraTrees.instance, "breeder"));
		this.createMode(TreeMode.FRUIT, new FruitModeWidgets(this));
		this.createMode(TreeMode.WOOD, new WoodModeWidgets(this));
		this.createMode(TreeMode.PLANKS, new PlanksModeWidgets(this));
		new PageFruit(this.getInfoPages(TreeMode.FRUIT), new DatabaseTab(ExtraTrees.instance, "fruit.natural"), true);
		new PageFruit(this.getInfoPages(TreeMode.FRUIT), new DatabaseTab(ExtraTrees.instance, "fruit.potential"), false);
		new PageWood(this.getInfoPages(TreeMode.WOOD), new DatabaseTab(ExtraTrees.instance, "wood.natural"));
		new PagePlanksOverview(this.getInfoPages(TreeMode.PLANKS), new DatabaseTab(ExtraTrees.instance, "planks.overview"));
		new PagePlanksTrees(this.getInfoPages(TreeMode.PLANKS), new DatabaseTab(ExtraTrees.instance, "planks.natural"));
	}

	@Override
	protected String getModId() {
		return ExtraTrees.instance.getModId();
	}

	@Override
	protected String getBackgroundTextureName() {
		return "TreeDatabase";
	}

	enum TreeMode implements IDatabaseMode {
		FRUIT,
		WOOD,
		PLANKS;

		@Override
		public String getName() {
			return I18N.localise("extratrees.gui.database.mode." + this.name().toLowerCase());
		}
	}

	private static class FruitModeWidgets extends ModeWidgets {
		private WindowArboristDatabase windowArboristDatabase;

		public FruitModeWidgets(WindowArboristDatabase windowArboristDatabase) {
			super(TreeMode.FRUIT, windowArboristDatabase);
			this.windowArboristDatabase = windowArboristDatabase;
		}

		@Override
		public void createListBox(final IArea area) {
			ControlListBox<ItemStack> controlListBox = new FruitModeControlListBox(this, area);
			ITreeBreedingSystem breedingSystem = (ITreeBreedingSystem) windowArboristDatabase.getBreedingSystem();
			controlListBox.setOptions(breedingSystem.getAllFruits());
			this.listBox = controlListBox;
		}

		private static class FruitModeControlListBox extends ControlListBox<ItemStack> {

			public FruitModeControlListBox(FruitModeWidgets fruitModeWidgets, IArea area) {
				super(fruitModeWidgets.modePage, area.xPos(), area.yPos(), area.width(), area.height(), 12);
			}

			@Override
			@SideOnly(Side.CLIENT)
			public IWidget createOption(final ItemStack value, final int y) {
				return new ControlItemStackOption(this.getContent(), value, y);
			}
		}
	}

	private static class WoodModeWidgets extends ModeWidgets {
		private WindowArboristDatabase windowArboristDatabase;

		public WoodModeWidgets(WindowArboristDatabase windowArboristDatabase) {
			super(TreeMode.WOOD, windowArboristDatabase);
			this.windowArboristDatabase = windowArboristDatabase;
		}

		@Override
		public void createListBox(final IArea area) {
			ControlListBox<ItemStack> controlListBox = new WoodModeControlListBox(this, area);
			ITreeBreedingSystem breedingSystem = (ITreeBreedingSystem) windowArboristDatabase.getBreedingSystem();
			controlListBox.setOptions(breedingSystem.getAllWoods());
			this.listBox = controlListBox;
		}

		private static class WoodModeControlListBox extends ControlListBox<ItemStack> {
			public WoodModeControlListBox(WoodModeWidgets woodModeWidgets, IArea area) {
				super(woodModeWidgets.modePage, area.xPos(), area.yPos(), area.width(), area.height(), 12);
			}

			@Override
			@SideOnly(Side.CLIENT)
			public IWidget createOption(final ItemStack value, final int y) {
				return new ControlItemStackOption(this.getContent(), value, y);
			}
		}
	}

	private static class PlanksModeWidgets extends ModeWidgets {

		public PlanksModeWidgets(WindowArboristDatabase windowArboristDatabase) {
			super(TreeMode.PLANKS, windowArboristDatabase);
		}

		@Override
		public void createListBox(final IArea area) {
			ControlListBox<ItemStack> controlListBox = new PlanksModeControlListBox(this, area);
			controlListBox.setOptions(WoodManager.getAllPlankTypes().stream().map(IDesignMaterial::getStack).collect(Collectors.toList()));
			this.listBox = controlListBox;
		}

		private static class PlanksModeControlListBox extends ControlListBox<ItemStack> {

			public PlanksModeControlListBox(PlanksModeWidgets planksModeWidgets, IArea area) {
				super(planksModeWidgets.modePage, area.xPos(), area.yPos(), area.width(), area.height(), 12);
			}

			@Override
			@SideOnly(Side.CLIENT)
			public IWidget createOption(final ItemStack value, final int y) {
				return new ControlItemStackOption(this.getContent(), value, y);
			}
		}
	}
}