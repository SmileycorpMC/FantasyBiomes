package net.smileycorp.fbiomes.common.world.biomes;

import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.smileycorp.fbiomes.common.Constants;

import java.util.Random;

public class BiomeIronHills extends Biome {

	public BiomeIronHills() {
		super(new BiomeProperties("Iron Hills").setBaseHeight(4F).setHeightVariation(1F));
		topBlock=Blocks.SNOW.getDefaultState();
		fillerBlock=Blocks.SNOW.getDefaultState();
		setRegistryName(Constants.loc("iron_hills"));
	}
	
	@Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return rand.nextInt(2)==1 ? new WorldGenTaiga1() : new WorldGenTaiga2(false);
	}
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunk, int x, int z, double noise) {
		//int s = world.getSeaLevel();
        int i = x & 15;
        int k = z & 15;
        int h = -1;
        //BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int j = 255; j >= 0; --j) {   
        	if (chunk.getBlockState(i, j, k).getMaterial()==Material.AIR) {
        		h = j-1;
        		continue;
        	}
            if (j <= rand.nextInt(5)) {
                chunk.setBlockState(i, j, k, BEDROCK);
            } else if ((h-j<=6&&h-j>=0)&&h>=100) {
            	trySetSnow(world, chunk, x, j, z);
            } else if (h < 80) {
            	if (j == h) {
            		chunk.setBlockState(i, j, k, Blocks.GRASS.getDefaultState());
            	} else if (h - j > 5) {
            		chunk.setBlockState(i, j, k, Blocks.DIRT.getDefaultState());
            	}
            }
        }
	}
	
	private void trySetSnow(World world, ChunkPrimer chunk, int x, int j, int z) {
		int i = x & 15;
        int k = z & 15;
        BlockPos pos = new BlockPos(x, j, z);
		for (EnumFacing facing : EnumFacing.VALUES) {
    		if (facing != EnumFacing.DOWN || facing != EnumFacing.UP) {
    			BlockPos newpos = pos.offset(facing);
    			if (world.getHeight(newpos.getX(), newpos.getZ()) > j) return;
    		}
    	}
		chunk.setBlockState(i, j, k, Blocks.SNOW.getDefaultState());
	}

	@Override
    public BiomeDecorator createBiomeDecorator(){
        return getModdedBiomeDecorator(new BiomeIronHillsDecorator());
	}
		
	
	public class BiomeIronHillsDecorator extends BiomeDecorator {
		
		private boolean oresGenerated = false;
		
		BiomeIronHillsDecorator(){
			waterlilyPerChunk = 0;
			treesPerChunk = 1;
	        extraTreeChance = 0.5F;
	        flowersPerChunk = 4;
	        grassPerChunk = 15;
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
	        }
	    }
		
		@Override
		protected void generateOres(World world, Random rand) {
			WorldGenMinable emeraldGen = new WorldGenMinable(Blocks.EMERALD_ORE.getDefaultState(), 1);
	        MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(world, rand, chunkPos));
	        if (TerrainGen.generateOre(world, rand, dirtGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIRT))
	        this.genStandardOre1(world, rand, this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, getHeightMultiplied(this.chunkProviderSettings.dirtMaxHeight));
	        if (TerrainGen.generateOre(world, rand, gravelOreGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GRAVEL))
	        this.genStandardOre1(world, rand, this.chunkProviderSettings.gravelCount, this.gravelOreGen, this.chunkProviderSettings.gravelMinHeight, getHeightMultiplied(this.chunkProviderSettings.gravelMaxHeight));
	        if (TerrainGen.generateOre(world, rand, dioriteGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIORITE))
	        this.genStandardOre1(world, rand, this.chunkProviderSettings.dioriteCount, this.dioriteGen, this.chunkProviderSettings.dioriteMinHeight, getHeightMultiplied(this.chunkProviderSettings.dioriteMaxHeight));
	        if (TerrainGen.generateOre(world, rand, graniteGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GRANITE))
	        this.genStandardOre1(world, rand, this.chunkProviderSettings.graniteCount, this.graniteGen, this.chunkProviderSettings.graniteMinHeight, getHeightMultiplied(this.chunkProviderSettings.graniteMaxHeight));
	        if (TerrainGen.generateOre(world, rand, andesiteGen, chunkPos, OreGenEvent.GenerateMinable.EventType.ANDESITE))
	        this.genStandardOre1(world, rand, this.chunkProviderSettings.andesiteCount, this.andesiteGen, this.chunkProviderSettings.andesiteMinHeight, getHeightMultiplied(this.chunkProviderSettings.andesiteMaxHeight));
	        if (TerrainGen.generateOre(world, rand, coalGen, chunkPos, OreGenEvent.GenerateMinable.EventType.COAL))
	        this.genStandardOre1(world, rand, this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, getHeightMultiplied(this.chunkProviderSettings.coalMaxHeight));
	        if (TerrainGen.generateOre(world, rand, ironGen, chunkPos, OreGenEvent.GenerateMinable.EventType.IRON))
	        this.genStandardOre1(world, rand, this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, getHeightMultiplied(this.chunkProviderSettings.ironMaxHeight));
	        if (TerrainGen.generateOre(world, rand, goldGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GOLD))
	        this.genStandardOre1(world, rand, this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, getHeightMultiplied(this.chunkProviderSettings.goldMaxHeight));
	        if (TerrainGen.generateOre(world, rand, redstoneGen, chunkPos, OreGenEvent.GenerateMinable.EventType.REDSTONE))
	        this.genStandardOre1(world, rand, this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, getHeightMultiplied(this.chunkProviderSettings.redstoneMaxHeight));
	        if (TerrainGen.generateOre(world, rand, diamondGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIAMOND))
	        this.genStandardOre1(world, rand, this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, getHeightMultiplied(this.chunkProviderSettings.diamondMaxHeight));
	        if (TerrainGen.generateOre(world, rand, lapisGen, chunkPos, OreGenEvent.GenerateMinable.EventType.LAPIS))
	        this.genStandardOre2(world, rand, this.chunkProviderSettings.lapisCount, this.lapisGen, getHeightMultiplied(this.chunkProviderSettings.lapisCenterHeight), getHeightMultiplied(this.chunkProviderSettings.lapisSpread));
	        if (TerrainGen.generateOre(world, rand, emeraldGen, chunkPos, OreGenEvent.GenerateMinable.EventType.EMERALD))
	        this.genStandardOre2(world, rand, 5 + rand.nextInt(3), emeraldGen, 40, 35);
	        MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(world, rand, chunkPos));
	        if (!oresGenerated) {
	        	oresGenerated = true;
	        	generateOres(world, rand);
	        }
	    }
		
		private int getHeightMultiplied(int height) {
			int heightMult = Math.round(height*2f);
			return heightMult > 255 ? 255 : heightMult < 1 ? 1 : heightMult;
		}
	
    }
}
