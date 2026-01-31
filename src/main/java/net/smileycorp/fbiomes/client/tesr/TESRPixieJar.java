package net.smileycorp.fbiomes.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.smileycorp.fbiomes.client.entity.RenderPixie;
import net.smileycorp.fbiomes.common.blocks.tiles.TilePixieJar;
import net.smileycorp.fbiomes.common.entities.PixieData;
import net.smileycorp.fbiomes.common.items.ItemPixieBottle;

public class TESRPixieJar extends TileEntitySpecialRenderer<TilePixieJar> {

    @Override
    public void render(TilePixieJar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        PixieData data = te.getPixie();
        RenderPixie.renderOrb(data.getVariant(), x, y + 0.5, z, 0.625f * data.getSize());
    }

    public static class ItemRenderer extends TileEntityItemStackRenderer {

        public void renderByItem(ItemStack stack, float partialTicks) {
            Minecraft mc = Minecraft.getMinecraft();
            PixieData data = ItemPixieBottle.getPixie(stack);
            RenderPixie.renderOrb(data.getVariant(), 0, 0.5, 0, 0.625f * data.getSize());
        }

    }

}
