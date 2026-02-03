package net.smileycorp.phantasiai.common;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.smileycorp.phantasiai.common.world.biomes.PhantasiaiBiomes;

public class EventListener {
    
    @SubscribeEvent
    public void onSpawn(WorldEvent.PotentialSpawns event) {
        if (event.getType() == null) return;
        if (event.getType().getPeacefulCreature()) return;
        BlockPos pos = event.getPos();
        if (pos.getY() < 65) return;
        if (event.getWorld().getBiome(pos) == PhantasiaiBiomes.ENCHANTED_THICKET) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onCraft(PlayerEvent.ItemCraftedEvent event) {

    }
    
    
}
