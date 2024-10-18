package net.smileycorp.fbiomes.common.items;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

@ObjectHolder("fbiomes")
public class FBiomesItems {
	
	public static Item BERRIES = new ItemBerries();
	public static Item MITHRIL_INGOT = new ItemMithril("Ingot");
	public static Item MITHRIL_NUGGET = new ItemMithril("Nugget");
	public static Item MITHRIL_DUST = new ItemMithril("Dust");
	
	
	@Mod.EventBusSubscriber(modid="fbiomes")
	public static class ItemRegistry {
		
		private static IForgeRegistry<Item> registry;
		
		public static final Set<Item> ITEMS = new HashSet<>();
		public static Item[] items = {BERRIES};
		public static final Item[] dustItems = {};
		
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			
			registry = event.getRegistry();
			
			for (final Item item : items) {
				register(item, registry);
			}
			
			for (final Block block : FBiomesBlocks.blocks) {
				if (!(((IBlockProperties)block).usesCustomItemHandler())){
					ItemBlock item = new ItemBlock(block);
					item.setRegistryName(block.getRegistryName());
					item.setUnlocalizedName(block.getUnlocalizedName());
					register(item, registry);
				}
			}
			register(new ItemBlockBigMushroom(), registry);
			register(new ItemBlockBigGlowshroom(), registry);
			FBiomesBlocks.WOOD.registerItems(registry);
		}
		
		public static void metaBlockRegistry(Block block, IForgeRegistry<Item> registry) {
			
		}
		
		public static void register(Item item, IForgeRegistry<Item> registry) {
			registry.register(item);
			ITEMS.add(item);
		}
		
		public static void registerLateItems () {
			if (OreDictionary.doesOreNameExist("dustIron")) {
				for (final Item item : dustItems) {
					registry.register(item);
					ITEMS.add(item);
				}
			}
		}
	}
}
