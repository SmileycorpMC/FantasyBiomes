package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.tiles.TileMysticStump;
import net.smileycorp.fbiomes.common.items.FBiomesItems;

public class BlockMysticStump extends BlockBase {
    
    //Credit: slava_110
    
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool HOUSES = PropertyBool.create("houses");
    
    public BlockMysticStump() {
        super("mystic_stump", Constants.MODID, Material.WOOD, SoundType.WOOD, 2, 5,  "axe", 0, FantasyBiomes.TAB);
        setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(HOUSES, false));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, HOUSES);
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        for(BlockPos mutable : BlockPos.getAllInBoxMutable(
                pos.add(-1, 0, -1), pos.add(1, 0, 1)))
            if(!world.getBlockState(mutable).getBlock().isReplaceable(world, mutable)) return false;
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        for (BlockMysticStumpPart.Direction direction : BlockMysticStumpPart.Direction.values()) world.setBlockState(pos.add(direction.getOffset()),
                FBiomesBlocks.MYSTIC_STUMP_PART.getDefaultState().withProperty(BlockMysticStumpPart.DIRECTION, direction));
    }
    
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote) return;
        breakStump(world, pos);
    }
    
    public static void breakStump(World world, BlockPos pos) {
        for(BlockPos mutable : BlockPos.getAllInBoxMutable(
                pos.add(-1, 0, -1), pos.add(1, 0, 1)))
            if (world.getBlockState(mutable).getBlock() == (pos.equals(mutable)
                    ? FBiomesBlocks.MYSTIC_STUMP : FBiomesBlocks.MYSTIC_STUMP_PART)) world.setBlockToAir(mutable);
    }
    
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withProperty(FACING, mirror.mirror(state.getValue(FACING)));
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite())
                .withProperty(HOUSES, stack.getMetadata() == 1);
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult rayTrace, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(FBiomesBlocks.MYSTIC_STUMP, 1, state.getValue(HOUSES) ? 1 : 0);
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState p_149656_1_) {
        return EnumPushReaction.BLOCK;
    }
    
    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(hand == EnumHand.OFF_HAND || world.isRemote) return false;
        ItemStack stack = player.getHeldItem(hand);
        if (!state.getValue(HOUSES)) {
            if (stack.getItem() == FBiomesItems.PIXIE_HOUSING) {
                world.setBlockState(pos, state.withProperty(HOUSES, true));
                return true;
            }
            return false;
        }
        TileEntity tile = world.getTileEntity(pos);
        if (stack.getItem() == FBiomesItems.PIXIE_BOTTLE && tile instanceof TileMysticStump
                && ((TileMysticStump) tile).getPixieCount() < 3) {
            if (!player.isCreative()) stack.shrink(1);
            ((TileMysticStump) tile).addPixie(stack);
            return true;
        }
        if (stack.getItem() == Items.GLASS_BOTTLE && tile instanceof TileMysticStump
                && ((TileMysticStump) tile).getPixieCount() > 0) {
            ItemStack pixie = ((TileMysticStump) tile).bottlePixie();
            if (!player.isCreative()) stack.shrink(1);
            if (!player.addItemStackToInventory(pixie)) player.dropItem(stack, false);
            return true;
        }
        player.openGui(FantasyBiomes.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4))
                .withProperty(HOUSES, meta >= 4);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(HOUSES) ? 4 : 0) + state.getValue(FACING).getHorizontalIndex();
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileMysticStump();
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < 2; i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public int getMaxMeta() {
        return 2;
    }
    
    @Override
    public String byMeta(int meta) {
        return meta == 1 ? "mystic_stump_houses" : "mystic_stump";
    }
    
}
