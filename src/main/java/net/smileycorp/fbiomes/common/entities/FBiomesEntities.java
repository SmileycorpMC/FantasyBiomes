package net.smileycorp.fbiomes.common.entities;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.fbiomes.common.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class FBiomesEntities {
    
    private static int ID = 177;
    public static final EntityEntry PIXIE = EntityEntryBuilder.create().entity(EntityPixie.class).id(Constants.loc("pixie"), ID++).name(Constants.name("pixie"))
            .egg(0x68C0FF, 0xF3621F).tracker(64, 3, true).build();
    
    
    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        IForgeRegistry<EntityEntry> registry = event.getRegistry();
        registry.register(PIXIE);
    }
    
}
