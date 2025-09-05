package net.smileycorp.fbiomes.common.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.fbiomes.common.EnumParticle;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;

import java.awt.*;
import java.util.Random;

public class PotionBioLuminescence extends PotionFBiomes {
    
    public PotionBioLuminescence() {
        super(false, 0x47E1D3, "bioluminescence");
    }
    
    @Override
    public String getName() {
        return super.getName();
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        Random rand = entity.getRNG();
        if (entity.isInvisible()) return;
        if (rand.nextBoolean()) return;
        EnumParticle.PIXEL_FULLBRIGHT.send(entity.dimension, entity.posX + (rand.nextDouble() - 0.5) * (double)entity.width,
                entity.posY + rand.nextDouble() * (double)entity.height, entity.posZ + (rand.nextDouble() - 0.5) * (double)entity.width,
                (double) EnumGlowshroomVariant.get(amplifier).getColour(), 50d, 0d, -0.05, 0d);
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void renderEffect(PotionEffect effect, int x, int y, float alpha) {
        GlStateManager.pushMatrix();
        Color colour = new Color(EnumGlowshroomVariant.get(effect.getAmplifier()).getColour());
        GlStateManager.color(colour.getRed(), colour.getBlue(), colour.getGreen(), alpha);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Minecraft.getMinecraft().renderEngine.bindTexture(getTexture(effect));
        Gui.drawScaledCustomSizeModalRect(x, y, 0, 0 , 18, 18, 18, 18, 18, 18);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
        super.renderInventoryEffect(effect, gui, x, y, z);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawStringWithShadow(I18n.format(getName()), (float)(x + 10 + 18), (float)(y + 6),
                EnumGlowshroomVariant.get(effect.getAmplifier()).getColour());
        String s = Potion.getPotionDurationString(effect, 1);
        fontRenderer.drawStringWithShadow(s, (float)(x + 10 + 18), (float)(y + 6 + 10), 8355711);
    }
    
}
