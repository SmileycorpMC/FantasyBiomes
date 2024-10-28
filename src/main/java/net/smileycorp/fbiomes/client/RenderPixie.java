package net.smileycorp.fbiomes.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.entities.EntityPixie;

import javax.annotation.Nullable;

public class RenderPixie extends RenderLiving<EntityPixie> {
    
    public RenderPixie(RenderManager rm) {
        super(rm, new ModelPixie(rm), 0.4F);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPixie entity) {
        return Constants.loc("textures/entity/pixie/variant_" + entity.getVariant() + ".png");
    }
    
}
