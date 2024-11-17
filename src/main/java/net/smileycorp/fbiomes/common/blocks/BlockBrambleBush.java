package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.items.FBiomesItems;

import java.util.Random;

public class BlockBrambleBush extends BlockBush implements IGrowable, BlockProperties {
    
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.15, 0.0D, 0.15, 0.85, 0.7, 0.85);
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
		if (state.getValue(AGE) < 3) return false;
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
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(AGE) == 3) return FBiomesItems.BERRIES;
        else return null;
	}
	 
	@Override
	public int quantityDropped(Random rand) {
        return rand.nextInt(2) + 1;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(world, pos, state, rand);
        if (!world.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (world.getLightFromNeighbors(pos.up()) < 9 || state.getValue(AGE) >= 3) return;
        if(!ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int)(25f / getGrowthChance(world, pos)) + 1) == 0)) return;
        grow(world, rand, pos, state);
        ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
    }
	
	protected float getGrowthChance(World world, BlockPos pos) {
        float f = 1;
        BlockPos blockpos = pos.down();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0;
                IBlockState iblockstate = world.getBlockState(blockpos.add(i, 0, j));
                if (iblockstate.getBlock().canSustainPlant(iblockstate, world, blockpos.add(i, 0, j), net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable)this)) {
                    f1 = 1;
                    if (iblockstate.getBlock().isFertile(world, blockpos.add(i, 0, j))) f1 = 3.0F;
                }
                if (i != 0 || j != 0) f1 /= 4.0F;
                f += f1;
            }
        }
        BlockPos north = pos.north();
        BlockPos south = pos.south();
        BlockPos west = pos.west();
        BlockPos east = pos.east();
        if ((world.getBlockState(west).getBlock() == this || this == world.getBlockState(east).getBlock()) &&
                (this == world.getBlockState(north).getBlock() || this == world.getBlockState(south).getBlock())) return f / 2f;
        return (this == world.getBlockState(west.north()).getBlock() || this == world.getBlockState(east.north()).getBlock()
                || this == world.getBlockState(east.south()).getBlock() || this == world.getBlockState(west.south()).getBlock()) ? f/2f : f;
    }
    
    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean remote) {
        return state.getValue(AGE) < 3;
    }

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
        return true;
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
		return new BlockStateContainer(this, AGE);
	}
	 
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(AGE, meta);
	}
    
    @Override
	public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

}
