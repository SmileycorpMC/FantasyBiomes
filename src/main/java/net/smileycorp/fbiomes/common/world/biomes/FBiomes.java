package net.smileycorp.fbiomes.common.world.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class FBiomes {
	public final static Biome FEN = new BiomeDeadMarsh();
	public final static Biome MOORLAND = new BiomeMoorland();
	public final static Biome FLOATING_MOUNTAIN = new BiomeFloatingMountain();
	public final static Biome IRON_HILLS = new BiomeIronHills();
	public final static Biome FUNGI_FOREST = new BiomeFungiForest();
	
	@Mod.EventBusSubscriber(modid = "fbiomes")
    public static class BiomeRegister {
		@SubscribeEvent
        public static void registerBiomes(final RegistryEvent.Register<Biome> event)
        {
            final IForgeRegistry<Biome> registry = event.getRegistry();

            System.out.println("Registering biomes");
            
            registry.register(FEN);
            registry.register(MOORLAND);
            registry.register(FLOATING_MOUNTAIN);
            registry.register(IRON_HILLS);
            registry.register(FUNGI_FOREST);
        }
		
	}
	
	public static void init() {
		initBiome(FEN, true, BiomeType.COOL, 15,  
                BiomeDictionary.Type.PLAINS,
                BiomeDictionary.Type.SWAMP,
                BiomeDictionary.Type.SPARSE
                );
		initBiome(MOORLAND, true, BiomeType.COOL, 15,  
                BiomeDictionary.Type.HILLS,
                BiomeDictionary.Type.SWAMP,
                BiomeDictionary.Type.SPARSE
                );
        initBiome(FLOATING_MOUNTAIN, false, BiomeType.COOL, 5,
                BiomeDictionary.Type.HILLS,
                BiomeDictionary.Type.MAGICAL,
                BiomeDictionary.Type.MOUNTAIN
                );
        initBiome(IRON_HILLS, false, BiomeType.ICY, 2,
                BiomeDictionary.Type.MOUNTAIN,
                BiomeDictionary.Type.COLD,
                BiomeDictionary.Type.SNOWY,
                BiomeDictionary.Type.HILLS);
        initBiome(FUNGI_FOREST, false, BiomeType.WARM, 5,  
                BiomeDictionary.Type.FOREST,
                BiomeDictionary.Type.DENSE,
                BiomeDictionary.Type.MAGICAL,
                BiomeDictionary.Type.MUSHROOM,
                BiomeDictionary.Type.LUSH
                );
    }
	
	private static void initBiome(Biome biome, boolean hasVillages, BiomeType biomeType, int weight, BiomeDictionary.Type... types) {
		BiomeManager.addStrongholdBiome(biome);
        BiomeDictionary.addTypes(biome, types);
        BiomeManager.addVillageBiome(biome, hasVillages);
		BiomeManager.addBiome(biomeType, new BiomeEntry(biome, weight));
        BiomeManager.addSpawnBiome(biome);
	}
}

