package net.smileycorp.fbiomes.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.fbiomes.client.entity.model.ModelPixie;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.entities.EntityPixie;

import javax.annotation.Nullable;
import java.awt.*;

public class RenderPixie extends RenderLiving<EntityPixie> {

    private static final ResourceLocation GREYSCALE_TEXTURE = Constants.loc("textures/entity/pixie/greyscale.png");

    public RenderPixie(RenderManager rm) {
        super(rm, new ModelPixie(), 0.4f);
    }
    
    @Override
    public void doRender(EntityPixie entity, double x, double y, double z, float yaw, float pt) {
        if (!renderOutlines) {
            EntityPixie.PixieVariant variant = entity.getVariant();
            if (entity.hasCustomName() && "jeb_".equals(entity.getCustomNameTag())) {
                variant = null;
                Color colour = Color.getHSBColor(((float)(entity.ticksExisted % 100) + pt) / 100f, 1, 1);
                GlStateManager.color(colour.getRed(), colour.getGreen(), colour.getBlue(), 1);
            }
            renderOrb(variant, x, y, z, (float) (0.4f + 0.05f * Math.sin(0.1 * (entity.ticksExisted + pt))) * entity.getSize());
        }
        shadowSize = 0.4f * entity.getSize();
        super.doRender(entity, x, y, z, yaw, pt);
    }

    @Override
    protected void preRenderCallback(EntityPixie entity, float pt) {
        super.preRenderCallback(entity, pt);
        float size = entity.getSize();
        GlStateManager.scale(size, size, size);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPixie entity) {
        return entity.getVariant().getTexture();
    }

    public static void renderOrb(EntityPixie.PixieVariant variant, double x, double y, double z, float scale) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        rm.renderEngine.bindTexture((variant == null) ? GREYSCALE_TEXTURE : variant.getTexture());
        RenderHelper.enableStandardItemLighting();
        final float u0 = 0;
        final float v0 = 0F;
        final float u1 = 0.375f;
        final float v1 = 0.375f;
        GlStateManager.translate(0, 0.1f, 0);
        GlStateManager.rotate(180.0F - rm.playerViewY, 0, 1, 0);
        GlStateManager.rotate((float)(rm.options.thirdPersonView == 2 ? -1 : 1) * -rm.playerViewX, 1, 0, 0);
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder buffer = tesselator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(-0.5, -0.25, 0.0).tex(u0, v1).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(0.5, -0.25, 0.0).tex(u1, v1).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(0.5, 0.75, 0.0).tex(u1, v0).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(-0.5, 0.75, 0.0).tex(u0, v0).normal(0.0F, 1.0F, 0.0F).endVertex();
        tesselator.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
        GlStateManager.disableRescaleNormal();
        GlStateManager.scale(1, 1, 1);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

}
