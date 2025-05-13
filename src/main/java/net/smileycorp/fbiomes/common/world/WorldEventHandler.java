package net.smileycorp.fbiomes.common.world;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.fbiomes.common.world.biomes.FBiomes;

public class WorldEventHandler {
    
    @SubscribeEvent
    public void genFeature(DecorateBiomeEvent.Decorate event) {
        if (event.getType() != DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA &&
                event.getType() != DecorateBiomeEvent.Decorate.EventType.LAKE_WATER) return;
        ChunkPos pos = event.getChunkPos();
        for (Biome biome : event.getWorld().getBiomeProvider().getBiomes(null, pos.getXStart(), pos.getZStart(), 16, 16)) {
            if (biome == FBiomes.ENCHANTED_THICKET) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }
    }
    
}
