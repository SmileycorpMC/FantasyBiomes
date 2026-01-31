package net.smileycorp.phantasiai.common.items;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.blocks.BlockLichen;
import net.smileycorp.phantasiai.common.blocks.BlockWebCover;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;
import net.smileycorp.phantasiai.common.blocks.IMultifaceBlock;
import net.smileycorp.phantasiai.common.items.block.ItemMultifaceBlock;
import net.smileycorp.phantasiai.common.items.block.ItemPixieJar;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid=Constants.MODID)
public class PhantasiaiItems {
	
	public static List<Item> ITEMS = Lists.newArrayList();

	public static ItemMultifaceBlock<BlockLichen> LICHEN = new ItemMultifaceBlock<>(PhantasiaiBlocks.LICHEN.get(0));
	public static ItemMultifaceBlock<BlockWebCover> WEB_COVER = new ItemMultifaceBlock<>(PhantasiaiBlocks.WEB_COVER.get(0));

	public static Item BERRIES = new ItemBerries();
	//public static Item MITHRIL_INGOT = new ItemMithril("ingot");
	//public static Item MITHRIL_NUGGET = new ItemMithril("nugget");
	//public static Item MITHRIL_DUST = new ItemMithril("dust");
	public static Item PIXIE_HOUSING = new ItemPixieHousing();

	public static Item PIXIE_BOTTLE = new ItemPixieBottle();
	public static ItemPixieJar PIXIE_JAR = new ItemPixieJar();
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		for (final Block block : PhantasiaiBlocks.BLOCKS) {
			if (block instanceof IMultifaceBlock) continue;
			if (((BlockProperties)block).usesCustomItemHandler()) {
				continue;
			}
			register(registry, new ItemBlockFBiomes(block));
		}
		PhantasiaiBlocks.WOOD.registerItems(registry);
		for (Field field : PhantasiaiItems.class.getDeclaredFields()) {
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
