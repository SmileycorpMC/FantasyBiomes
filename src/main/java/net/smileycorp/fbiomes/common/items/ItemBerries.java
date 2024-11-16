package net.smileycorp.fbiomes.common.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFood;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

public class ItemBerries extends ItemFood {

	public ItemBerries() {
		super(1, 0.5f, false);
		setCreativeTab(FantasyBiomes.TAB);
		setUnlocalizedName(Constants.name("Berries"));
		setRegistryName(Constants.loc("Berries"));
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		IBlockState state = world.getBlockState(pos);
		if (!state.getBlock().canSustainPlant(state, world, pos.down(), EnumFacing.UP, (IPlantable)Blocks.WHEAT)) return EnumActionResult.PASS;
		world.setBlockState(pos, FBiomesBlocks.BRAMBLES.getDefaultState(), 8);
		player.getHeldItem(hand).shrink(1);
		return EnumActionResult.SUCCESS;
	}
	
}
