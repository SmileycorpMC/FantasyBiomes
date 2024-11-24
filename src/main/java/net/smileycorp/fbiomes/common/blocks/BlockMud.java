package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMudType;

import javax.annotation.Nullable;

public class BlockMud extends BlockBase {
    
    public static final PropertyEnum<EnumMudType> VARIANT = PropertyEnum.create("variant", EnumMudType.class);
    public static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);
    
	public BlockMud() {
		super("mud", Constants.MODID, Material.CLAY, SoundType.GROUND, 0.5f, 1,  "shovel", 0, FantasyBiomes.TAB);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, EnumMudType.MUD));
	}
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        return getDefaultState().withProperty(VARIANT, EnumMudType.get(stack.getMetadata()));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, EnumMudType.get(meta));
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < EnumMudType.values().length; i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public int getMaxMeta() {
        return EnumMudType.values().length;
    }
    
    @Override
    public String byMeta(int meta) {
        return EnumMudType.get(meta).getName();
    }
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)	{
        EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));
        if (plantType == EnumPlantType.Plains || plantType == EnumPlantType.Water) return true;
        if (plantType == EnumPlantType.Beach) for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL.facings())
            if (world.getBlockState(pos.offset(facing)).getMaterial() == Material.WATER) return true;
        return false;
    }
	
	@Override
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AABB;
    }
    
    @Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        entity.motionX *= 0.8d;
        entity.motionZ *= 0.8d;
    }

}
