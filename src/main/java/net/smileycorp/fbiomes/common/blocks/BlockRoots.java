package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;

import java.util.Random;

public class BlockRoots extends BlockBush implements IBlockProperties {

	public BlockRoots() {
		super(Material.PLANTS);
		setCreativeTab(FantasyBiomes.creativeTab);
		setUnlocalizedName(Constants.name("Roots"));
		setRegistryName(Constants.loc("Roots"));
		setSoundType(SoundType.PLANT);
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		 if (side == EnumFacing.DOWN){
			 IBlockState state = world.getBlockState(pos.up());
			 if (state.getMaterial() == Material.GROUND|| state.getMaterial() == Material.ROCK || state.getMaterial() == Material.GRASS || state.getBlock() == this) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		 IBlockState state = world.getBlockState(pos.up());
		 if (!(state.getMaterial() == Material.GROUND|| state.getMaterial() == Material.ROCK || state.getMaterial() == Material.GRASS || state.getBlock() == this)){
			 Minecraft.getMinecraft().world.setBlockToAir(pos);
		 }
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
        if (state.getBlock() == this) {
            return canPlaceBlockOnSide(world, pos, EnumFacing.DOWN);
        }
        return false;
    }

}
