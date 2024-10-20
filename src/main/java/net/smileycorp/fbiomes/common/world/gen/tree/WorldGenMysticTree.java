package net.smileycorp.fbiomes.common.world.gen.tree;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.fbiomes.common.blocks.BlockFBMushroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.world.gen.fungusforest.WorldGenSmallFBMushroom;
import net.smileycorp.fbiomes.common.world.gen.fungusforest.WorldGenSmallFBMushroomBase;
import net.smileycorp.fbiomes.common.world.gen.fungusforest.WorldGenSmallGlowshroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenMysticTree extends WorldGenAbstractTree {
	
	final boolean isNatural;
	IBlockState wood = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
	IBlockState bark = wood.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
	IBlockState leaves = Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK);
	IBlockState roots = FBiomesBlocks.ROOTS.getDefaultState();
	
	public WorldGenMysticTree(boolean notify, boolean isNatural) {
		super(notify);
		this.isNatural=isNatural;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		BlockPos ground = pos.down();
		IBlockState soil = world.getBlockState(ground);
		if (soil.getBlock().canSustainPlant(soil, world, ground, EnumFacing.UP, (BlockSapling) Blocks.SAPLING)) {
			if(!(world.isBlockFullCube(ground.north()) && world.isBlockFullCube(ground.south()) && world.isBlockFullCube(ground.east()) &&  world.isBlockFullCube(ground.west()))) return false;
			int h = rand.nextInt(5)+13;
			
			//trunk
			for (int j = 0; j<=h; j++){
				setBlock(world, rand, ground, pos.up(j), wood, h);
			}
			
			//roots
			for (int r = 0; r <4; r++) {
				BlockPos rpos = pos;
				switch (r) {
					case 0:
						rpos=pos.north(1);
						break;
					case 1:
						rpos=pos.south(1);
						break;
					case 2:
						rpos=pos.east(1);
						break;
					case 3:
						rpos=pos.west(1);
						break;
				}
				int rh = rand.nextInt(2)+2;
				if (rh==3) {
					for (int k = -1; k<2; k++){
						for (int i = -1; i<2; i++){
							if (Math.abs(i*k)==0) {
								BlockPos rhpos = rpos.north(k).west(i); 
								setBlock(world, rand, ground, rhpos, bark, h);
								IBlockState rstate = world.getBlockState(rhpos.down());
								if (rstate==Blocks.AIR||rstate instanceof BlockBush) world.setBlockState(rhpos.down(), bark, 18);
								if (isNatural&&(rstate.getMaterial()==Material.GRASS||rstate.getMaterial()==Material.GROUND)) tryGenRoots(world, rand, rhpos.down());
							}
						}
					}
				}
				for (int j = 0; j<rh; j++){
					setBlock(world, rand, ground, rpos.up(j), bark, h);	
				}
			}
			
			//branches
			List<Integer> skips = new ArrayList<Integer>();
			for(int s = 0; s < rand.nextInt(4); s++) {
				skips.add(rand.nextInt(8));
			}
			for (int b = 0; b < 8; b++) {
				if (skips.contains(b)) continue;
				int h2 = h+rand.nextInt(3)-2;
				int o = 0;
				int t = rand.nextInt(3)+6;
				BlockPos bpos=pos.up(h2);
				int f = 1;
				switch (b){
					//north
					case 0 :
						for (int n = 0; n< t; n++) {
							bpos = bpos.north(f).west(o);
							if (rand.nextInt(t)<Math.floor(t/3)) {
								bpos = bpos.up(1);
							}
							world.setBlockState(bpos, bark, 18);
							generateBranches(world, bpos, n, t);
							if (n>2) {
								o = rand.nextInt(3)-1;
								f = rand.nextInt(t-n)-n;
								if (f>1) f = 1;
							}
						}
						break;
					//south
					case 1:
						for (int n = 0; n< t; n++) {
							bpos = bpos.south(f).west(o);
							if (rand.nextInt(t)<Math.floor(t/3)) {
								bpos = bpos.up(1);
							}
							world.setBlockState(bpos, bark, 18);
							generateBranches(world, bpos, n, t);
							if (n>2) {
								o = rand.nextInt(3)-1;
								f = rand.nextInt(t-n)-n;
								if (f>1) f = 1;
							}
						}
						break;
					//west
					case 2:
						for (int n = 0; n< t; n++) {
							bpos = bpos.west(f).north(o);
							if (rand.nextInt(t)<Math.floor(t/3)) {
								bpos = bpos.up(1);
							}
							generateBranches(world, bpos, n, t);
							if (n>2) {
								o = rand.nextInt(3)-1;
								f = rand.nextInt(t-n)-n;
								if (f>1) f = 1;
							}
						}
						break;
					//east
					case 3:
						for (int n = 0; n< t; n++) {
							bpos = bpos.east(f).north(o);
							if (rand.nextInt(t)<Math.floor(t/3)) {
								bpos = bpos.up(1);
							}
							generateBranches(world, bpos, n, t);
							if (n>2) {
								o = rand.nextInt(3)-1;
								f = rand.nextInt(t-n)-n;
								if (f>1) f = 1;
							}
						}
						break;
							
					//neast
					case 4:
						for (int n = 0; n< t; n++) {
							bpos = bpos.east((f+o)%2).north(Math.abs((f-o)%2));
							if (rand.nextInt(t)<Math.floor(t/3)) {
								bpos = bpos.up(1);
							}
							generateBranches(world, bpos, n, t);
							if (n>2) {
								o = rand.nextInt(3)-1;
								f = rand.nextInt(t-n)-n;
								if (f>1) f = 1;
							}
						}
						break;
							
					//nwest
					case 5:
						for (int n = 0; n< t; n++) {
							bpos = bpos.west((f+o)%2).north(Math.abs((f-o)%2));
							if (rand.nextInt(t)<Math.floor(t/3)) {
								bpos = bpos.up(1);
							}
							generateBranches(world, bpos, n, t);
							if (n>2) {
								o = rand.nextInt(3)-1;
								f = rand.nextInt(t-n)-n;
								if (f>1) f = 1;
							}
						}
						break;
						
					//seast
					case 6:
						for (int n = 0; n< t; n++) {
							bpos = bpos.east((f+o)%2).south(Math.abs((f-o)%2));
							if (rand.nextInt(t)<Math.floor(t/3)) {
								bpos = bpos.up(1);
							}
							generateBranches(world, bpos, n, t);
							if (n>2) {
								o = rand.nextInt(3)-1;
								f = rand.nextInt(t-n)-n;
								if (f>1) f = 1;
							}
						}
						break;
							
					//swest
					case 7:
						for (int n = 0; n< t; n++) {
							bpos = bpos.west((f+o)%2).south(Math.abs((f-o)%2));
							if (rand.nextInt(t)<Math.floor(t/3)) {
								bpos = bpos.up(1);
							}
							generateBranches(world, bpos, n, t);
							if (n>2) {
								o = rand.nextInt(3)-1;
								f = rand.nextInt(t-n)-n;
								if (f>1) f = 1;
							}
						}
						break;
					}
				
			}
			
			return true;
		}
		return false;
	}

	private void tryGenRoots(World world, Random rand, BlockPos soil) {
		BlockPos pos = soil.down();
		if (world.isAirBlock(pos)&&rand.nextInt(3)==1){
			int length = rand.nextInt(3);
			for (int j = 0; j<length; j++) {
				if (world.isAirBlock(pos.down(j))) world.setBlockState(pos, roots, 18);
				else break;
			}
		}
	}

	private void setBlock(World world, Random rand, BlockPos ground, BlockPos pos, IBlockState state, int height) {
		world.setBlockState(pos, state, 18);
		if (isNatural&&rand.nextInt(7)<3) {
			EnumFacing facing = DirectionUtils.getRandomDirectionXZ(rand);
			BlockPos facePos = pos.offset(facing);
			if (rand.nextInt(4)==0 && state!=bark && (pos.getY()-ground.getX())<(height-6)) {
				WorldGenSmallFBMushroomBase shroom = rand.nextInt(2)==0 ? new WorldGenSmallFBMushroom(rand, facing) : new WorldGenSmallGlowshroom(rand, facing);
				shroom.generate(world, rand, facePos);
			} else {
				IBlockState shroom = (rand.nextInt(3)==1 ? FBiomesBlocks.glowshrooms[rand.nextInt(FBiomesBlocks.glowshrooms.length)]:
					FBiomesBlocks.shrooms[rand.nextInt(FBiomesBlocks.shrooms.length)]).getDefaultState();
				
				if (world.isAirBlock(facePos)) world.setBlockState(facePos, shroom.withProperty(BlockFBMushroom.FACING, facing));
			}
		}
	}

	private void generateBranches(World world, BlockPos pos, int n, int t) {
		world.setBlockState(pos, bark, 18);
		//if (n > t-3) {
			generateLeaves(world, pos, 3);
		//}
	}
	
	private void generateLeaves(World world, BlockPos pos, int r) {
		for (int i = -r; i<=r; i++){
			for (int j = -r; j<=r; j++){
				for (int k = -r; k<=r; k++){
					if (((i*i)+(j*j)+(k*k))<(r*r)){
						BlockPos newpos = pos.north(i).up(j).east(k);
						if (world.isAirBlock(newpos) || world.getBlockState(newpos).getBlock() instanceof BlockFBMushroom) {
							world.setBlockState(newpos, leaves, 18);
						}
					}
				}
			}
		}
	}	
}	
