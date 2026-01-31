package net.smileycorp.fbiomes.client.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.fbiomes.client.entity.model.ModelWitchTrader;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.entities.EntityWitchTrader;

import javax.annotation.Nullable;

public class RenderWitchTrader extends RenderLiving<EntityWitchTrader> {

    private static final ResourceLocation TEXTURE = Constants.loc("textures/entity/witch_trader.png");

    public RenderWitchTrader(RenderManager rm) {
        super(rm, new ModelWitchTrader(), 0.5f);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityWitchTrader entity) {
        return TEXTURE;
    }

}
