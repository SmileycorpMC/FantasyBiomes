package net.smileycorp.fbiomes.common.blocks.enums;

import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.atlas.api.block.wood.WoodEnum;
import net.smileycorp.fbiomes.common.world.gen.tree.WorldGenElderwoodTree;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public enum EnumWoodType implements WoodEnum {
    ELDERWOOD ("elderwood", rand -> new WorldGenElderwoodTree(true, false), null,
            MapColor.SILVER_STAINED_HARDENED_CLAY, MapColor.CYAN_STAINED_HARDENED_CLAY, MapColor.ORANGE_STAINED_HARDENED_CLAY, 0.02f)/*,
    GNARLWILLOW("gnarlwillow", null, null,
            MapColor.BLACK_STAINED_HARDENED_CLAY, MapColor.BLACK_STAINED_HARDENED_CLAY, MapColor.BROWN_STAINED_HARDENED_CLAY, 0.05f),
    PINE("pine", null, null,
            MapColor.WOOD, MapColor.SAND, MapColor.GRASS, 0.02f)*/;
    
    private final String name;
    private final Function<Random, WorldGenerator> tree, largeTree;
    private final MapColor plankColour, logColour, leavesColour;
    private final float saplingDropChance;
    
    EnumWoodType(String name, Function<Random, WorldGenerator> tree, Function<Random, WorldGenerator> largeTree, MapColor plankColour, MapColor logColour, MapColor leavesColour, float saplingDropChance) {
        this.name = name;
        this.tree = tree;
        this.largeTree = largeTree;
        this.plankColour = plankColour;
        this.logColour = logColour;
        this.leavesColour = leavesColour;
        this.saplingDropChance = saplingDropChance;
    }
    
    @Nullable
    @Override
    public Function<Random, WorldGenerator> getTree() {
        return tree;
    }
    
    @Nullable
    @Override
    public Function<Random, WorldGenerator> getLargeTree() {
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
        return saplingDropChance;
    }
    
    @Override
    public Map<ItemStack, Float> getLeafDrops() {
        return Collections.emptyMap();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean solidLeaves() {
        return this == ELDERWOOD;
    }
    
}
