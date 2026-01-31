package net.smileycorp.phantasiai.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.atlas.api.block.wood.WoodVariant;
import net.smileycorp.phantasiai.common.blocks.enums.EnumVanillaWoodType;

import javax.annotation.Nullable;

public class VanillaLeavesColours implements IBlockColor, IItemColor {
    
    public static final VanillaLeavesColours INSTANCE = new VanillaLeavesColours();
    
    @Override
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
        return state.getValue(((WoodVariant<EnumVanillaWoodType>)state.getBlock()).getVariantProperty()).getColour();
    }
    
    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return EnumVanillaWoodType.values()[stack.getMetadata() % EnumVanillaWoodType.values().length].getColour();
    }
    
}
