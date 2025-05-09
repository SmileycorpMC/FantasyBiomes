package net.smileycorp.fbiomes.common.world.gen.features.enchantedthicket;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.fbiomes.common.blocks.BlockMysticStumpPart;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.entities.EntityPixie;

import java.util.Random;

public class WorldGenMysticStump extends WorldGenerator {
    
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.add(-1, 0, -1));
        for (int i = -1; i < 2; i++) {
            for (int k = -1; k < 2; k++) {
                if (!world.isAirBlock(mutable)) return false;
                if (!world.isSideSolid(mutable.move(EnumFacing.DOWN), EnumFacing.UP, false)) return false;
                mutable.setY(pos.getY());
                mutable.move(EnumFacing.NORTH);
            }
            mutable.move(EnumFacing.NORTH);
        }
        setBlockAndNotifyAdequately(world, pos, FBiomesBlocks.MYSTIC_STUMP.getDefaultState());
        for (BlockMysticStumpPart.Direction direction : BlockMysticStumpPart.Direction.values()) setBlockAndNotifyAdequately(world,
                pos.add(direction.getOffset()), FBiomesBlocks.MYSTIC_STUMP_PART.getDefaultState().withProperty(BlockMysticStumpPart.DIRECTION, direction));
        for (int i = 0; i < 3; i++) {
            EntityPixie pixie = new EntityPixie(world);
            pixie.setVariant(EntityPixie.Variant.GLASSWING);
            pixie.setPosition(pos.getX() + rand.nextFloat() - 0.5f, pos.getY() + 1.5f, pos.getZ() + rand.nextFloat() - 0.5f);
            world.spawnEntity(pixie);
        }
        return true;
    }
    
}
