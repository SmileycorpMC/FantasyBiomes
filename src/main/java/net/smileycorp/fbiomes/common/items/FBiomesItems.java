package net.smileycorp.fbiomes.common.items;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid=Constants.MODID)
public class FBiomesItems {
	
	public static List<Item> ITEMS = Lists.newArrayList();
	
	public static Item BERRIES = new ItemBerries();
	//public static Item MITHRIL_INGOT = new ItemMithril("ingot");
	//public static Item MITHRIL_NUGGET = new ItemMithril("nugget");
	//public static Item MITHRIL_DUST = new ItemMithril("dust");
	public static Item PIXIE_BOTTLE = new ItemPixieBottle();
	public static Item PIXIE_HOUSING = new ItemPixieHousing();
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		for (final Block block : FBiomesBlocks.BLOCKS) {
			if (((BlockProperties)block).usesCustomItemHandler()) continue;
			register(registry, new ItemBlockFBiomes(block));
		}
		FBiomesBlocks.WOOD.registerItems(registry);
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
