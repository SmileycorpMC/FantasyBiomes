package net.smileycorp.fbiomes.common.items;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid="fbiomes")
public class FBiomesItems {
	
	public static List<Item> ITEMS = Lists.newArrayList();
	
	public static Item BERRIES = new ItemBerries();
	public static Item MITHRIL_INGOT = new ItemMithril("Ingot");
	public static Item MITHRIL_NUGGET = new ItemMithril("Nugget");
	public static Item MITHRIL_DUST = new ItemMithril("Dust");
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		for (final Block block : FBiomesBlocks.BLOCKS) {
			if (((BlockProperties)block).usesCustomItemHandler()) continue;
			ItemBlock item = new ItemBlock(block);
			item.setRegistryName(block.getRegistryName());
			item.setUnlocalizedName(block.getUnlocalizedName());
			register(registry, item);
		}
		register(registry, new ItemBlockBigMushroom());
		register(registry, new ItemBlockBigGlowshroom());
		for (Field field : FBiomesItems.class.getDeclaredFields()) {
			try {
				Object object = field.get(null);
				if (!(object instanceof Item) || object == null) continue;
				register(registry, (Item) object);
			} catch (Exception e) {}
		}
	}
	
	private static void register(IForgeRegistry<Item> registry, Item item) {
		registry.register(item);
		ITEMS.add(item);
	}
	
}
