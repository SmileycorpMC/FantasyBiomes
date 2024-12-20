package net.smileycorp.fbiomes.common.blocks;

import java.util.Random;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.api.atlas.IBlockProperties;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.items.FBiomesItems;

public class BlockLooseRock extends BlockFalling implements IBlockProperties {
	
	public BlockLooseRock() { 
		super (Material.ROCK);
		setCreativeTab(FantasyBiomes.creativeTab);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setUnlocalizedName("BlockLooseRock");
		setRegistryName("FBiomes:loose_rock");
	}
	
	public ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(Blocks.STONE), 1);
    }
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
       return Item.getItemFromBlock(Blocks.COBBLESTONE);
	}
	
	@SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState state) {
        return 7631988;
    }
	
}
