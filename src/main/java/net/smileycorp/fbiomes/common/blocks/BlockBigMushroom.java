package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.EnumFBiomesParticle;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomShape;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomVariant;
import net.smileycorp.fbiomes.common.network.FBiomesParticleMessage;
import net.smileycorp.fbiomes.common.network.PacketHandler;

import java.util.Random;

public class BlockBigMushroom extends BlockBase {
	
	public static final PropertyEnum<EnumMushroomShape> SHAPE = PropertyEnum.create("shape", EnumMushroomShape.class);
	public static final PropertyEnum<EnumMushroomVariant> VARIANT = PropertyEnum.create("variant", EnumMushroomVariant.class);
	
	public BlockBigMushroom() {
		this("Big_Mushroom");
		setDefaultState(blockState.getBaseState().withProperty(SHAPE, EnumMushroomShape.CAP).withProperty(VARIANT, EnumMushroomVariant.GREEN));
	}
	
	public BlockBigMushroom(String name) {
		super(name, Constants.MODID, Material.WOOD, SoundType.WOOD, 0.2F, 0, "axe", 1, null);
		isFlamable = false;
		setCreativeTab(FantasyBiomes.TAB);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SHAPE, VARIANT);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(placer.getHeldItem(hand).getMetadata());
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, EnumMushroomVariant.values()[(meta-(meta % 3)) / 3])
				.withProperty(SHAPE, EnumMushroomShape.values()[meta % 3]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).getMeta() * 3 + state.getValue(SHAPE).getMeta();
	}
	
	@Override
	public int getMaxMeta(){
		return 6;
	}
	
	@Override
	public String byMeta(int meta) {
		return byState(getStateFromMeta(meta));
	}
	
	@Override
	public String byState(IBlockState state) {
		return "big_" + state.getValue(VARIANT).getName() + "_mushroom_" + state.getValue(SHAPE).getName();
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}
	
	@Override
	public int quantityDropped(Random rand) {
        return Math.max(0, rand.nextInt(10) - 7);
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(VARIANT).getDrop();
    }
	
	@Override
	public void getSubBlocks(CreativeTabs item, NonNullList<ItemStack> items) {
        for (int i = 0; i<this.getMaxMeta(); i++) items.add(new ItemStack(this, 1, i));
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, getMetaFromState(state));
    }
	
	@Override
	public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
		if (!isBouncy(world.getBlockState(pos))) super.onFallenUpon(world, pos, entity, fallDistance);
		else entity.fall(fallDistance, 0.0F);
	}
	
	@Override
	public void onLanded(World world, Entity entity) {
		BlockPos pos = entity.getPosition().down();
		IBlockState state = world.getBlockState(pos);
		if (!isBouncy(state)) super.onLanded(world, entity);
		else if (entity.motionY < -0.02) {
			if (world.isAirBlock(pos.down())) for (int i = 0; i < world.rand.nextInt(3) + 3; i++) {
				PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new FBiomesParticleMessage(EnumFBiomesParticle.PIXEL,
								pos.getX() + world.rand.nextFloat(), pos.getY() - 0.1f, pos.getZ() + world.rand.nextFloat(), getSporeColour(state)),
						new NetworkRegistry.TargetPoint(entity.dimension, pos.getX(), pos.getY(), pos.getZ(), 32));
			}
			if (entity.isSneaking()) super.onLanded(world, entity);
			else entity.motionY = Math.min(-entity.motionY * getBounceSpeed(state), getMaxBounce(state));
		}
	}
	
	protected double getSporeColour(IBlockState state) {
		return 0x899989;
	}
	
	protected boolean isBouncy(IBlockState state) {
		return state.getBlock() == this ? state.getValue(SHAPE).isBouncy() : false;
	}
	
	protected float getMaxBounce(IBlockState state) {
		return state.getBlock() == this ? state.getValue(VARIANT).getMaxBounce() : 0;
	}
	
	protected float getBounceSpeed(IBlockState state) {
		return state.getBlock() == this ? state.getValue(VARIANT).getBounceSpeed() : 0;
	}
	
}
