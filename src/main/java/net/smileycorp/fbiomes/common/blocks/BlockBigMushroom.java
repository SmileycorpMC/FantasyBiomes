package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.enums.EnumBigMushroomShape;
import net.smileycorp.fbiomes.common.blocks.enums.EnumBigMushroomVariant;

import java.util.Random;

public class BlockBigMushroom extends BlockBase {
	
	public static final PropertyEnum<EnumBigMushroomShape> SHAPE = PropertyEnum.create("shape", EnumBigMushroomShape.class);
	public static final PropertyEnum<EnumBigMushroomVariant> VARIANT = PropertyEnum.create("variant", EnumBigMushroomVariant.class);
	
	public BlockBigMushroom() {
		this("Big_Mushroom");
		setDefaultState(blockState.getBaseState().withProperty(SHAPE, EnumBigMushroomShape.CAP).withProperty(VARIANT, EnumBigMushroomVariant.GREEN));
	}
	
	public BlockBigMushroom(String name) {
		super(name, Constants.MODID, Material.WOOD, SoundType.WOOD, 0.2F, 0, "axe", 1, null);
		isFlamable = false;
		setCreativeTab(FantasyBiomes.TAB);
	}
	
	@Override
	public int quantityDropped(Random rand) {
        return Math.max(0, rand.nextInt(10) - 7);
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(VARIANT).getDrop();
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SHAPE, VARIANT);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(placer.getHeldItem(hand).getMetadata());
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= this.getMaxMeta()) return this.getDefaultState();
		EnumBigMushroomShape shape = EnumBigMushroomShape.values()[meta % 3];
		EnumBigMushroomVariant variant = EnumBigMushroomVariant.values()[(meta-(meta % 3)) / 3];
		return this.getDefaultState().withProperty(VARIANT, variant).withProperty(SHAPE, shape);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int variant = state.getValue(VARIANT).getMeta();
		int shape = state.getValue(SHAPE).getMeta();
		return  variant * 3 + shape;
    }
	
	@Override
	public void getSubBlocks(CreativeTabs item, NonNullList<ItemStack> items) {
        for (int i = 0; i<this.getMaxMeta(); i++) {
        	items.add(new ItemStack(this, 1, i));
        }
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, getMetaFromState(state));
    }
	
	@Override
	public int getMaxMeta(){
		return 6;
	}
	
}
