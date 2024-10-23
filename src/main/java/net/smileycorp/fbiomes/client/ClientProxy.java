package net.smileycorp.fbiomes.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.blocks.BlockFBMushroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.items.FBiomesItems;

@Mod.EventBusSubscriber(modid= Constants.MODID, value = Side.CLIENT)
public class ClientProxy {
	
	public static IBlockState stateCache;
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		DefaultStateMapper mapper = new DefaultStateMapper();
		for (Item item : FBiomesItems.ITEMS)
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
		for (Block block : FBiomesBlocks.BLOCKS) for (int meta = 0; meta <= ((BlockProperties) block).getMaxMeta(); meta++) {
			String state = mapper.getPropertyString(block.getStateFromMeta(meta).getProperties());
			if (block instanceof BlockFBMushroom) {
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
				continue;
			}
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), state));
		}
		FBiomesBlocks.WOOD.registerModels();
	}
	
}
