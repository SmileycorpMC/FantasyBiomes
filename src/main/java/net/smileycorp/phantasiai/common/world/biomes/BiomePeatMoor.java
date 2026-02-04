package net.smileycorp.phantasiai.common.world.biomes;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.blocks.BlockMud;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;
import net.smileycorp.phantasiai.common.blocks.enums.EnumMudType;
import net.smileycorp.phantasiai.common.world.gen.features.moorland.WorldGenBoulder;
import net.smileycorp.phantasiai.common.world.gen.features.moorland.WorldGenBrambles;
import net.smileycorp.phantasiai.common.world.gen.features.moorland.WorldGenGraniteBoulder;
import net.smileycorp.phantasiai.common.world.gen.features.moorland.WorldGenStoneCircle;

import java.util.Random;

public class BiomePeatMoor extends Biome {
	
	WorldGenStoneCircle circle = new WorldGenStoneCircle();

	public BiomePeatMoor() {
		super(new BiomeProperties("Moorland").setBaseHeight(1.1F).setHeightVariation(0.3F));
		topBlock = Blocks.GRASS.getDefaultState();
		fillerBlock = Blocks.DIRT.getDefaultState();
		setRegistryName(Constants.loc("Moorland"));
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
		boolean peat = Math.sin(noise) < 0;
		topBlock = peat ? PhantasiaiBlocks.GRASSY_MUD.getDefaultState().withProperty(BlockMud.VARIANT, EnumMudType.PEAT) : Blocks.GRASS.getDefaultState();
		fillerBlock = peat ? PhantasiaiBlocks.MUD.getDefaultState().withProperty(BlockMud.VARIANT, EnumMudType.PEAT) : Blocks.DIRT.getDefaultState();
		super.genTerrainBlocks(world, rand, chunk, x, z, noise);
	}
	
	@Override
    public BiomeDecorator createBiomeDecorator(){
        return getModdedBiomeDecorator(new BiomeMoorDecorator());
    }	
	
	public class BiomeMoorDecorator extends BiomeDecorator {
		
		BiomeMoorDecorator(){
			waterlilyPerChunk = 0;
			treesPerChunk = 1;
	        extraTreeChance = 0.2F;
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
	    public void decorate(World world, Random rand, Biome biome, BlockPos pos) {
	        if (decorating) throw new RuntimeException("Already decorating");
			chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(world.getWorldInfo().getGeneratorOptions()).build();
			chunkPos = pos;
			dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), chunkProviderSettings.dirtSize);
			gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), chunkProviderSettings.gravelSize);
			graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), chunkProviderSettings.graniteSize);
			dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), chunkProviderSettings.dioriteSize);
			andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), chunkProviderSettings.andesiteSize);
			coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), chunkProviderSettings.coalSize);
			ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), chunkProviderSettings.ironSize);
			goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), chunkProviderSettings.goldSize);
			redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), chunkProviderSettings.redstoneSize);
			diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), chunkProviderSettings.diamondSize);
			lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), chunkProviderSettings.lapisSize);
			DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
			WorldGenerator BRAMBLE_GENERATOR = new WorldGenBrambles();
			if(TerrainGen.decorate(world, rand, new net.minecraft.util.math.ChunkPos(pos), EventType.GRASS)) for (int i = 0; i < 16; ++i) {
				int j = rand.nextInt(16) + 8;
				int k = rand.nextInt(16) + 8;
				int l = rand.nextInt(world.getHeight(pos.add(j, 0, k)).getY() + 32);
				BRAMBLE_GENERATOR.generate(world, rand, pos.add(j, l, k));
				DOUBLE_PLANT_GENERATOR.generate(world, rand, pos.add(j, l, k));
			}
			for (int i = 0; i < 128; ++i) if (rand.nextInt(5) == 0 && TerrainGen.decorate(world, rand, new ChunkPos(pos), pos, EventType.GRASS)) {
				int j = rand.nextInt(16) + 8;
				int k = rand.nextInt(16) + 8;
				int l = rand.nextInt(world.getHeight(pos.add(j, 0, k)).getY() + 32);
				(rand.nextInt(3) == 1 ? DOUBLE_PLANT_GENERATOR : getRandomWorldGenForGrass(rand)).generate(world, rand, pos.add(j, l, k));
			}
			int j = rand.nextInt(16) + 8;
			int k = rand.nextInt(16) + 8;
			int l = rand.nextInt(world.getHeight(pos.add(j, 0, k)).getY() + 32);
			if (rand.nextInt(6) == 0) (rand.nextInt(3) == 0 ? new WorldGenGraniteBoulder() : new WorldGenBoulder()).generate(world, rand, pos.add(j, l, k));
			else if (rand.nextInt(20) == 0) circle.generate(world, rand, pos.add(j, l, k));
			generateTrees(world, biome, rand, pos);
			decorating = false;
		}
	
		private void generateTrees(World world, Biome biome, Random rand, BlockPos chunkPos)
	    {

			int treesToGen = treesPerChunk;

			if (rand.nextFloat() < extraTreeChance)
			{
				++treesToGen;
			}
	
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
