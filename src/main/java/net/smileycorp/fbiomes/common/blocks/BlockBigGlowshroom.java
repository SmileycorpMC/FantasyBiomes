package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SHAPE, VARIANT);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, EnumGlowshroomVariant.values()[(meta - (meta % 2)) / 2])
				.withProperty(SHAPE, EnumBigMushroomShape.values()[meta % 2]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).getMeta() * 2 + state.getValue(SHAPE).getMeta();
	}
	
	@Override
	public int getMaxMeta(){
		return 10;
	}
	
	@Override
	public String byMeta(int meta) {
		return byState(getStateFromMeta(meta));
	}
	
	@Override
	public String byState(IBlockState state) {
		return "big_" + state.getValue(VARIANT).getName() + "_glowshroom_" + state.getValue(SHAPE).getName();
	}
	
	@Override
	public int getLightValue(IBlockState state) {
		return state.getValue(SHAPE) == EnumBigMushroomShape.CAP ? 15 : 5;
	}
	
	@Override
	public boolean canSilkHarvest(World p_canSilkHarvest_1_, BlockPos p_canSilkHarvest_2_, IBlockState p_canSilkHarvest_3_, EntityPlayer p_canSilkHarvest_4_) {
		return true;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(VARIANT).getDrop();
    }
	
}
