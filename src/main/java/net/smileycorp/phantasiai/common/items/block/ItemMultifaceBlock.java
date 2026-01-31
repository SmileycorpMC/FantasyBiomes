package net.smileycorp.phantasiai.common.items.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.phantasiai.common.blocks.IMultifaceBlock;
import net.smileycorp.phantasiai.common.items.ItemFBiomes;
import org.apache.commons.lang3.ArrayUtils;

public class ItemMultifaceBlock<T extends Block & IMultifaceBlock<T>> extends ItemFBiomes {
    
    private final T block;
    
    public ItemMultifaceBlock(T block) {
        super(block.getName());
        this.block = block;
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() |! player.canPlayerEdit(pos, side, stack) || stack.getItem() != this)  return EnumActionResult.PASS;
        pos = pos.offset(side);
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = side.getOpposite();
        if (block.multipartClass() == state.getBlock().getClass()) {
            if (IMultifaceBlock.hasFacing(state, facing)) return EnumActionResult.PASS;
            state = block.getBlockStateFromFacing(ArrayUtils.add(IMultifaceBlock.getFacings(state), facing));
        } else if (!world.mayPlace(block, pos, false, side, player)) return EnumActionResult.PASS;
        else state = block.getBlockStateFromFacing(facing);
        if (!world.setBlockState(pos, state, 11)) return EnumActionResult.PASS;
        SoundType sound = block.getSoundType(state, world, pos, player);
        world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1) / 2f, sound.getPitch() * 0.8f);
        stack.shrink(1);
        if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        return EnumActionResult.SUCCESS;
    }
    
}
