package net.smileycorp.fbiomes.common.world.gen.features;

import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Map;
import java.util.Random;

public abstract class WorldGenTemplate extends WorldGenerator {
    
    private final ResourceLocation[] structures;
    
    protected WorldGenTemplate(ResourceLocation... structures) {
        this.structures = structures;
    }
    
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        WorldServer worldServer = (WorldServer) world;
        TemplateManager manager = worldServer.getStructureTemplateManager();
        Template template = manager.getTemplate(worldServer.getMinecraftServer(), structures[rand.nextInt(structures.length)]);
        Rotation rotation = Rotation.values()[rand.nextInt(4)];
        PlacementSettings settings = new PlacementSettings().setMirror(Mirror.values()[rand.nextInt(3)]).setRotation(rotation);
        BlockPos size = template.transformedSize(rotation);
        pos = pos.add(-size.getX() / 2, 0, -size.getZ() / 2);
        if (!world.getBlockState(pos.down()).equals(world.getBiome(pos.down()).topBlock)) return false;
        template.addBlocksToWorld(world, pos, settings, 18);
        for (Map.Entry<BlockPos, String> entry : template.getDataBlocks(pos, settings).entrySet()) handleDataMarker(entry.getValue(), entry.getKey(), worldServer, rand, settings);
        return true;
    }
    
    abstract protected void handleDataMarker(String function, BlockPos pos, WorldServer world, Random rand, PlacementSettings settings);
    
}
