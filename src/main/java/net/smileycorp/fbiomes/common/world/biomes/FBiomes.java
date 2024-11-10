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
import net.smileycorp.fbiomes.common.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class FBiomes {
	public final static Biome DEAD_MARSH = new BiomeDeadMarsh();
	public final static Biome PEAT_MOOR = new BiomePeatMoor();
	public final static Biome FLOATING_MOUNTAIN = new BiomeFloatingMountain();
	public final static Biome IRON_HILLS = new BiomeIronHills();
	public final static Biome ENCHANTED_THICKET = new EnchantedThicket();
    
    @SubscribeEvent
    public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
        final IForgeRegistry<Biome> registry = event.getRegistry();
        registry.register(DEAD_MARSH);
        registry.register(PEAT_MOOR);
        registry.register(ENCHANTED_THICKET);
        initBiome(DEAD_MARSH, false, BiomeType.COOL, 7,
                BiomeDictionary.Type.PLAINS,
                BiomeDictionary.Type.SWAMP,
                BiomeDictionary.Type.SPARSE
        );
        initBiome(PEAT_MOOR, true, BiomeType.COOL, 8,
                BiomeDictionary.Type.HILLS,
                BiomeDictionary.Type.SWAMP,
                BiomeDictionary.Type.SPARSE
        );
        initBiome(ENCHANTED_THICKET, false, BiomeType.WARM, 5,
                BiomeDictionary.Type.FOREST,
                BiomeDictionary.Type.DENSE,
                BiomeDictionary.Type.MAGICAL,
                BiomeDictionary.Type.MUSHROOM,
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

