package net.smileycorp.fbiomes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;
import net.smileycorp.fbiomes.common.potion.FBiomesPotions;

import java.awt.*;

public class ClientEventListener {

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        World world = Minecraft.getMinecraft().world;
        if (world == null) return;
        BioluminescenceTracker tracker = BioluminescenceTracker.instance;
        if (!tracker.isRunning()) tracker.tick(world);
    }

    @SubscribeEvent
    public void entityTick(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!entity.isPotionActive(FBiomesPotions.BIOLUMINESCENCE)) return;
        entity.getEntityWorld().checkLightFor(EnumSkyBlock.BLOCK, entity.getPosition());
    }

    @SubscribeEvent
    public void renderEntity(RenderLivingEvent<EntityLivingBase> event) {
        EntityLivingBase entity = event.getEntity();
        if (!entity.isPotionActive(FBiomesPotions.BIOLUMINESCENCE)) return;
        int c = EnumGlowshroomVariant.get(entity.getActivePotionEffect(FBiomesPotions.BIOLUMINESCENCE).getAmplifier()).getColour();
        float[] hsv = Color.RGBtoHSB((c >> 16) & 0xFF, (c >> 8) & 0xFF, c & 0xFF, new float[3]);
        Color color = new Color(Color.HSBtoRGB(hsv[0], 0.9f, 1) & 0xFFFFFF);
        GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), 1);
    }
    
}
