package net.smileycorp.fbiomes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.atlas.api.block.wood.WoodVariant;
import net.smileycorp.atlas.api.client.WoodStateMapper;
import net.smileycorp.atlas.api.client.colour.BlockGrassColour;
import net.smileycorp.atlas.api.client.colour.ItemFoliageColour;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.fbiomes.client.entity.RenderPixie;
import net.smileycorp.fbiomes.client.particle.ParticlePixel;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.blocks.BlockFBMushroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.entities.EntityPixie;
import net.smileycorp.fbiomes.common.items.FBiomesItems;

@Mod.EventBusSubscriber(modid= Constants.MODID, value = Side.CLIENT)
public class ClientProxy {
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		DefaultStateMapper mapper = new DefaultStateMapper();
		ModelLoader.setCustomStateMapper(FBiomesBlocks.VANILLA_LEAVES, new WoodStateMapper(FBiomesBlocks.VANILLA_LEAVES));
		ModelLoader.setCustomStateMapper(FBiomesBlocks.VANILLA_SAPLING, new WoodStateMapper(FBiomesBlocks.VANILLA_SAPLING));
		for (Item item : FBiomesItems.ITEMS) {
			if (item instanceof IMetaItem) {
				for (int i = 0; i < ((IMetaItem) item).getMaxMeta(); i++)
					ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(Constants.locStr(((IMetaItem) item).byMeta(i))));
				continue;
			}
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
		}
		for (Block block : FBiomesBlocks.BLOCKS) for (int meta = 0; meta < ((BlockProperties) block).getMaxMeta(); meta++) {
			String state = mapper.getPropertyString(block.getStateFromMeta(meta).getProperties());
			if (block instanceof BlockFBMushroom) {
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
				continue;
			}
			if (block instanceof WoodVariant) {
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta,
						new ModelResourceLocation(Constants.loc(((WoodVariant<?>) block).byMeta(meta)).toString(),"inventory"));
				continue;
			}
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), state));
		}
		FBiomesBlocks.WOOD.registerModels();
		RenderingRegistry.registerEntityRenderingHandler(EntityPixie.class, RenderPixie::new);
	}
	
	@SubscribeEvent
	public static void itemColour(ColorHandlerEvent.Item event) {
		ItemColors colours = event.getItemColors();
		colours.registerItemColorHandler(VanillaLeavesColours.INSTANCE, FBiomesBlocks.VANILLA_LEAVES);
		colours.registerItemColorHandler(new ItemFoliageColour(), FBiomesBlocks.GRASSY_MUD);
	}
	
	@SubscribeEvent
	public static void blockColour(ColorHandlerEvent.Block event) {
		BlockColors colours = event.getBlockColors();
		colours.registerBlockColorHandler(VanillaLeavesColours.INSTANCE, FBiomesBlocks.VANILLA_LEAVES);
		colours.registerBlockColorHandler(new BlockGrassColour(), FBiomesBlocks.GRASSY_MUD);
	}
	
	@SubscribeEvent
	public static void mapTextures(TextureStitchEvent event) {
		TextureMap map = event.getMap();
		ParticlePixel.SPRITE = map.registerSprite(Constants.loc("particle/pixel"));
	}
	
}
