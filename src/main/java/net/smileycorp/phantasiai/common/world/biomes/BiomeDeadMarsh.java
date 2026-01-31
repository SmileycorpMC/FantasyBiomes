package net.smileycorp.phantasiai.common.world.biomes;

import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;
import net.smileycorp.phantasiai.common.world.gen.tree.WorldGenGnarlwillow;

import java.util.Random;

public class BiomeDeadMarsh extends Biome {

	public BiomeDeadMarsh() {
		super(new BiomeProperties("Dead Marsh").setBaseHeight(-0.1F).setHeightVariation(-0.1F));
		topBlock = PhantasiaiBlocks.MUD.getDefaultState();
		fillerBlock = PhantasiaiBlocks.MUD.getDefaultState();
		setRegistryName(Constants.loc("Dead_Marsh"));
	}
	
	@Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return new WorldGenGnarlwillow(false);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return 0x775E2E;
    }
	
 	@Override
 	@SideOnly(Side.CLIENT)
 	public int getFoliageColorAtPos(BlockPos pos) {
		 return 0x492519;
	 }

	@Override
	public int getWaterColorMultiplier() {
		return 0xFBFF44;
	}

	@Override
    public BiomeDecorator createBiomeDecorator(){
        return getModdedBiomeDecorator(new Decorator());
    }
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunk, int x, int z, double noise) {
		double variation = GRASS_COLOR_NOISE.getValue((double)x * 0.25D, (double)z * 0.25D);
		if (variation > 0) {
			int i = x & 15;
			int j = z & 15;
			for (int k = 255; k >= 0; --k) if (chunk.getBlockState(j, k, i).getMaterial() != Material.AIR) {
				if (k == 63 && chunk.getBlockState(j, k, i).getBlock() != Blocks.AIR) {
					chunk.setBlockState(j, k, i, AIR);
					if (rand.nextBoolean()) chunk.setBlockState(j, k-1, i, WATER);
				}
				if (k == 62 && chunk.getBlockState(j, k, i).getBlock() != Blocks.WATER) chunk.setBlockState(j, k, i, WATER);
				break;
			}
		}
		boolean mud = Math.abs(noise) > 1;
		topBlock = (mud ? PhantasiaiBlocks.GRASSY_MUD : Blocks.GRASS).getDefaultState();
		fillerBlock = (mud ? PhantasiaiBlocks.MUD : Blocks.DIRT).getDefaultState();
		super.genTerrainBlocks(world, rand, chunk, x, z, noise);
	}
	
	
	protected static class Decorator extends BiomeDecorator {
		
		private Decorator() {
			waterlilyPerChunk = 0;
			treesPerChunk = 0;
	        extraTreeChance = 0.5F;
	        flowersPerChunk = 4;
	        grassPerChunk = 25;
	        deadBushPerChunk = 0;
	        mushroomsPerChunk = 0;
	        reedsPerChunk = 0;
	        cactiPerChunk = 0;
	        gravelPatchesPerChunk = 1;
	        sandPatchesPerChunk = 3;
	        clayPerChunk = 1;
	        bigMushroomsPerChunk = 0;
	        generateFalls = true;
		}
		
		@Override
	    public void decorate(World world, Random rand, Biome biome, BlockPos pos)
	    {
	        if (this.decorating)
	        {
	            throw new RuntimeException("Already decorating");
	        }
	        else
	        {
	            this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(world.getWorldInfo().getGeneratorOptions()).build();
	            this.chunkPos = pos;
	            this.dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), this.chunkProviderSettings.dirtSize);
	            this.gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), this.chunkProviderSettings.gravelSize);
	            this.graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
	            this.dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
	            this.andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
	            this.coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), this.chunkProviderSettings.coalSize);
	            this.ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), this.chunkProviderSettings.ironSize);
	            this.goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), this.chunkProviderSettings.goldSize);
	            this.redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), this.chunkProviderSettings.redstoneSize);
	            this.diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), this.chunkProviderSettings.diamondSize);
	            this.lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), this.chunkProviderSettings.lapisSize);
	            if(TerrainGen.decorate(world, rand, new ChunkPos(pos), EventType.GRASS)) {
	                for (int i = 0; i < 16; ++i) {
	                    int j = rand.nextInt(16) + 8;
	                    int k = rand.nextInt(16) + 8;
	                    int l = rand.nextInt(world.getHeight(pos.add(j, 0, k)).getY() + 32);
	                    biome.getRandomWorldGenForGrass(rand).generate(world, rand, pos.add(j, l, k));
	                }
	            }	
	            generateTrees(world, biome, rand, pos);
				//mulched bone
				for (int i = 0; i < 2 + rand.nextInt(3); i++) {
					int x = pos.getX() + rand.nextInt(16);
					int z = pos.getZ() + rand.nextInt(16);
					BlockPos.MutableBlockPos pos1 = new BlockPos.MutableBlockPos(world.getHeight(new BlockPos(x, 0, z)));
					System.out.println(pos1 + ", " + world.getBlockState(pos1));
					while (!world.getBlockState(pos1).isFullBlock()) pos1.setY(pos1.getY() - 1);
					if (world.getBlockState(pos1) == PhantasiaiBlocks.MUD.getDefaultState() && world.getBlockState(pos1.up()).getMaterial() == Material.WATER)
						world.setBlockState(pos1, PhantasiaiBlocks.MULCHED_BONE.getDefaultState(), 10);
				}
	            this.decorating = false;
	        }
	    }
		
		private void generateTrees(World worldIn, Biome biomeIn, Random random, BlockPos chunkPos)
	    {
	        int treesToGen = treesPerChunk;
	
	        if (random.nextFloat() < extraTreeChance)
	        {
	            ++treesToGen;
	        }
	
	        if(TerrainGen.decorate(worldIn, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.TREE)) {
		        for (int numTreesGenerated = 0; numTreesGenerated < treesToGen; ++numTreesGenerated)
		        {
		            int treeX = random.nextInt(16) + 8;
		            int treeZ = random.nextInt(16) + 8;
		            WorldGenAbstractTree treeGen = biomeIn.getRandomTreeFeature(random);
		            treeGen.setDecorationDefaults();
		            BlockPos blockpos = worldIn.getHeight(chunkPos.add(treeX, 0, treeZ));
					if (random.nextBoolean()) return;
		            if (treeGen.generate(worldIn, random, blockpos)){
		                treeGen.generateSaplings(worldIn, random, blockpos);
		            }
		        }
	        }
    	}
	
    }
		
}
