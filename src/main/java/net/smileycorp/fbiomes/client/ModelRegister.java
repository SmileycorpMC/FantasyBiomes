package net.smileycorp.fbiomes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.items.FBiomesItems;

@Mod.EventBusSubscriber(modid="fbiomes")
public class ModelRegister {
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		DefaultStateMapper mapper = new DefaultStateMapper();
		for (Item item : FBiomesItems.ItemRegistry.items) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
		}
		for (Block block : FBiomesBlocks.blocks) {
			for (int meta = 0; meta <= ((IBlockProperties) block).getMaxMeta(); meta++) {
				String state = mapper.getPropertyString(block.getStateFromMeta(meta).getProperties());
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), state));
			}
		}
		registerModelShrooms();
		FBiomesBlocks.WOOD.registerModels();
	}
	
	public static void registerModelShrooms() {
		for(Block mushroom : FBiomesBlocks.shrooms) {
			Item item = Item.getItemFromBlock(mushroom);
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
		for(Block mushroom : FBiomesBlocks.glowshrooms) {
			Item item = Item.getItemFromBlock(mushroom);
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}
}
