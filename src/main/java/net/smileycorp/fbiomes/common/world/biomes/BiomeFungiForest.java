package net.smileycorp.fbiomes.common.world.biomes;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.fbiomes.client.ClientProxy;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.world.gen.fungusforest.*;
import net.smileycorp.fbiomes.common.world.gen.tree.WorldGenElderwoodTree;

import java.util.Random;

public class BiomeFungiForest extends Biome {

	public BiomeFungiForest() {
		super(new BiomeProperties("Fungus Forest").setBaseHeight(0.3F).setHeightVariation(0.0F));
		topBlock=Blocks.GRASS.getDefaultState();
		fillerBlock=Blocks.DIRT.getDefaultState();
		setRegistryName(Constants.loc("Fungus_Forest"));
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySpider.class, 5, 2, 6));
		spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySheep.class, 3, 1, 4));
		spawnableCreatureList.add(new Biome.SpawnListEntry(EntityMooshroom.class, 1, 1, 2));
		spawnableMonsterList.clear();
		spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpider.class, 5, 2, 6));
		spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 3, 1, 4));
		spawnableMonsterList.add(new Biome.SpawnListEntry(EntityWitch.class, 1, 1, 2));
	}
	
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
       int chance = rand.nextInt(10);
       if (chance<4) {
    	   return rand.nextInt(3)==1 ? new WorldGenBigTree(false) : new WorldGenTrees(false);
       }
       else {
    	   return new WorldGenElderwoodTree(false, true);
       }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return getFoliageColorAtPos(pos);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos) {
		if (ClientProxy.stateCache == null) return 0x06B700;
		if (ClientProxy.stateCache.getBlock() == Blocks.LEAVES) {
			BlockPlanks.EnumType variant = ClientProxy.stateCache.getValue(BlockOldLeaf.VARIANT);
			if (variant == BlockPlanks.EnumType.BIRCH) return 0xF7AD00;;
			if (variant == BlockPlanks.EnumType.OAK) return 0x9E1A06;
		}
	 	return 0x06B700;
	}
	
	@Override
    public BiomeDecorator createBiomeDecorator(){
        return getModdedBiomeDecorator(new BiomeFungiForestDecorator());
    }
	
	public class BiomeFungiForestDecorator extends BiomeDecorator {
		
		BiomeFungiForestDecorator(){
			waterlilyPerChunk = 0;
			treesPerChunk = -999;
	        extraTreeChance = 1F;
	        flowersPerChunk = 0;
	        grassPerChunk = 20;
	        deadBushPerChunk = 0;
	        mushroomsPerChunk = 35;
	        reedsPerChunk = 0;
	        cactiPerChunk = 0;
	        gravelPatchesPerChunk = 0;
	        sandPatchesPerChunk = 0;
	        clayPerChunk = 0;
	        bigMushroomsPerChunk = 10;
	        generateFalls = false;
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
	            chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(world.getWorldInfo().getGeneratorOptions()).build();
	            chunkPos = pos;
	            dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), this.chunkProviderSettings.dirtSize);
	            gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), this.chunkProviderSettings.gravelSize);
	            graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), chunkProviderSettings.graniteSize);
	            dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), chunkProviderSettings.dioriteSize);
	            andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), chunkProviderSettings.andesiteSize);
	            coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), this.chunkProviderSettings.coalSize);
	            ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), this.chunkProviderSettings.ironSize);
	            goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), this.chunkProviderSettings.goldSize);
	            redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), this.chunkProviderSettings.redstoneSize);
	            diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), this.chunkProviderSettings.diamondSize);
	            lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), this.chunkProviderSettings.lapisSize);
	            DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
	            
	            WorldGenerator SHROOM_GENERATOR = new WorldGenShroom();
                
	            for (int i = 0; i < 4; ++i)
	            {
	                for (int j = 0; j < 4; ++j)
	                {
	                    int k = i * 4 + 1 + 8 + rand.nextInt(3);
	                    int l = j * 4 + 1 + 8 + rand.nextInt(3);
	                    BlockPos blockpos = world.getHeight(pos.add(k, 0, l));
	                    if (TerrainGen.decorate(world, rand, new ChunkPos(pos), blockpos, EventType.TREE))
	                    {
	                        WorldGenAbstractTree worldgenabstracttree = getRandomTreeFeature(rand);
	                        worldgenabstracttree.setDecorationDefaults();

	                        if (worldgenabstracttree.generate(world, rand, blockpos))
	                        {
	                            worldgenabstracttree.generateSaplings(world, rand, blockpos);
	                        } else if (worldgenabstracttree instanceof WorldGenElderwoodTree) {
	                        	worldgenabstracttree = rand.nextInt(3) == 0 ? new WorldGenCanopyTree(false) : new WorldGenBigTree(false);
		                        worldgenabstracttree.setDecorationDefaults();

		                        if (worldgenabstracttree.generate(world, rand, blockpos))
		                        {
		                            worldgenabstracttree.generateSaplings(world, rand, blockpos);
		                        }
	                        }
	                    }
	                }
	            }
	            
	            for (int i = 0; i < 4; ++i)
	            {
	                for (int j = 0; j < 4; ++j)
	                {
	                    int k = i * 4 + 1 + 8 + rand.nextInt(3);
	                    int l = j * 4 + 1 + 8 + rand.nextInt(3);
	                    BlockPos blockpos = world.getHeight(pos.add(k, 0, l));

	                    if (TerrainGen.decorate(world, rand, new ChunkPos(pos), blockpos, EventType.BIG_SHROOM))
	                    {
	                    	if (rand.nextInt(3)==0) {
	                    		WorldGenBigGlowshroom shroom = new WorldGenBigGlowshroom(rand);
	                    		shroom.generate(world, rand, blockpos);
	                    	} else if (rand.nextInt(3)==1) {
	                    		WorldGenBigFBMushroomBase shroom = new WorldGenBigFBMushroom(rand);
		                    	shroom.generate(world, rand, blockpos);
	                    	} else {
	                    		WorldGenBigMushroom shroom = new WorldGenBigMushroom();
	                    		shroom.generate(world, rand, blockpos);
	                    	}
	                    }
	                }
	            }   
	            
	            for (int i = 0; i < 128; ++i)
                {
	            	if (TerrainGen.decorate(world, rand, new ChunkPos(pos), pos, EventType.SHROOM)) {
	            		int j = rand.nextInt(16) + 8;
	            		int k = rand.nextInt(16) + 8;
	            		int l = rand.nextInt(world.getHeight(pos.add(j, 0, k)).getY() + 32);
	            		if (rand.nextInt(6)<2) {
                    		WorldGenSmallFBMushroomBase shroom = rand.nextInt(2)==0 ? new WorldGenSmallFBMushroom(rand) : new WorldGenSmallGlowshroom(rand);
	                    	shroom.generate(world, rand, pos.add(j, l, k));
	            		} else if (rand.nextInt(6)<5) {
		            		SHROOM_GENERATOR.generate(world, rand, pos.add(j, l, k));
	            		}
	            	}
                }
	            
	            for (int i = 0; i < 128; ++i)
                {
	            	if (rand.nextInt(5) == 0 && TerrainGen.decorate(world, rand, new ChunkPos(pos), pos, EventType.GRASS)) {
	            		int j = rand.nextInt(16) + 8;
	            		int k = rand.nextInt(16) + 8;
	            		int l = rand.nextInt(world.getHeight(pos.add(j, 0, k)).getY() + 32);
	            		if (rand.nextInt(3)==1) {
	            			DOUBLE_PLANT_GENERATOR.generate(world, rand, pos.add(j, l, k));
	            		} else {
	            			getRandomWorldGenForGrass(rand).generate(world, rand, pos.add(j, l, k));
	            		}
	            	}
                }
		            
	            this.decorating = false;
	        }
	    }
	
    }
		
}
