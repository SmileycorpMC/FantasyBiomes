package net.smileycorp.fbiomes.common.world.biomes;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.fbiomes.common.ModDefinitions;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.world.gen.WorldGenBrambles;
import net.smileycorp.fbiomes.common.world.gen.features.WorldGenBoulder;
import net.smileycorp.fbiomes.common.world.gen.features.WorldGenStoneCircle;

public class BiomeMoorland extends Biome {
	
	WorldGenStoneCircle circle = new WorldGenStoneCircle();

	public BiomeMoorland() {
		super(new BiomeProperties("Moorland").setBaseHeight(1.1F).setHeightVariation(0.5F));
		topBlock=Blocks.GRASS.getDefaultState();
		fillerBlock=Blocks.DIRT.getDefaultState();
		setRegistryName(ModDefinitions.getResource("Moorland"));
	}
	
	@Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
	        return new WorldGenBigTree(false);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return getFoliageColorAtPos(pos);
    }
	
	 @Override
	@SideOnly(Side.CLIENT)
	 public int getFoliageColorAtPos(BlockPos pos) {
		 return 0x629E48;
	 }
	 
	 @Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunk, int x, int z, double noise) {
		 super.genTerrainBlocks(world, rand, chunk, x, z, noise);
		 int i = x & 15;
	     int k = z & 15;
	     int y = 0;
	     for (int j = 255; j >= 0; --j) {
	    	 if (chunk.getBlockState(i, j, k).isFullCube()) {
	    		 y = j;
	    		 break;
	    	 }
	     }
	     System.out.println(x + ", " + y + ", " + z);
	     if (y < 82 && y > 75) {
	    	 for (int j = y - 1; j <= y; j++) {
	    		 if (j<= 0 || j>=255) break;
	    		// if (chunk.getBlockState(i, j, k).getBlock() == Blocks.DIRT || chunk.getBlockState(i, j, k).getBlock() == Blocks.GRASS) {
	    			 chunk.setBlockState(i, j, k, FBiomesBlocks.PEAT.getDefaultState());
	    		 //}
	    	 }
	     }
	}
	
	@Override
    public BiomeDecorator createBiomeDecorator(){
        return getModdedBiomeDecorator(new BiomeMoorDecorator());
    }	
	
	public class BiomeMoorDecorator extends BiomeDecorator {
		
		BiomeMoorDecorator(){
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
	    public void decorate(World worldIn, Random rand, Biome biome, BlockPos pos)
	    {
	        if (this.decorating)
	        {
	            throw new RuntimeException("Already decorating");
	        }
	        else
	        {
	        	WorldGenerator BOULDER_GENERATOR = new WorldGenBoulder();
	            this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
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
	            DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
	            WorldGenerator BRAMBLE_GENERATOR = new WorldGenBrambles();
	            if(TerrainGen.decorate(worldIn, rand, new net.minecraft.util.math.ChunkPos(pos), EventType.GRASS)) {
	                for (int i = 0; i < 16; ++i) {
	                    int j = rand.nextInt(16) + 8;
	                    int k = rand.nextInt(16) + 8;
	                    int l = rand.nextInt(worldIn.getHeight(pos.add(j, 0, k)).getY() + 32);
	                    
	                    BRAMBLE_GENERATOR.generate(worldIn, rand, pos.add(j, l, k));
	                    DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j, l, k));
	                }
	            }
	            int j = rand.nextInt(16) + 8;
                int k = rand.nextInt(16) + 8;
                int l = rand.nextInt(worldIn.getHeight(pos.add(j, 0, k)).getY() + 32);
                if (rand.nextInt(6)==0) BOULDER_GENERATOR.generate(worldIn, rand, pos.add(j, l, k));
                else if (rand.nextInt(16)==0) circle.generate(worldIn, rand, pos.add(j, l, k));
	            generateTrees(worldIn, biome, rand, pos);
	            this.decorating = false;
	        }
	    }
	
		private void generateTrees(World world, Biome biome, Random rand, BlockPos chunkPos)
	    {
	        int treesToGen = treesPerChunk;
	
	        if(TerrainGen.decorate(world, rand, chunkPos, DecorateBiomeEvent.Decorate.EventType.TREE)) {
		        for (int numTreesGenerated = 0; numTreesGenerated < treesToGen; ++numTreesGenerated)
		        {
		        	if (rand.nextInt(6)!=0) break;
		            int treeX = rand.nextInt(16) + 8;
		            int treeZ = rand.nextInt(16) + 8;
		            WorldGenAbstractTree treeGen = biome.getRandomTreeFeature(rand);
		            treeGen.setDecorationDefaults();
		            BlockPos blockpos = world.getHeight(chunkPos.add(treeX, 0, treeZ));
		
		            if (treeGen.generate(world, rand, blockpos)){
		                treeGen.generateSaplings(world, rand, blockpos);
		            }
		        }
	        }
    	}
	
    }
		
}
