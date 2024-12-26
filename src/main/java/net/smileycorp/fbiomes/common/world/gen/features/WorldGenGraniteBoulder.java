package net.smileycorp.fbiomes.common.world.gen.features;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.Random;

public class WorldGenGraniteBoulder extends WorldGenBoulder {
    
    @Override
    protected IBlockState getBlockState(Random rand) {
        return Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, rand.nextInt(3) == 1
                ? BlockStone.EnumType.GRANITE_SMOOTH : BlockStone.EnumType.GRANITE);
    }
    
}
