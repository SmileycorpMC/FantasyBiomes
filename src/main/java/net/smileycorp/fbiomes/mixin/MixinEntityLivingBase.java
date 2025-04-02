package net.smileycorp.fbiomes.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.smileycorp.fbiomes.common.entities.ICreatureType;
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
