package net.smileycorp.fbiomes.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

public class ItemBerries extends ItemFood implements IMetaItem {

	public ItemBerries() {
		super(1, 0.5f, false);
		setCreativeTab(FantasyBiomes.TAB);
		setUnlocalizedName(Constants.name("Berries"));
		setRegistryName(Constants.loc("Berries"));
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		IBlockState state = world.getBlockState(pos);
		ItemStack stack = player.getHeldItem(hand);
		BlockPos up = pos.up();
		if (!world.isAirBlock(up) |!state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, (IPlantable)Blocks.WHEAT) |!
				player.canPlayerEdit(pos.offset(side), side, stack)) return EnumActionResult.PASS;
		world.setBlockState(up, FBiomesBlocks.BRAMBLES.getDefaultState());
		if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos.up(), stack);
		if (!player.isCreative()) stack.shrink(1);
		world.playSound(null, up, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS,
				(SoundType.PLANT.getVolume() + 1) / 2f, SoundType.PLANT.getPitch() * 0.8f);
		return EnumActionResult.SUCCESS;
	}
	
}
