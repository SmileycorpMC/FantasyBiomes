package net.smileycorp.phantasiai.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.smileycorp.phantasiai.common.blocks.BlockPixieJar;
import net.smileycorp.phantasiai.common.capabilities.PhantasiaiCapabilities;
import net.smileycorp.phantasiai.common.capabilities.PixieStorage;
import net.smileycorp.phantasiai.common.items.ItemPixieBottle;
import net.smileycorp.phantasiai.common.world.biomes.PhantasiaiBiomes;

public class EventListener {

    @SubscribeEvent
    public void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        Item item = stack.getItem();
        if (!(item instanceof ItemPixieBottle) &! (item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockPixieJar)) return;
        event.addCapability(Constants.loc("pixie_storage"), new PixieStorage.PixieStorageItemStack(stack));
    }
    
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
        ItemStack stack = event.crafting;
        if (!stack.hasCapability(PhantasiaiCapabilities.PIXIE_STORAGE, null)) return;
        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
            ItemStack stack1 = event.craftMatrix.getStackInSlot(i);
            if (!stack1.hasCapability(PhantasiaiCapabilities.PIXIE_STORAGE, null)) continue;
            stack.getCapability(PhantasiaiCapabilities.PIXIE_STORAGE, null)
                    .setPixie(stack1.getCapability(PhantasiaiCapabilities.PIXIE_STORAGE, null).getPixie());
        }
    }
    
    
}
