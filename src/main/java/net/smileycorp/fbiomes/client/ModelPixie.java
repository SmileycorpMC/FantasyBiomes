package net.smileycorp.fbiomes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelPixie extends ModelBase {
    
    private final RenderManager rm;
    private final ModelRenderer body;
    private final ModelRenderer l_wing;
    private final ModelRenderer r_wing;
    
    public ModelPixie(RenderManager rm) {
        textureWidth = 16;
        textureHeight = 16;
        this.rm = rm;
        body = new ModelRenderer(this).addBox(-3, -6, 0, 6, 6, 0, false);
        l_wing = new ModelRenderer(this).addBox(-2, -7, 0, 0, 4, 6, false);
        r_wing = new ModelRenderer(this).addBox(2, -7, 0, 0, 4, 6, true);
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        float rotation = (float) (0.45f * (1 + Math.sin(ageInTicks)));
        l_wing.rotateAngleY = -rotation;
        r_wing.rotateAngleY = rotation;
    }
    
    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        scale = scale * 0.5f;
        l_wing.render(scale);
        r_wing.render(scale);
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(180.0F + interpolateRotation((EntityLivingBase) entity, Minecraft.getMinecraft().getRenderPartialTicks()), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F - rm.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(rm.options.thirdPersonView == 2 ? -1 : 1) * -rm.playerViewX, 1.0F, 0.0F, 0.0F);
        body.render(scale);
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }
    
    protected float interpolateRotation(EntityLivingBase entity, float partialTicks) {
        float f;
        for (f = entity.renderYawOffset - entity.prevRenderYawOffset; f < -180.0F; f += 360.0F);
        while (f >= 180.0F) f -= 360.0F;
        return entity.prevRenderYawOffset + partialTicks * f;
    }
    
}
