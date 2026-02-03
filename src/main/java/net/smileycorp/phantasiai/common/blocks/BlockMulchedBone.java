package net.smileycorp.phantasiai.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.Phantasiai;
import net.smileycorp.phantasiai.common.PhantasiaiLootTables;
import net.smileycorp.phantasiai.common.blocks.enums.EnumMudType;
import net.smileycorp.phantasiai.common.blocks.enums.EnumMulchedBoneType;

import javax.annotation.Nullable;

public class BlockMulchedBone extends BlockBase {

    public static final PropertyEnum<EnumMulchedBoneType> VARIANT = PropertyEnum.create("variant", EnumMulchedBoneType.class);

    public BlockMulchedBone() {
        super("mulched_bone", Constants.MODID, Material.CLAY, SoundType.GROUND, 1f, 1,  "shovel", 0, Phantasiai.TAB);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, EnumMulchedBoneType.MUD));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        return getDefaultState().withProperty(VARIANT, EnumMulchedBoneType.get(stack.getMetadata()));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, EnumMulchedBoneType.get(meta));
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < EnumMulchedBoneType.values().length; i++) items.add(new ItemStack(this, 1, i));
    }

    @Override
    public int getMaxMeta() {
        return EnumMulchedBoneType.values().length;
    }

    @Override
    public String byMeta(int meta) {
        return "mulched_bone_" + EnumMulchedBoneType.get(meta).getName();
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess blockAccess, BlockPos pos, IBlockState state, int fortune) {
        if (!(blockAccess instanceof WorldServer)) return;
        WorldServer world = (WorldServer) blockAccess;
        LootTable table = world.getLootTableManager().getLootTableFromLocation(PhantasiaiLootTables.MULCHED_BONE);
        if (table == null) return;
        LootContext.Builder builder = new LootContext.Builder(world);
        EntityPlayer player = harvesters.get();
        builder.withPlayer(player);
        builder.withLuck(player.getLuck() + fortune);
        drops.add(table.generateLootForPools(world.rand, builder == null ? null : builder.build()).get(0));
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(VARIANT).slowsPlayer() ? BlockMud.AABB : FULL_BLOCK_AABB;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!state.getValue(VARIANT).slowsPlayer()) return;
        entity.motionX *= 0.8d;
        entity.motionZ *= 0.8d;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

}
