package net.smileycorp.phantasiai.client.entity.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWitchTrader extends ModelBiped {

    private final ModelRenderer body_r1;
    private final ModelRenderer leftPauldron;
    private final ModelRenderer leftPauldron_r1;
    private final ModelRenderer rightPauldron;
    private final ModelRenderer rightPauldron_r1;
    private final ModelRenderer ClothingL1_r1;
    private final ModelRenderer leftKnee;
    private final ModelRenderer ClothingR1_r1;
    private final ModelRenderer rightKnee;

	public ModelWitchTrader() {
        textureWidth = 64;
        textureHeight = 64;

        bipedHead = new ModelRenderer(this);
        bipedHead.setRotationPoint(0.0F, -1.0F, 0.0F);
        bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 0, -4.0F, -10.0F, -4.0F, 8, 8, 8, 0.0F, false));
        bipedHead.cubeList.add(new ModelBox(bipedHead, 48, 54, -4.0F, -2.0F, 4.0F, 8, 10, 0, 0.0F, false));

        bipedBody = new ModelRenderer(this);
        bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedBody.cubeList.add(new ModelBox(bipedBody, 8, 16, -4.0F, -3.0F, -2.0F, 8, 13, 4, 0.0F, false));

        body_r1 = new ModelRenderer(this);
        body_r1.setRotationPoint(0.0F, 0.5F, -1.5F);
        bipedBody.addChild(body_r1);
        setRotationAngle(body_r1, -0.7854F, 0.0F, 0.0F);
        body_r1.cubeList.add(new ModelBox(body_r1, 9, 17, -4.0F, -1.5F, -1.5F, 8, 3, 3, 0.0F, false));

        bipedLeftArm = new ModelRenderer(this);
        bipedLeftArm.setRotationPoint(-5.0F, 0.0F, 0.0F);
        bipedLeftArm.cubeList.add(new ModelBox(bipedLeftArm, 36, 36, -2.0F, -3.0F, -2.0F, 3, 12, 3, 0.0F, false));

        leftPauldron = new ModelRenderer(this);
        leftPauldron.setRotationPoint(0.0F, 2.0F, 0.0F);
        bipedLeftArm.addChild(leftPauldron);


        leftPauldron_r1 = new ModelRenderer(this);
        leftPauldron_r1.setRotationPoint(-2.0F, -5.0F, 0.5F);
        leftPauldron.addChild(leftPauldron_r1);
        setRotationAngle(leftPauldron_r1, -3.1416F, 0.0F, -2.9671F);
        leftPauldron_r1.cubeList.add(new ModelBox(leftPauldron_r1, 0, 46, 0.0F, 0.0F, -2.0F, 0, 11, 5, 0.0F, true));

        bipedRightArm = new ModelRenderer(this);
        bipedRightArm.setRotationPoint(5.0F, 0.0F, 0.0F);
        bipedRightArm.cubeList.add(new ModelBox(bipedRightArm, 21, 36, -1.0F, -3.0F, -2.0F, 3, 12, 3, 0.0F, false));

        rightPauldron = new ModelRenderer(this);
        rightPauldron.setRotationPoint(0.0F, 2.0F, 0.0F);
        bipedRightArm.addChild(rightPauldron);


        rightPauldron_r1 = new ModelRenderer(this);
        rightPauldron_r1.setRotationPoint(2.0F, -5.0F, 0.5F);
        rightPauldron.addChild(rightPauldron_r1);
        setRotationAngle(rightPauldron_r1, -3.1416F, 0.0F, 2.9671F);
        rightPauldron_r1.cubeList.add(new ModelBox(rightPauldron_r1, 0, 46, 0.0F, 0.0F, -2.0F, 0, 11, 5, 0.0F, false));

        bipedLeftLeg = new ModelRenderer(this);
        bipedLeftLeg.setRotationPoint(-1.9F, 10.0F, 0.0F);
        bipedLeftLeg.cubeList.add(new ModelBox(bipedLeftLeg, 48, 28, -2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F, false));

        ClothingL1_r1 = new ModelRenderer(this);
        ClothingL1_r1.setRotationPoint(5.9F, 0.0F, -0.5F);
        bipedLeftLeg.addChild(ClothingL1_r1);
        setRotationAngle(ClothingL1_r1, 0.0F, 0.0F, -0.1745F);
        ClothingL1_r1.cubeList.add(new ModelBox(ClothingL1_r1, 0, 46, 0.0F, 0.0F, -2.0F, 0, 11, 5, 0.0F, false));

        leftKnee = new ModelRenderer(this);
        leftKnee.setRotationPoint(0.0F, 7.0F, 0.0F);
        bipedLeftLeg.addChild(leftKnee);
        leftKnee.cubeList.add(new ModelBox(leftKnee, 50, 11, -1.6F, 0.0F, -1.5F, 3, 7, 3, 0.0F, false));

        bipedRightLeg = new ModelRenderer(this);
        bipedRightLeg.setRotationPoint(1.9F, 10.0F, 0.0F);
        bipedRightLeg.cubeList.add(new ModelBox(bipedRightLeg, 44, 0, -2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F, false));

        ClothingR1_r1 = new ModelRenderer(this);
        ClothingR1_r1.setRotationPoint(-5.9F, 0.0F, -0.5F);
        bipedRightLeg.addChild(ClothingR1_r1);
        setRotationAngle(ClothingR1_r1, 0.0F, 0.0F, 0.1745F);
        ClothingR1_r1.cubeList.add(new ModelBox(ClothingR1_r1, 0, 46, 0.0F, 0.0F, -2.0F, 0, 11, 5, 0.0F, false));

        rightKnee = new ModelRenderer(this);
        rightKnee.setRotationPoint(0.0F, 7.0F, 0.0F);
        bipedRightLeg.addChild(rightKnee);
        rightKnee.cubeList.add(new ModelBox(rightKnee, 32, 0, -1.4F, 0.0F, -1.5F, 3, 7, 3, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bipedHead.render(f5);
        bipedBody.render(f5);
        bipedLeftArm.render(f5);
        bipedRightArm.render(f5);
        bipedLeftLeg.render(f5);
        bipedRightLeg.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

}
