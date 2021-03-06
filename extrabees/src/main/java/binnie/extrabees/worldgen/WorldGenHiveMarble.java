package binnie.extrabees.worldgen;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.oredict.OreDictionary;

import binnie.extrabees.blocks.BlockExtraBeeHives;
import binnie.extrabees.blocks.type.EnumHiveType;
import binnie.extrabees.modules.ModuleCore;

public class WorldGenHiveMarble extends WorldGenHive {
	private final Set<Block> validBlocks = new HashSet<>();

	public WorldGenHiveMarble(int rate) {
		super(rate);
		//TODO config option / more ore names
		OreDictionary.getOres("stoneMarble").stream().filter(s -> s.getItem() instanceof ItemBlock).map(s -> ((ItemBlock) s.getItem()).getBlock()).forEach(validBlocks::add);
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		Block blockAtPos = world.getBlockState(position).getBlock();
		if (world.isAirBlock(position) || !validBlocks.contains(blockAtPos)) {
			return false;
		}

		//generate when one face is different from marble
		int otherFace = 0;
		for (EnumFacing face : EnumFacing.values()) {
			if (!world.getBlockState(position.offset(face)).getBlock().equals(blockAtPos)) {
				otherFace++;
				if (otherFace > 1) {
					return true;
				}
			}
		}

		world.setBlockState(position, ModuleCore.hive.getDefaultState().withProperty(BlockExtraBeeHives.HIVE_TYPE, EnumHiveType.MARBLE));

		return true;
	}
}
