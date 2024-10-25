package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;

import javax.annotation.Nullable;

public class BlockMud extends BlockBase {
    
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);
    
	public BlockMud(String name) {
		super(name, Constants.MODID, Material.CLAY, SoundType.GROUND, 0.5f, 1,  "shovel", 0, FantasyBiomes.TAB);
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
        entity.motionX *= 0.8D;
        entity.motionZ *= 0.8D;
    }

}
