package net.smileycorp.fbiomes.common.world.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder("fbiomes")
public class FBiomes {
	public final static Biome PRAIRIE = new BiomePrairie();
	public final static Biome FLOATING_MOUNTAIN = new BiomeFloatingMountain();
	public final static Biome IRON_HILLS = new BiomeIronHills();
	
	@Mod.EventBusSubscriber(modid = "fbiomes")
    public static class BiomeRegister {
		@SubscribeEvent
        public static void registerBiomes(final RegistryEvent.Register<Biome> event)
        {
            final IForgeRegistry<Biome> registry = event.getRegistry();

            System.out.println("Registering biomes");
            
            registry.register(PRAIRIE);
            registry.register(FLOATING_MOUNTAIN);
            registry.register(IRON_HILLS);
        }
		
	}
	
	public static void init() {
        BiomeManager.addBiome(BiomeType.WARM, new BiomeEntry(PRAIRIE, 10));
        BiomeManager.addSpawnBiome(PRAIRIE);
        BiomeManager.addStrongholdBiome(PRAIRIE);
        BiomeManager.addVillageBiome(PRAIRIE, true);
        BiomeDictionary.addTypes(PRAIRIE, 
                BiomeDictionary.Type.PLAINS,
                BiomeDictionary.Type.SAVANNA,
                BiomeDictionary.Type.LUSH
                );
        BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(FLOATING_MOUNTAIN, 10));
        BiomeManager.addSpawnBiome(FLOATING_MOUNTAIN);
        BiomeManager.addStrongholdBiome(FLOATING_MOUNTAIN);
        BiomeDictionary.addTypes(FLOATING_MOUNTAIN, 
                BiomeDictionary.Type.HILLS,
                BiomeDictionary.Type.MAGICAL,
                BiomeDictionary.Type.MOUNTAIN
                );
        BiomeManager.addBiome(BiomeType.ICY, new BiomeEntry(IRON_HILLS, 10));
        BiomeManager.addSpawnBiome(IRON_HILLS);
        BiomeManager.addStrongholdBiome(IRON_HILLS);
        BiomeDictionary.addTypes(IRON_HILLS, 
                BiomeDictionary.Type.MOUNTAIN,
                BiomeDictionary.Type.COLD,
                BiomeDictionary.Type.SNOWY,
                BiomeDictionary.Type.HILLS
                );
    }
}

