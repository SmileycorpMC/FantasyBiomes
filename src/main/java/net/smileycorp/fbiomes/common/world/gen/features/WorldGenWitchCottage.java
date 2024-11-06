package net.smileycorp.fbiomes.common.world.gen.features;

import net.minecraft.block.BlockChest;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.entities.EntityPixie;

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
                chest.setLootTable(Constants.loc("chests/witch_cottage"), rand.nextLong());
                world.setTileEntity(pos, chest);
                break;
            case "witch":
                EntityWitch witch = new EntityWitch(world);
                witch.posX = pos.getX() + 0.5;
                witch.posY = pos.getY();
                witch.posZ = pos.getZ() + 0.5;
                witch.enablePersistence();
                world.spawnEntity(witch);
                break;
            case "pixie":
                EntityPixie pixie = new EntityPixie(world);
                pixie.setVariant((byte) (rand.nextInt(3) + 1));
                pixie.posX = pos.getX() + 0.5;
                pixie.posY = pos.getY();
                pixie.posZ = pos.getZ() + 0.5;
                pixie.enablePersistence();
                world.spawnEntity(pixie);
                break;
        }
    }
    
}
