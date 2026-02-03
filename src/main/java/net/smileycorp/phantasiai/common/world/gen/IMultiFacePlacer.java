package net.smileycorp.phantasiai.common.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.phantasiai.common.blocks.BlockLichen;
import net.smileycorp.phantasiai.common.blocks.BlockWebCover;
import net.smileycorp.phantasiai.common.blocks.IMultifaceBlock;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface IMultiFacePlacer {
    
    default BlockLichen lichen() {
        return PhantasiaiBlocks.LICHEN.get(0);
    }
    
    default BlockWebCover web() {
        return PhantasiaiBlocks.WEB_COVER.get(0);
    }
    
    default <T extends Block & IMultifaceBlock> IBlockState getMultiface(T block, World world, BlockPos pos) {
        List<EnumFacing> dirs = Arrays.stream(EnumFacing.values()).filter(facing -> canPlace(world, pos, facing)).collect(Collectors.toList());
        return dirs.isEmpty() ? Blocks.AIR.getDefaultState() : block.getBlockStateFromFacing(dirs.toArray(new EnumFacing[dirs.size()]));
    }
    
    default boolean canPlace(World world, BlockPos pos, EnumFacing facing) {
        IBlockState state = world.getBlockState(pos.offset(facing));
        return state.isSideSolid(world, pos.offset(facing), facing.getOpposite()) && supportsMultiFace(state);
    }
    
    boolean supportsMultiFace(IBlockState state);
    
}
