package net.smileycorp.phantasiai.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.Phantasiai;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockMysticStumpPart extends BlockBase {
    
    //Credit: slava_110
    
    public static final PropertyEnum<Direction> DIRECTION = PropertyEnum.create("direction", Direction.class);
    
    public BlockMysticStumpPart() {
        super("mystic_stump_part", Constants.MODID, Material.WOOD, SoundType.WOOD, 2, 5,  "axe", 0, Phantasiai.TAB);
        disableStats();
        setLightOpacity(0);
        translucent = true;
        fullBlock = false;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DIRECTION);
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(DIRECTION).getHitbox();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(DIRECTION, Direction.get(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DIRECTION).ordinal();
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return false;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(PhantasiaiBlocks.MYSTIC_STUMP);
    }
    
    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return true;
    }
    
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote) return;
        BlockPos masterPos = state.getValue(DIRECTION).getMain(pos);
        if (masterPos == null) return;
        BlockMysticStump.breakStump(world, masterPos);
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
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
    public ItemStack getPickBlock(IBlockState state, RayTraceResult result, World world, BlockPos pos, EntityPlayer player) {
        BlockPos stump = getStumpPos(world, pos);
        return world.getBlockState(stump).getBlock() == PhantasiaiBlocks.MYSTIC_STUMP ?
                PhantasiaiBlocks.MYSTIC_STUMP.getPickBlock(world.getBlockState(stump), result, world, stump, player)
                : new ItemStack(PhantasiaiBlocks.MYSTIC_STUMP);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote) return false;
            BlockPos stump = getStumpPos(world, pos);
            if(stump != null) return PhantasiaiBlocks.MYSTIC_STUMP.onBlockActivated(world, stump, world.getBlockState(stump),
                    player, hand, facing, hitX, hitY, hitZ);
        return false;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Nullable
    private static BlockPos getStumpPos(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getValue(DIRECTION).getMain(pos);
    }
    
    @Override
    public boolean usesCustomItemHandler() {
        return true;
    }
    
    public enum Direction implements IStringSerializable {
        
        NORTH("north", new Vec3i(0, 0, -1),
                new AxisAlignedBB(0, 0, 0.1875, 1, 1, 1)),
        NORTHEAST("northeast", new Vec3i(1, 0, -1),
                new AxisAlignedBB(0, 0, 0.1875, 0.8125, 1, 1)),
        EAST("east", new Vec3i(1, 0, 0),
                new AxisAlignedBB(0, 0, 0, 0.8125, 1, 1)),
        SOUTHEAST("southeast", new Vec3i(1, 0, 1),
                new AxisAlignedBB(0, 0, 0, 0.8125, 1, 0.8125)),
        SOUTH("south", new Vec3i(0, 0, 1),
                new AxisAlignedBB(0, 0, 0, 1, 1, 0.8125)),
        SOUTHWEST("southwest", new Vec3i(-1, 0, 1),
                new AxisAlignedBB(0.1875, 0, 0, 1, 1, 0.8125)),
        WEST("west", new Vec3i(-1, 0, 0),
                new AxisAlignedBB(0.1875, 0, 0, 1, 1, 1)),
        NORTHWEST("northwest", new Vec3i(-1, 0, -1),
                new AxisAlignedBB(0.1875, 0, 0.1875, 1, 1, 1));
        
        private final String name;
        private final Vec3i offset;
        private final AxisAlignedBB hitbox;
        
        Direction(String name, Vec3i offset, AxisAlignedBB hitbox) {
            this.name = name;
            this.offset = offset;
            this.hitbox = hitbox;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        public BlockPos getMain(BlockPos pos) {
            return pos.add(new Vec3i(-offset.getX(), 0, -offset.getZ()));
        }
        
        public Vec3i getOffset() {
            return offset;
        }
        
        public static Direction get(int meta) {
            return meta > 8 ? Direction.NORTH : values()[meta];
        }
        
        public AxisAlignedBB getHitbox() {
            return hitbox;
        }
        
    }
}
