package net.smileycorp.fbiomes.common.entities.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.fbiomes.common.entities.EntityPixie;

public class EntityAIPixieFollowOwner extends EntityAIBase {

    private final EntityPixie pixie;

    public EntityAIPixieFollowOwner(EntityPixie pixie) {
        this.pixie = pixie;
        setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        if (pixie.getMoveHelper().isUpdating() |! pixie.hasOwner()) return false;
        double dis = pixie.getDistanceSq(pixie.getOwner());
        return dis > 9 && dis <= 4094;
    }
    
    @Override
    public void startExecuting() {
        Entity owner = pixie.getOwner();
        Vec3d dir = DirectionUtils.getDirectionVec(owner, pixie);
        pixie.getMoveHelper().setMoveTo(owner.posX + dir.x * 3, owner.posY + dir.y * 3, owner.posZ + dir.z * 3, 2);
        super.startExecuting();
    }

}
