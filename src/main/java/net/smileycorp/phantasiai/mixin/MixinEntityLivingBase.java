package net.smileycorp.phantasiai.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.smileycorp.phantasiai.common.entities.ICreatureType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase implements ICreatureType {
    
    private EnumCreatureType type;
    
    public void setCreatureType(EnumCreatureType type) {
        this.type = type;
    }
    
    public EnumCreatureType getCreatureType() {
        return type;
    }
    
}
