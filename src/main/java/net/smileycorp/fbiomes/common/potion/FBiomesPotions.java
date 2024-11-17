package net.smileycorp.fbiomes.common.potion;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.fbiomes.common.Constants;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class FBiomesPotions {
    
    //public static final PotionType GLOWING = register("glowing", new PotionEffect(MobEffects.GLOWING, 3600));
    //public static final PotionType LONGER_GLOWING = register("longer_glowing", new PotionEffect(MobEffects.GLOWING, 9600));
    
    private static PotionType register(String name, PotionEffect... effects) {
        PotionType type = new PotionType(name, effects);
        type.setRegistryName(Constants.loc(name));
        return type;
    }
    
    @SubscribeEvent
    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        IForgeRegistry<PotionType> registry = event.getRegistry();
        for (Field field : FBiomesPotions.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (!(object instanceof PotionType) || object == null) continue;
                registry.register((PotionType) object);
            } catch (Exception e) {}
        }
    }
    
}
