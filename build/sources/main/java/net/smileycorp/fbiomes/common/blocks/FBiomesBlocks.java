package net.smileycorp.fbiomes.common.blocks;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder("fbiomes")
public class FBiomesBlocks {
	//Surface Blocks
	public static Block MUD;
	
	//Decorations
	public static Block FLOWER;
	public static Block LOOSE_ROCK = new BlockLooseRock();
	public static Block ROOT;
	public static Block BRAMBLES = new BlockBrambleBush();
	public static Block FAE_WATER;
	public static Block FAE_WATER_FLOWING;
	
	//Trees
	public static Block WOOD;
	public static Block LEAVES;
	public static Block SAPLING;
	public static Block PLANKS;
	public static Block PLANK_SLAB;
	public static Block PLANK_STAIRS;
	public static Block PLANK_FENCE;
	public static Block PLANK_GATE;
	public static Block WOOD_DOOR;
	
	//Ores
	public static Block MYTHRIL_ORE;
	
	@Mod.EventBusSubscriber(modid="fbiomes")
	public static class BlockRegister {
		
		public final static Set<Block> BLOCKS = new HashSet<>();
		public final static Block[] blocks = {LOOSE_ROCK, BRAMBLES};
		
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			
			final IForgeRegistry<Block> registry = event.getRegistry();
			
			for (final Block block : blocks) {
				registry.register(block);
				BLOCKS.add(block);
			}
		}
	}
}
