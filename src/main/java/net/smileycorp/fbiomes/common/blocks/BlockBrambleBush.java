package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.items.FBiomesItems;

import java.util.Random;

public class BlockBrambleBush extends BlockBush implements IGrowable, BlockProperties {
	
	public static PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
	
	public BlockBrambleBush() {
		setCreativeTab(FantasyBiomes.TAB);
		setUnlocalizedName(Constants.name("Brambles"));
		setRegistryName(Constants.loc("Brambles"));
		setDefaultState(blockState.getBaseState().withProperty(AGE, 0));
		setSoundType(SoundType.PLANT);
		setTickRandomly(true);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (state.getValue(AGE) == 3){
            ItemStack stack = new ItemStack(FBiomesItems.BERRIES, world.rand.nextInt(2) + 1);
			if (! world.isRemote) {
                EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                item.motionX = world.rand.nextFloat();
                item.motionY = 0.2f;
                item.motionZ = world.rand.nextFloat();
                world.spawnEntity(item);
            }
			world.setBlockState(pos, this.getDefaultState(), 3);
			return true;
		}
		return false;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (((Integer)state.getProperties().get(AGE)).intValue( )== 3) return FBiomesItems.BERRIES;
        else return null;
	}
	 
	@Override
	public int quantityDropped(Random rand) {
        return rand.nextInt(3)+1;
    }
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);

        if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
        {

            if (state.getValue(AGE) < 3) {
                float f = getGrowthChance(this, worldIn, pos);

                if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0))
                {
                    grow(worldIn, rand, pos, state);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
                }
            }
        }
    }
	
	protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos)
    {
        float f = 1.0F;
        BlockPos blockpos = pos.down();

        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                float f1 = 0.0F;
                IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));

                if (iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, blockpos.add(i, 0, j), net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable)blockIn))
                {
                    f1 = 1.0F;

                    if (iblockstate.getBlock().isFertile(worldIn, blockpos.add(i, 0, j)))
                    {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0)
                {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock();
        boolean flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock();

        if (flag && flag1)
        {
            f /= 2.0F;
        }
        else
        {
            boolean flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();

            if (flag2)
            {
                f /= 2.0F;
            }
        }
        return f;
    }
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return state.getValue(AGE) < 3 && rand.nextFloat() < 0.5;
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.cycleProperty(AGE), 4);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity){
		if (entity.hurtResistantTime <= 0 &&!(entity instanceof EntityItem)) entity.attackEntityFrom(DamageSource.CACTUS, 1);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{AGE});
	}
	 
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AGE, meta);
	}

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
    @Override
	public int getMetaFromState(IBlockState state) {
        return ((Integer)state.getProperties().get(AGE)).intValue();
    }	 
	 

}
