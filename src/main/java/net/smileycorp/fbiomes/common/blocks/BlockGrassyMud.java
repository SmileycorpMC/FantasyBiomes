package net.smileycorp.fbiomes.common.blocks;

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
import net.smileycorp.atlas.api.block.BlockGrassBase;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMudType;

import javax.annotation.Nullable;

public class BlockGrassyMud extends BlockGrassBase {
    
	public BlockGrassyMud() {
		super("grassy_mud", Constants.MODID, FantasyBiomes.TAB, BlockGrassyMud::removeGrass);
        setDefaultState(blockState.getBaseState().withProperty(BlockMud.VARIANT, EnumMudType.MUD));
	}
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        return getDefaultState().withProperty(BlockMud.VARIANT, EnumMudType.get(stack.getMetadata()));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockMud.VARIANT, SNOWY);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockMud.VARIANT).ordinal();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockMud.VARIANT, EnumMudType.get(meta));
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
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return BlockMud.AABB;
    }
    
    @Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        entity.motionX *= 0.8d;
        entity.motionZ *= 0.8d;
    }
    
    private static IBlockState removeGrass(IBlockState state) {
        return FBiomesBlocks.MUD.getDefaultState().withProperty(BlockMud.VARIANT, state.getValue(BlockMud.VARIANT));
    }

}
