package net.smileycorp.fbiomes.common.blocks.enums;

import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.atlas.api.block.wood.WoodEnum;
import net.smileycorp.fbiomes.common.world.gen.tree.WorldGenElderwoodTree;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

public enum EnumWoodType implements WoodEnum {
    ELDERWOOD ("elderwood", new WorldGenElderwoodTree(true, false), null,
            MapColor.SILVER_STAINED_HARDENED_CLAY, MapColor.CYAN_STAINED_HARDENED_CLAY, MapColor.GREEN_STAINED_HARDENED_CLAY);
    
    private final String name;
    private final WorldGenerator tree, largeTree;
    private final MapColor plankColour, logColour, leavesColour;
    
    EnumWoodType(String name, WorldGenerator tree, WorldGenerator largeTree, MapColor plankColour, MapColor logColour, MapColor leavesColour) {
        this.name = name;
        this.tree = tree;
        this.largeTree = largeTree;
        this.plankColour = plankColour;
        this.logColour = logColour;
        this.leavesColour = leavesColour;
    }
    
    @Nullable
    @Override
    public WorldGenerator getTree() {
        return tree;
    }
    
    @Nullable
    @Override
    public WorldGenerator getLargeTree() {
        return largeTree;
    }
    
    @Override
    public MapColor plankColour() {
        return plankColour;
    }
    
    @Override
    public MapColor logColour() {
        return logColour;
    }
    
    @Override
    public MapColor leavesColour() {
        return leavesColour;
    }
    
    @Override
    public float saplingDropChance() {
        return 0.05f;
    }
    
    @Override
    public Map<Float, ItemStack> getLeafDrops() {
        return Collections.emptyMap();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
}
