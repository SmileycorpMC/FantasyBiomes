package net.smileycorp.fbiomes.common.entities;

import net.minecraft.entity.EnumCreatureType;

public interface ICreatureType {
    
    void setCreatureType(EnumCreatureType type);

    EnumCreatureType getCreatureType();
    
}
