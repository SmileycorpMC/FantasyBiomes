package net.smileycorp.phantasiai.common.world.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.phantasiai.common.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class PhantasiaiBiomes {
	public final static Biome DEAD_MARSH = new BiomeDeadMarsh();
	public final static Biome PEAT_MOOR = new BiomePeatMoor();
	public final static Biome FLOATING_MOUNTAIN = new BiomeFloatingMountain();
	public final static Biome IRON_HILLS = new BiomeIronHills();
	public final static Biome ENCHANTED_THICKET = new BiomeEnchantedThicket();
    
    @SubscribeEvent
    public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
        final IForgeRegistry<Biome> registry = event.getRegistry();
        registry.register(DEAD_MARSH);
        registry.register(PEAT_MOOR);
        registry.register(ENCHANTED_THICKET);
        //registry.register(FLOATING_MOUNTAIN);
        //registry.register(IRON_HILLS);
        initBiome(DEAD_MARSH, false, BiomeType.COOL, 5,
                BiomeDictionary.Type.PLAINS,
                BiomeDictionary.Type.SWAMP,
                BiomeDictionary.Type.SPARSE
               // BiomeDictionary.Type.SPOOKY
        );
        initBiome(PEAT_MOOR, true, BiomeType.COOL, 6,
                BiomeDictionary.Type.HILLS,
                BiomeDictionary.Type.SWAMP,
                BiomeDictionary.Type.SPARSE
                //BiomeDictionary.Type.SPOOKY
        );
        initBiome(ENCHANTED_THICKET, false, BiomeType.WARM, 1,
                BiomeDictionary.Type.FOREST,
                BiomeDictionary.Type.DENSE,
                //BiomeDictionary.Type.MAGICAL,
                //BiomeDictionary.Type.MUSHROOM,
                BiomeDictionary.Type.LUSH
        );
        /*initBiome(FLOATING_MOUNTAIN, false, BiomeType.COOL, 5,
                BiomeDictionary.Type.HILLS,
                BiomeDictionary.Type.MAGICAL,
                BiomeDictionary.Type.MOUNTAIN
        );
        initBiome(IRON_HILLS, false, BiomeType.ICY, 2,
                BiomeDictionary.Type.MOUNTAIN,
                BiomeDictionary.Type.COLD,
                BiomeDictionary.Type.SNOWY,
                BiomeDictionary.Type.HILLS);*/
    }
	
	public static void init() {
		
    }
	
	private static void initBiome(Biome biome, boolean hasVillages, BiomeType biomeType, int weight, BiomeDictionary.Type... types) {
		BiomeManager.addStrongholdBiome(biome);
        BiomeDictionary.addTypes(biome, types);
        if (hasVillages) BiomeManager.addVillageBiome(biome, true);
		BiomeManager.addBiome(biomeType, new BiomeEntry(biome, weight));
        BiomeManager.addSpawnBiome(biome);
	}
}

