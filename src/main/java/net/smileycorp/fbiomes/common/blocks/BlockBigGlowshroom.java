package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.smileycorp.fbiomes.common.blocks.enums.EnumBigMushroomShape;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;

import java.util.Random;

public class BlockBigGlowshroom extends BlockBigMushroom {
	
	public static final PropertyEnum<EnumGlowshroomVariant> VARIANT = PropertyEnum.create("variant", EnumGlowshroomVariant.class);
	public static final PropertyEnum<EnumBigMushroomShape> SHAPE = PropertyEnum.create("shape", EnumBigMushroomShape.class, EnumBigMushroomShape.CAP, EnumBigMushroomShape.STEM);
	
	public BlockBigGlowshroom() {
		super("Big_Glowshroom");
		setDefaultState(blockState.getBaseState().withProperty(SHAPE, EnumBigMushroomShape.CAP).withProperty(VARIANT, EnumGlowshroomVariant.BLUE));
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(VARIANT).getDrop();
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SHAPE, VARIANT);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= this.getMaxMeta()) return this.getDefaultState();
		EnumBigMushroomShape shape = EnumBigMushroomShape.values()[meta % 2];
		EnumGlowshroomVariant variant = EnumGlowshroomVariant.values()[(meta - (meta % 2)) / 2];
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
		return state.getValue(SHAPE)== EnumBigMushroomShape.CAP ? 15 : 5;
	}
	
}
