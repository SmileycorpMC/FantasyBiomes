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
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.api.atlas.IBlockProperties;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

@ObjectHolder("fbiomes")
public class FBiomesItems {
	
	public static Item BERRIES = new ItemBerries();
	public static Item INGOT;
	
	@Mod.EventBusSubscriber(modid="fbiomes")
	public static class ItemRegister {
		
		public static final Set<Item> ITEMS = new HashSet<>();
		public static final Item[] items = {BERRIES};
		
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			
			final IForgeRegistry<Item> registry = event.getRegistry();
			
			for (final Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}
			
			for (final Block block : FBiomesBlocks.BlockRegister.blocks) {
				if (!((IBlockProperties)block).usesCustomItemHandler()) {
					Item item = new ItemBlock(block);
					item.setRegistryName(block.getRegistryName());
					item.setUnlocalizedName(block.getUnlocalizedName());
					registry.register(item);
					ITEMS.add(item);
				}
			}
		}
	}
}
