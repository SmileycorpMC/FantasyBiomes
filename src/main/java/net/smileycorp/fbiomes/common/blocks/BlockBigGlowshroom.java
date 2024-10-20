package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

import java.util.Random;

public class BlockBigGlowshroom extends BlockBigMushroom {
	
	public static final PropertyEnum<EnumVariant> VARIANT = PropertyEnum.create("variant", EnumVariant.class);
	
	public BlockBigGlowshroom() {
		super("Big_Glowshroom");
		setDefaultState(blockState.getBaseState().withProperty(SHAPE, EnumShape.CAP).withProperty(VARIANT, EnumVariant.BLUE));
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(VARIANT).getDrop();
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{SHAPE, VARIANT});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= this.getMaxMeta()) return this.getDefaultState();
		EnumShape shape = EnumShape.values()[meta%2];
		EnumVariant variant = EnumVariant.values()[(meta-(meta%2)) / 2];
		return this.getDefaultState().withProperty(VARIANT, variant).withProperty(SHAPE, shape);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int variant = state.getValue(VARIANT).getMeta();
		int shape = state.getValue(SHAPE).getMeta();
		return  variant * 2 + shape;
    }
	
	@Override
	public int getMaxMeta(){
		return 10;
	}
	
	@Override
	public int getLightValue(IBlockState state) {
		return state.getValue(SHAPE)==EnumShape.CAP ? 15 : 5;
	}
	
	public enum EnumVariant implements IStringSerializable {
		BLUE("blue", FBiomesBlocks.BLUE_GLOWSHROOM),
		GREEN("green", FBiomesBlocks.GREEN_GLOWSHROOM),
		ORANGE("orange", FBiomesBlocks.ORANGE_GLOWSHROOM),
		PINK("pink", FBiomesBlocks.PINK_GLOWSHROOM),
		PURPLE("purple", FBiomesBlocks.PURPLE_GLOWSHROOM);
		
		final String name;
		final int meta;
		final Block drop;
		
		EnumVariant(String name, Block drop) {
			this.name=name;
			this.meta=this.ordinal();
			this.drop=drop;
		}

		@Override
		public String getName() {
			return name;
		}
		
		public int getMeta() {
			return meta;
		}
		
		public Item getDrop() {
			return Item.getItemFromBlock(drop);
		}
		
	}

}
