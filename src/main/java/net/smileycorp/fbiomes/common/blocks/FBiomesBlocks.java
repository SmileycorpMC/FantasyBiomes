package net.smileycorp.fbiomes.common.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.ShapedBlock;
import net.smileycorp.atlas.api.block.wood.BlockBaseLeaves;
import net.smileycorp.atlas.api.block.wood.BlockBaseSapling;
import net.smileycorp.atlas.api.block.wood.WoodBlock;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomVariant;
import net.smileycorp.fbiomes.common.blocks.enums.EnumVanillaWoodType;
import net.smileycorp.fbiomes.common.blocks.enums.EnumWoodType;
import net.smileycorp.fbiomes.common.world.gen.fungusforest.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = Constants.MODID)
public class FBiomesBlocks {
	
	public static List<Block> BLOCKS = Lists.newArrayList();
	
	//Surface Blocks
	public static Block MUD = new BlockMud("Mud");
	public static Block PEAT = new BlockMud("Peat");
	
	//Decorations
	//public static Block FLOWER;
	
	public static BlockFBMushroom TOADSTOOL = new BlockFBMushroom("Toadstool", 0.1F, () -> new WorldGenSmallToadstool(), null);
	public static BlockFBMushroom PURPLE_SHROOM = new BlockFBMushroom("Purple_Mushroom", 0.1F,
			() -> new WorldGenBigFBMushroom(EnumMushroomVariant.PURPLE), () -> new WorldGenHugeFBMushroom(EnumMushroomVariant.PURPLE));
	public static BlockFBMushroom GREEN_SHROOM = new BlockFBMushroom("Green_Mushroom", 0.1f,
			() -> new WorldGenBigFBMushroom(EnumMushroomVariant.GREEN), () -> new WorldGenHugeFBMushroom(EnumMushroomVariant.GREEN));
	public static BlockFBMushroom BLUE_GLOWSHROOM = new BlockFBMushroom("Blue_Glowshroom", 0.7F,
			() -> new WorldGenBigGlowshroom(EnumGlowshroomVariant.BLUE), () -> new WorldGenHugeGlowshroom(EnumGlowshroomVariant.BLUE));
	public static BlockFBMushroom GREEN_GLOWSHROOM = new BlockFBMushroom("Green_Glowshroom", 0.7F,
			() -> new WorldGenBigGlowshroom(EnumGlowshroomVariant.GREEN),  () -> new WorldGenHugeGlowshroom(EnumGlowshroomVariant.GREEN));
	public static BlockFBMushroom YELLOW_GLOWSHROOM = new BlockFBMushroom("Yellow_Glowshroom", 0.7F,
			() -> new WorldGenBigGlowshroom(EnumGlowshroomVariant.YELLOW),  () -> new WorldGenHugeGlowshroom(EnumGlowshroomVariant.YELLOW));
	public static BlockFBMushroom PINK_GLOWSHROOM = new BlockFBMushroom("Pink_Glowshroom", 0.7F,
			() -> new WorldGenBigGlowshroom(EnumGlowshroomVariant.PINK),  () -> new WorldGenHugeGlowshroom(EnumGlowshroomVariant.PINK));
	public static BlockFBMushroom PURPLE_GLOWSHROOM = new BlockFBMushroom("Purple_Glowshroom", 0.7F,
			() -> new WorldGenBigGlowshroom(EnumGlowshroomVariant.PURPLE),  () -> new WorldGenHugeGlowshroom(EnumGlowshroomVariant.PURPLE));
	public static Block BIG_SHROOM = new BlockBigMushroom();
	public static Block BIG_GLOWSHROOM = new BlockBigGlowshroom();
	public static Block SHELF_MUSHROOM = new BlockShelfMushroom();
	public static Block LICHEN = new BlockLichen();
	public static Block BRAMBLES = new BlockBrambleBush();
	public static Block ROOTS = new BlockRoots();
	public static Block WEB_COVER = new BlockWebCover();
	//public static Block FAE_WATER;
	//public static Block FAE_WATER_FLOWING;
	
	//Trees
	public static WoodBlock WOOD = new WoodBlock<>(Constants.MODID, FantasyBiomes.TAB, EnumWoodType.class, true);
	public static BlockBaseSapling VANILLA_SAPLING = BlockBaseSapling.create("simple_sapling", Constants.MODID, FantasyBiomes.TAB, EnumVanillaWoodType.class, 0);
	public static BlockBaseLeaves VANILLA_LEAVES = BlockBaseLeaves.create("simple_leaves", Constants.MODID, FantasyBiomes.TAB, null, EnumVanillaWoodType.class, 0);
	public static BlockTwistedGnarlwillow TWISTED_GNARLWILLOW = new BlockTwistedGnarlwillow();
	public static BlockGnarledVines GNARLED_VINES = new BlockGnarledVines();
	//Ores
	//public static Block MYTHRIL_ORE;
	
	public final static BlockFBMushroom[] shrooms = {TOADSTOOL, PURPLE_SHROOM, GREEN_SHROOM};
	public final static BlockFBMushroom[] glowshrooms = {BLUE_GLOWSHROOM, GREEN_GLOWSHROOM, YELLOW_GLOWSHROOM, PINK_GLOWSHROOM, PURPLE_GLOWSHROOM};
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		for (Field field : FBiomesBlocks.class.getDeclaredFields()) {
			try {
				Object object = field.get(null);
				if (object instanceof Map) {
					for (Object o : ((Map)object).values()) if (o instanceof Block) register(registry, (Block) o);
					continue;
				}
				if (object instanceof ShapedBlock) {
					((ShapedBlock) object).registerBlocks(registry);
					continue;
				}
				if (object instanceof WoodBlock) {
					((WoodBlock) object).registerBlocks(registry);
					continue;
				}
				if (!(object instanceof Block) || object == null) continue;
				register(registry, (Block) object);
			} catch (Exception e) {}
		}
	}
	
	private static <T extends Block> void register(IForgeRegistry<Block> registry, T block) {
		registry.register(block);
		BLOCKS.add(block);
	}
	
}
