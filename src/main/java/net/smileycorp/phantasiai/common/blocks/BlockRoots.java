package net.smileycorp.phantasiai.common.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.Phantasiai;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockRoots extends BlockBush implements BlockProperties, IShearable, IGrowable {

	public BlockRoots() {
		super(Material.PLANTS);
		setCreativeTab(Phantasiai.TAB);
		setUnlocalizedName(Constants.name("Roots"));
		setRegistryName(Constants.loc("Roots"));
		setSoundType(SoundType.PLANT);
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		 if (side != EnumFacing.DOWN) return false;
		 IBlockState state = world.getBlockState(pos.up());
		 if (state.getBlock() == this) return true;
		 return state.getMaterial() == Material.GROUND || state.getMaterial() == Material.ROCK
				 || state.getMaterial() == Material.GRASS || state.getBlock() == this;
	 }
	 
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		 IBlockState state = world.getBlockState(pos.up());
		 if (!(state.getMaterial() == Material.GROUND || state.getMaterial() == Material.ROCK || state.getMaterial() == Material.GRASS
				 || state.getBlock() == this) && world instanceof World) ((World) world).setBlockToAir(pos);
	 }
	 
	 @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		 return Items.STICK;
	 }
		 
	 @Override
	public int quantityDropped(Random rand) {
		 return rand.nextInt(4)+1;
	 }
	 
	 @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		 return new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 1D, 0.699999988079071D);
	 }
	 
	 @Override
	 public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return state.getBlock() == this && canPlaceBlockOnSide(world, pos, EnumFacing.DOWN);
    }


	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public void grow(World world, Random random, BlockPos pos, IBlockState state) {
		world.setBlockState(pos.down(), state);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isShearable(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return Lists.newArrayList(new ItemStack(this));
	}

}
