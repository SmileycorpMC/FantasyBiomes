package net.smileycorp.fbiomes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.atlas.api.client.WoodStateMapper;
import net.smileycorp.atlas.api.client.colour.BlockGrassColour;
import net.smileycorp.atlas.api.client.colour.ItemFoliageColour;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.fbiomes.client.entity.RenderPixie;
import net.smileycorp.fbiomes.client.particle.ParticlePixel;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.entities.EntityPixie;
import net.smileycorp.fbiomes.common.items.FBiomesItems;

@Mod.EventBusSubscriber(modid= Constants.MODID, value = Side.CLIENT)
public class ClientProxy {

	public ClientProxy() {
		MinecraftForge.EVENT_BUS.register(new ClientEventListener());
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomStateMapper(FBiomesBlocks.VANILLA_LEAVES, new WoodStateMapper(FBiomesBlocks.VANILLA_LEAVES));
		ModelLoader.setCustomStateMapper(FBiomesBlocks.VANILLA_SAPLING, new WoodStateMapper(FBiomesBlocks.VANILLA_SAPLING));
		for (Block block : FBiomesBlocks.LICHEN) ModelLoader.setCustomStateMapper(block, new MultifaceStateMapper());
		for (Block block : FBiomesBlocks.WEB_COVER) ModelLoader.setCustomStateMapper(block, new MultifaceStateMapper());
		for (Item item : FBiomesItems.ITEMS) if (item instanceof IMetaItem &! (item instanceof ItemBlock &&
				((BlockProperties)((ItemBlock) item).getBlock()).usesCustomItemHandler())) {
			if (((IMetaItem) item).getMaxMeta() > 1) for (int i = 0; i < ((IMetaItem) item).getMaxMeta(); i++)
				ModelLoader.setCustomModelResourceLocation(item, i,
						new ModelResourceLocation(Constants.loc(((IMetaItem) item).byMeta(i)),
						item instanceof ItemBlock ? "inventory" : "normal"));
			else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(),
						item instanceof ItemBlock ? "inventory" : "normal"));
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
