package net.smileycorp.phantasiai.common;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.smileycorp.phantasiai.common.entities.ICreatureType;
import net.smileycorp.phantasiai.common.world.biomes.PhantasiaiBiomes;

public class EventListener {
    
    @SubscribeEvent
    public void onSpawn(LivingSpawnEvent.CheckSpawn event) {
        if ((((ICreatureType)event.getEntity()).getCreatureType()) == null) return;
        if (((ICreatureType)event.getEntity()).getCreatureType().getPeacefulCreature()) return;
        if (event.isSpawner()) return;
        if (event.getY() < 65) return;
        if (event.getWorld().getBiome(new BlockPos(event.getEntity())) == PhantasiaiBiomes.ENCHANTED_THICKET) event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public void onCraft(PlayerEvent.ItemCraftedEvent event) {

    }
    
    
}
