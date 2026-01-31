package net.smileycorp.fbiomes.common.world.gen.features.enchantedthicket;

import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FBiomesLootTables;
import net.smileycorp.fbiomes.common.entities.EntityPixie;
import net.smileycorp.fbiomes.common.entities.EntityWitchTrader;
import net.smileycorp.fbiomes.common.entities.PixieData;
import net.smileycorp.fbiomes.common.world.gen.features.WorldGenTemplate;

import java.util.Random;

public class WorldGenWitchCottage extends WorldGenTemplate {
    
    public WorldGenWitchCottage() {
        super(Constants.loc("enchanted_thicket/witch_cottage"));
    }
    
    @Override
    protected void handleDataMarker(String function, BlockPos pos, WorldServer world, Random rand, PlacementSettings settings) {
        switch (function) {
            case "chest":
                setBlockAndNotifyAdequately(world, pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.WEST)
                        .withMirror(settings.getMirror()).withRotation(settings.getRotation()));
                TileEntityChest chest = new TileEntityChest(BlockChest.Type.BASIC);
                chest.setLootTable(FBiomesLootTables.WITCH_COTTAGE, rand.nextLong());
                world.setTileEntity(pos, chest);
                break;
            case "witch":
                EntityWitchTrader witch = new EntityWitchTrader(world);
                witch.enablePersistence();
                witch.moveToBlockPosAndAngles(pos, 0, 0);
                world.spawnEntity(witch);
                EntityPixie pixie = PixieData.newPixie(PixieData.Variant.MALACHITE, rand)
                        .spawn(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
                pixie.setOwner(witch);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                break;
            case "pixie":
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                break;
        }
    }
    
}
