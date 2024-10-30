package net.smileycorp.fbiomes.client;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.entities.EntityPixie;

import javax.annotation.Nullable;

public class RenderPixie extends RenderLiving<EntityPixie> {
    
    public RenderPixie(RenderManager rm) {
        super(rm, new ModelPixie(), 0.4F);
    }
    
    @Override
    public void doRender(EntityPixie entity, double x, double y, double z, float yaw, float pt) {
        if (!renderOutlines) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y, (float)z);
            bindEntityTexture(entity);
            RenderHelper.enableStandardItemLighting();
            final float u0 = 0;
            final float v0 = 0F;
            final float u1 = 0.375f;
            final float v1 = 0.375f;
            int brightness = entity.getBrightnessForRender();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness % 65536, brightness / 65536);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(0.0F, 0.1F, 0.0F);
            GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float)(renderManager.options.thirdPersonView == 2 ? -1 : 1) * -renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            float scale = (float) (0.4f + 0.05f * Math.sin(0.1 * (entity.ticksExisted + pt)));
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
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
            GlStateManager.popMatrix();
        }
        super.doRender(entity, x, y, z, yaw, pt);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPixie entity) {
        return Constants.loc("textures/entity/pixie/variant_" + entity.getVariant() + ".png");
    }
    
}
