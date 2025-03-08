package net.smileycorp.fbiomes.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPixie extends ModelBase {
    
    private final ModelRenderer l_wing;
    private final ModelRenderer r_wing;
    
    public ModelPixie() {
        textureWidth = 16;
        textureHeight = 16;
        l_wing = new ModelRenderer(this);
        l_wing.setRotationPoint(-1, 19, 0.0F);
        l_wing.cubeList.add(new ModelBox(l_wing, 0, 0, 0, 0, 0, 0, 6, 6, 0, true));
        l_wing.rotateAngleX = 0.6108652f;
        r_wing = new ModelRenderer(this);
        r_wing.setRotationPoint(1, 19, 0);
        r_wing.cubeList.add(new ModelBox(r_wing, 0, 0, 0, 0, 0, 0, 6, 6, 0, false));
        r_wing.rotateAngleX = 0.6108652f;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        float rotation = (float) (0.45f * (1 + Math.sin(ageInTicks * 0.6f)));
        l_wing.rotateAngleY = -rotation;
        r_wing.rotateAngleY = rotation;
    }
    
    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        l_wing.render(scale);
        r_wing.render(scale);
    }
    
}
