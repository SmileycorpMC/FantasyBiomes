package net.smileycorp.phantasiai.common.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.Phantasiai;

import java.util.Random;

public class BlockLooseRock extends BlockFalling implements BlockProperties {
	
	public BlockLooseRock() { 
		super (Material.ROCK);
		setCreativeTab(Phantasiai.TAB);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setUnlocalizedName(Constants.name("loose_rock"));
		setRegistryName(Constants.loc("loose_rock"));
	}
	
	@Override
	public ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(Blocks.STONE), 1);
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
       return Item.getItemFromBlock(Blocks.COBBLESTONE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState state) {
        return 7631988;
    }
	
}
