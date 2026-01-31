package net.smileycorp.fbiomes.common.entities;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.fbiomes.common.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class FBiomesEntities {
    
    private static int ID = 177;
    public static final EntityEntry PIXIE = EntityEntryBuilder.create().entity(EntityPixie.class).id(Constants.loc("pixie"), ID++).name(Constants.name("pixie"))
            .egg(0x68C0FF, 0xF3621F).tracker(64, 3, true).build();
    public static final EntityEntry WITCH_TRADER = EntityEntryBuilder.create().entity(EntityWitchTrader.class).id(Constants.loc("witch_trader"), ID++).name(Constants.name("witch_trader"))
            .egg(0x51344A, 0x212636).tracker(64, 3, true).build();
    
    
    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        IForgeRegistry<EntityEntry> registry = event.getRegistry();
        registry.register(PIXIE);
        registry.register(WITCH_TRADER);
    }

    @SubscribeEvent
    public static void registerDataSerializers(RegistryEvent.Register<DataSerializerEntry> event) {
        IForgeRegistry<DataSerializerEntry> registry = event.getRegistry();
        registry.register(new DataSerializerEntry(PixieData.SERIALIZER).setRegistryName(Constants.loc("pixie_data")));
    }
    
}
