/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package binnie.extratrees.models;

import com.google.common.base.Preconditions;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.core.IModelBaker;
import forestry.arboriculture.blocks.BlockAbstractLeaves;
import forestry.core.models.ModelBlockCached;
import forestry.core.models.baker.ModelBaker;
import forestry.core.proxy.Proxies;

import binnie.extratrees.blocks.BlockETDefaultLeavesFruit;
import binnie.extratrees.blocks.property.PropertyETTypeFruit;
import binnie.extratrees.genetics.ETTreeDefinition;

@SideOnly(Side.CLIENT)
public class ModelDefaultETLeavesFruit extends ModelBlockCached<BlockETDefaultLeavesFruit, ModelDefaultETLeavesFruit.Key> {
	public ModelDefaultETLeavesFruit() {
		super(BlockETDefaultLeavesFruit.class);
	}

	public static class Key {
		public final ETTreeDefinition definition;
		public final boolean fancy;
		private final int hashCode;

		public Key(ETTreeDefinition definition, boolean fancy) {
			this.definition = definition;
			this.fancy = fancy;
			this.hashCode = Objects.hash(definition, fancy);
		}

		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Key)) {
				return false;
			} else {
				Key otherKey = (Key) other;
				return otherKey.definition == definition && otherKey.fancy == fancy;
			}
		}

		@Override
		public int hashCode() {
			return hashCode;
		}
	}

	@Override
	protected ModelDefaultETLeavesFruit.Key getInventoryKey(ItemStack stack) {
		Block block = Block.getBlockFromItem(stack.getItem());
		Preconditions.checkArgument(block instanceof BlockETDefaultLeavesFruit, "ItemStack must be for default fruit leaves.");
		BlockETDefaultLeavesFruit bBlock = (BlockETDefaultLeavesFruit) block;
		return new Key(bBlock.getTreeType(stack.getMetadata()).definition, Proxies.render.fancyGraphicsEnabled());
	}

	@Override
	protected ModelDefaultETLeavesFruit.Key getWorldKey(IBlockState state) {
		Block block = state.getBlock();
		Preconditions.checkArgument(block instanceof BlockETDefaultLeavesFruit, "state must be for default fruit leaves.");
		BlockETDefaultLeavesFruit bBlock = (BlockETDefaultLeavesFruit) block;
		PropertyETTypeFruit.LeafVariant leafVariant = bBlock.getLeafVariant(state);
		Preconditions.checkNotNull(leafVariant);
		return new ModelDefaultETLeavesFruit.Key(leafVariant.definition, Proxies.render.fancyGraphicsEnabled());
	}

	@Override
	protected void bakeBlock(BlockETDefaultLeavesFruit block, Key key, IModelBaker baker, boolean inventory) {
		ETTreeDefinition treeDefinition = key.definition;
		TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();

		ITreeGenome genome = treeDefinition.getGenome();
		IAlleleTreeSpecies species = genome.getPrimary();
		ILeafSpriteProvider leafSpriteProvider = species.getLeafSpriteProvider();

		ResourceLocation leafSpriteLocation = leafSpriteProvider.getSprite(false, key.fancy);
		TextureAtlasSprite leafSprite = map.getAtlasSprite(leafSpriteLocation.toString());

		// Render the plain leaf block.
		baker.addBlockModel(null, leafSprite, BlockAbstractLeaves.FOLIAGE_COLOR_INDEX);

		// Render overlay for fruit leaves.
		ResourceLocation fruitSpriteLocation = genome.getFruitProvider().getDecorativeSprite();
		if (fruitSpriteLocation != null) {
			TextureAtlasSprite fruitSprite = map.getAtlasSprite(fruitSpriteLocation.toString());
			baker.addBlockModel(null, fruitSprite, BlockAbstractLeaves.FRUIT_COLOR_INDEX);
		}

		// Set the particle sprite
		baker.setParticleSprite(leafSprite);
	}

	@Override
	protected IBakedModel bakeModel(IBlockState state, Key key, BlockETDefaultLeavesFruit block) {
		IModelBaker baker = new ModelBaker();

		bakeBlock(block, key, baker, false);

		blockModel = baker.bakeModel(false);
		onCreateModel(blockModel);
		return blockModel;
	}
}
