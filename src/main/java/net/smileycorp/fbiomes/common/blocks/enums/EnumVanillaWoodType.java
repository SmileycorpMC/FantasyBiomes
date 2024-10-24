package net.smileycorp.fbiomes.common.blocks.enums;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.material.MapColor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.atlas.api.block.wood.WoodEnum;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.world.gen.tree.WorldGenBigRedOakTree;
import net.smileycorp.fbiomes.common.world.gen.tree.WorldGenGoldenBirchTree;
import net.smileycorp.fbiomes.common.world.gen.tree.WorldGenRedOakTree;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public enum EnumVanillaWoodType implements WoodEnum {
    RED_OAK("red_oak", rand -> rand.nextInt(5) == 0 ? new WorldGenBigRedOakTree(true, false) : new WorldGenRedOakTree(true, false),
            null, MapColor.RED_STAINED_HARDENED_CLAY, 0xFF9E1A06),
    GOLD_BIRCH("gold_birch", rand -> new WorldGenGoldenBirchTree(true, false, false), null, MapColor.GOLD, 0xFFF7AD00);
    
    private final String name;
    private final Function<Random,WorldGenerator> tree, largeTree;
    private final MapColor mapColour;
    private final int colour;
    
    EnumVanillaWoodType(String name, Function<Random, WorldGenerator> tree, Function<Random, WorldGenerator> largeTree, MapColor mapColour, int colour) {
        this.name = name;
        this.tree = tree;
        this.largeTree = largeTree;
        this.mapColour = mapColour;
        this.colour = colour;
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
        return null;
    }
    
    @Override
    public MapColor logColour() {
        return null;
    }
    
    @Override
    public MapColor leavesColour() {
        return mapColour;
    }
    
    @Override
    public float saplingDropChance() {
        return 0.05f;
    }
    
    @Override
    public Map<ItemStack, Float> getLeafDrops() {
        return this == RED_OAK ? ImmutableMap.of(new ItemStack(Items.APPLE), 0.05f) : ImmutableMap.of();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public int getColour() {
        return colour;
    }
    
}
