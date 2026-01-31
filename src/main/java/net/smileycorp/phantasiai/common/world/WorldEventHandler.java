package net.smileycorp.phantasiai.common.world;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.phantasiai.common.world.biomes.PhantasiaiBiomes;

public class WorldEventHandler {
    
    @SubscribeEvent
    public void genFeature(DecorateBiomeEvent.Decorate event) {
        if (event.getType() != DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA &&
                event.getType() != DecorateBiomeEvent.Decorate.EventType.LAKE_WATER) return;
        ChunkPos pos = event.getChunkPos();
        for (Biome biome : event.getWorld().getBiomeProvider().getBiomes(null, pos.getXStart(), pos.getZStart(), 16, 16)) {
            if (biome == PhantasiaiBiomes.ENCHANTED_THICKET) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }
    }
    
}
