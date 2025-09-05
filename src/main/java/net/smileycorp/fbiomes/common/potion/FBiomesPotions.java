package net.smileycorp.fbiomes.common.potion;

import com.google.common.collect.Maps;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;

import java.util.EnumMap;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class FBiomesPotions {
    
    public static final Potion BIOLUMINESCENCE = new PotionBioLuminescence();

    public static final EnumMap<EnumGlowshroomVariant, PotionTypeBioluminescence> BIOLUMINESCENCE_POTIONS = Maps.newEnumMap(EnumGlowshroomVariant.class);
    public static final EnumMap<EnumGlowshroomVariant, PotionTypeBioluminescence> EXTENDED_BIOLUMINESCENCE_POTIONS = Maps.newEnumMap(EnumGlowshroomVariant.class);
    
    private static PotionType register(String name, PotionEffect... effects) {
        PotionType type = new PotionType(name, effects);
        type.setRegistryName(Constants.loc(name));
        return type;
    }
    
    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        IForgeRegistry<Potion> registry = event.getRegistry();
        registry.register(BIOLUMINESCENCE);
    }
    
    @SubscribeEvent
    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        IForgeRegistry<PotionType> registry = event.getRegistry();
        for (EnumGlowshroomVariant variant : EnumGlowshroomVariant.values()) {
            PotionTypeBioluminescence potion = new PotionTypeBioluminescence(variant, false);
            BIOLUMINESCENCE_POTIONS.put(variant, potion);
            registry.register(potion);
            potion = new PotionTypeBioluminescence(variant, true);
            EXTENDED_BIOLUMINESCENCE_POTIONS.put(variant, potion);
            registry.register(potion);
        }
    }
    
}
