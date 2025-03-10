package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.fbiomes.common.EnumParticle;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomShape;

import java.util.Random;

public class BlockBigGlowshroom extends BlockBigMushroom {
	
	public static final PropertyEnum<EnumGlowshroomVariant> VARIANT = PropertyEnum.create("variant", EnumGlowshroomVariant.class);
	public static final PropertyEnum<EnumMushroomShape> SHAPE = PropertyEnum.create("shape", EnumMushroomShape.class, EnumMushroomShape.CAP, EnumMushroomShape.STEM);
	
	public BlockBigGlowshroom() {
		super("Big_Glowshroom");
		setDefaultState(blockState.getBaseState().withProperty(SHAPE, EnumMushroomShape.CAP).withProperty(VARIANT, EnumGlowshroomVariant.BLUE));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SHAPE, VARIANT);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, EnumGlowshroomVariant.values()[(meta - (meta % 2)) / 2])
				.withProperty(SHAPE, EnumMushroomShape.values()[meta % 2]);
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
		return state.getValue(SHAPE) == EnumMushroomShape.CAP ? 15 : 5;
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(VARIANT).getDrop();
    }
	
	@Override
	protected boolean isBouncy(IBlockState state) {
		if (state.getBlock() != this) return false;
		return state.getValue(SHAPE).isBouncy();
	}
	
	@Override
	protected float getMaxBounce(IBlockState state) {
		return state.getBlock() == this ? 0.9f : 0;
	}
	
	@Override
	protected float getBounceSpeed(IBlockState state) {
		return state.getBlock() == this ? 1.3f : 0;
	}
	
	protected void spawnSpores(World world, BlockPos pos, IBlockState state, Entity entity) {
		for (int i = 0; i < world.rand.nextInt(3) + 3; i++)
			EnumParticle.PIXEL_FULLBRIGHT.send(entity.dimension, pos.getX() + world.rand.nextFloat(), pos.getY() - 0.1f,
					pos.getZ() + world.rand.nextFloat(), getSporeColour(state), 100d, 0d, -0.05, 0d);
	}
	
	@Override
	protected double getSporeColour(IBlockState state) {
		return state.getValue(VARIANT).getColour();
	}
	
}
