package net.smileycorp.fbiomes.common.blocks;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.wood.WoodBlock;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.ModDefinitions;

import org.apache.commons.lang3.ArrayUtils;

@EventBusSubscriber(modid="fbiomes")
public class FBiomesBlocks {
	//Surface Blocks
	public static Block MUD = new BlockMud("Mud");
	public static Block PEAT = new BlockMud("Peat");
	
	//Decorations
	public static Block FLOWER;
	
	public static BlockFBMushroom TOADSTOOL = new BlockFBMushroom("Toadstool", 0.1F);
	public static BlockFBMushroom PURPLE_SHROOM = new BlockFBMushroom("Mushroom_Purple", 0.1F);
	public static BlockFBMushroom GREEN_SHROOM = new BlockFBMushroom("Mushroom_Green", 0.1F);
	public static BlockFBMushroom BLUE_GLOWSHROOM = new BlockFBMushroom("Glowshroom_Blue", 0.7F);
	public static BlockFBMushroom GREEN_GLOWSHROOM = new BlockFBMushroom("Glowshroom_Green", 0.7F);
	public static BlockFBMushroom ORANGE_GLOWSHROOM = new BlockFBMushroom("Glowshroom_Orange", 0.7F);
	public static BlockFBMushroom PINK_GLOWSHROOM = new BlockFBMushroom("Glowshroom_Pink", 0.7F);
	public static BlockFBMushroom PURPLE_GLOWSHROOM = new BlockFBMushroom("Glowshroom_Purple", 0.7F);
	public static Block BIG_SHROOM = new BlockBigMushroom();
	public static Block BIG_GLOWSHROOM = new BlockBigGlowshroom();
	public static Block LOOSE_ROCK = new BlockLooseRock();
	public static Block ROOTS = new BlockRoots();
	public static Block BRAMBLES = new BlockBrambleBush();
	public static Block FAE_WATER;
	public static Block FAE_WATER_FLOWING;
	
	//Trees
	public static WoodBlock WOOD;
	
	//Ores
	public static Block MYTHRIL_ORE;
	
		
	public final static Set<Block> BLOCKS = new HashSet<>();
	public final static BlockFBMushroom[] shrooms = {TOADSTOOL, PURPLE_SHROOM, GREEN_SHROOM};
	public final static BlockFBMushroom[] glowshrooms = {BLUE_GLOWSHROOM, GREEN_GLOWSHROOM, ORANGE_GLOWSHROOM, PINK_GLOWSHROOM, PURPLE_GLOWSHROOM};
	
	public static Block[] blocks = {MUD, PEAT, LOOSE_ROCK, ROOTS, BRAMBLES, BIG_SHROOM, BIG_GLOWSHROOM};
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();
		
		blocks = ArrayUtils.addAll(blocks, shrooms);
		blocks = ArrayUtils.addAll(blocks, glowshrooms);
		
		for (final Block block : blocks) {
			registry.register(block);
			BLOCKS.add(block);
		}
		WOOD = new WoodBlock("", ModDefinitions.modid, FantasyBiomes.creativeTab, ModDefinitions.wood_types, true);
		WOOD.registerBlocks(registry);
	}
}
