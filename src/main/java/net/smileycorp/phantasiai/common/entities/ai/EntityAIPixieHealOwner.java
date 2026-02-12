package net.smileycorp.phantasiai.common.entities.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.smileycorp.phantasiai.common.EnumParticle;
import net.smileycorp.phantasiai.common.entities.EntityPixie;

import java.util.Random;

public class EntityAIPixieHealOwner extends EntityAIBase {

    private final EntityPixie pixie;
    private int heals;

    public EntityAIPixieHealOwner(EntityPixie pixie) {
        this.pixie = pixie;
    }

    @Override
    public boolean shouldExecute() {
        if (!pixie.hasOwner() || pixie.getSpellCooldown() > 0) return false;
        EntityLivingBase owner = pixie.getOwner();
        if (owner.getHealth() >= owner.getMaxHealth()) return false;
        double dis = pixie.getDistanceSq(pixie.getOwner());
        return dis <= 25;
    }

    @Override
    public void resetTask() {
        heals = 0;
        pixie.setSpellCooldown(20);
        super.resetTask();
    }

    @Override
    public void updateTask() {
        EntityLivingBase owner = pixie.getOwner();
        Random rand = pixie.getRNG();
        owner.heal(2f);
        heals ++;
        pixie.playSound(SoundEvents.BLOCK_NOTE_HARP, 0.75f, 0.5f + heals * 0.02f);
        for (int i = 3; i < 5 + rand.nextInt(3); i++)
            EnumParticle.TWINKLE.send(owner.dimension, owner.posX + (rand.nextDouble() - 0.5) * (double)owner.width,
                    owner.posY + rand.nextDouble() * (double)owner.height, owner.posZ + (rand.nextDouble() - 0.5) * (double)owner.width,
                    (double) pixie.getVariant().getColour(), 50d, 0d, 0.05, 0d);
        super.updateTask();
    }

}
