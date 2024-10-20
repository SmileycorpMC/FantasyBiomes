package net.smileycorp.fbiomes.common.blocks;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;

import javax.annotation.Nullable;

public class BlockFBMushroom extends BlockBush implements IBlockProperties {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>()
	{
        @Override
		public boolean apply(@Nullable EnumFacing p_apply_1_)
        {
            return p_apply_1_ != EnumFacing.DOWN;
        }
	});
	
	public BlockFBMushroom(String name, float light) {
		super(Material.PLANTS);
		setLightLevel(light);
		setCreativeTab(FantasyBiomes.creativeTab);
		setSoundType(SoundType.PLANT);
		setUnlocalizedName(Constants.name(name));
		setRegistryName(Constants.loc(name.toLowerCase()));
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{FACING});
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(placer.getHeldItem(hand).getMetadata())
				.withProperty(FACING, getFacing(world, pos, facing));
	}
	
	private EnumFacing getFacing(World world, BlockPos pos, EnumFacing facing) {
		if (canFacingSustain(world, pos, facing)) return facing;
		for (EnumFacing facing0 : FACING.getAllowedValues()) {
			if (facing0!=facing && canFacingSustain(world, pos, facing0)) return facing;
		}
		return EnumFacing.UP;
	}

	private boolean canFacingSustain(World world, BlockPos pos, EnumFacing facing) {
		if (facing==EnumFacing.DOWN) return false;
		IBlockState state = world.getBlockState(pos.offset(facing, -1));
		Block block = state.getBlock();
		if (state.isFullBlock() && (block instanceof BlockLog || state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND))
			return true;
		for (int id : OreDictionary.getOreIDs(new ItemStack(block))) {
			if (OreDictionary.getOreName(id).equals("stone")) return true;
		}
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BUSH_AABB;
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, getFacingFromMeta(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		EnumFacing facing = (EnumFacing) state.getProperties().get(FACING);
		switch (facing) {
			case NORTH:
				return 1;
			case SOUTH:
				return 2;
			case EAST:
				return 3;
			case WEST:
				return 4;
			default:
				return 0;
			}
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos){
        IBlockState soil = world.getBlockState(pos.down());
        for (EnumFacing facing : FACING.getAllowedValues()) {
        	IBlockState state = world.getBlockState(pos.offset(facing.getOpposite()));
        	if (state.isFullCube()&&(state.getBlock() instanceof BlockLog || state.getMaterial() == Material.GROUND || state.getMaterial() == Material.GRASS)) {
        		return true;
        	}
        }
        return soil.isFullBlock() && (soil.getMaterial()==Material.GRASS || soil.getMaterial()==Material.GROUND);
    }
	
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		IBlockState soil = world.getBlockState(pos.down());
        if (state.getBlock() == this) {
            return canPlaceBlockAt(world, pos);
        }
        return soil.isFullBlock() && (soil.getMaterial()==Material.GRASS || soil.getMaterial()==Material.GROUND);
    }
	 
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		IBlockState soil = world.getBlockState(pos.down());
		EnumFacing facing = world.getBlockState(pos).getValue(FACING);
    	if (!(world.getBlockState(pos.offset(facing.getOpposite())).getBlock() instanceof BlockLog)) {
    		if (!(facing==EnumFacing.UP&&(soil.isFullBlock() && (soil.getMaterial()==Material.GRASS || soil.getMaterial()==Material.GROUND)))){
    			Minecraft.getMinecraft().world.setBlockToAir(pos);
    		}
    	}
	 }
	
	@Override
	public int getMaxMeta(){
		return 4;
	}
	
	public EnumFacing getFacingFromMeta(int meta) {
		switch (meta) {
			case 1:
				return EnumFacing.NORTH;
			case 2:
				return EnumFacing.SOUTH;
			case 3:
				return EnumFacing.EAST;
			case 4:
				return EnumFacing.WEST;
			default:
				return EnumFacing.UP;
		}
	}
	

}
