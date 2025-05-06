package net.smileycorp.fbiomes.client;

import com.google.common.collect.Maps;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.blocks.IMultifaceBlock;

import java.util.Map;

public class MultifaceStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
        map.remove(IMultifaceBlock.META);
        return new ModelResourceLocation(Constants.loc(((IMultifaceBlock)state.getBlock()).getName()), getPropertyString(map));
    }
    
}
