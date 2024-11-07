package net.smileycorp.fbiomes.common.world.biomes;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockStone;
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
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.entities.EntityPixie;
import net.smileycorp.fbiomes.common.world.gen.features.WorldGenMushroomLog;
import net.smileycorp.fbiomes.common.world.gen.features.WorldGenWitchCottage;
import net.smileycorp.fbiomes.common.world.gen.fungusforest.*;
import net.smileycorp.fbiomes.common.world.gen.tree.WorldGenBigRedOakTree;
import net.smileycorp.fbiomes.common.world.gen.tree.WorldGenOrantikkuTree;
import net.smileycorp.fbiomes.common.world.gen.tree.WorldGenGoldenBirchTree;
import net.smileycorp.fbiomes.common.world.gen.tree.WorldGenRedOakTree;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

public class EnchantedThicket extends Biome {

	public EnchantedThicket() {
		super(new BiomeProperties("Enchanted Thicket").setBaseHeight(0.3F).setHeightVariation(0.0F));
		topBlock = Blocks.GRASS.getDefaultState();
		fillerBlock = Blocks.DIRT.getDefaultState();
		setRegistryName(Constants.loc("enchanted_thicket"));
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPixie.class, 5, 2, 6));
		//spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySheep.class, 3, 1, 4));
		//spawnableCreatureList.add(new Biome.SpawnListEntry(EntityMooshroom.class, 1, 1, 2));
		spawnableMonsterList.clear();
		//spawnableMonsterList.add(new Biome.SpawnListEntry(EntityPixie.class, 5, 2, 6));
		//spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 3, 1, 4));
		//spawnableMonsterList.add(new Biome.SpawnListEntry(EntityWitch.class, 1, 1, 2));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return 0x5A9158;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos) {
	 	return 0x214720;
	}
	
	@Override
    public BiomeDecorator createBiomeDecorator(){
        return getModdedBiomeDecorator(new Decorator());
    }
	
	public class Decorator extends BiomeDecorator {
		
		private Decorator(){
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
	    public void decorate(World world, Random rand, Biome biome, BlockPos pos) {
			if (this.decorating) throw new RuntimeException("Already decorating");
			else {
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
				WorldGenOrantikkuTree CANOPY_TREE_GENERATOR = new WorldGenOrantikkuTree(false, true);
				Set<BlockPos> canopy_trees = Sets.newHashSet();
				//canopy trees
				for (int i = 0; i < 2; ++i) for (int j = 0; j < 2; ++j) {
					BlockPos blockpos = world.getHeight(pos.add(i * 8 + 1 + 8 + rand.nextInt(3), 0, j * 8 + 1 + 8 + rand.nextInt(3)));
					if (TerrainGen.decorate(world, rand, new ChunkPos(blockpos), blockpos, EventType.TREE)
							&& CANOPY_TREE_GENERATOR.canGenerate(world, blockpos)) canopy_trees.add(blockpos);
				}
				//features
				if (rand.nextInt(5) == 0) {
					BlockPos pos1 = world.getHeight(pos.add(rand.nextInt(16) - rand.nextInt(16), 0, rand.nextInt(16) - rand.nextInt(16)));
					WorldGenerator feature = getRandomFeature(world, pos1, rand, canopy_trees);
					if (feature != null) feature.generate(world, rand, pos1);
				}
				//trees and big mushrooms
				for (int i = 0; i < 8; ++i) for (int j = 0; j < 8; ++j) {
					BlockPos blockpos =  world.getHeight(pos.add(i * 2 + 1 + 8 + rand.nextInt(2), 0, j * 2 + 1 + 8 + rand.nextInt(2)));
                    if (rand.nextFloat() < 0.3) genBigMushroom(world, rand, blockpos, canopy_trees);
                    else genTree(world, rand, blockpos, canopy_trees);
                }
				for (BlockPos tree : canopy_trees) CANOPY_TREE_GENERATOR.generate(world, rand, tree);
				//mushrooms
				for (int i = 0; i < 128; ++i) {
					if (TerrainGen.decorate(world, rand, new ChunkPos(pos), pos, EventType.SHROOM)) {
						int j = rand.nextInt(16) + 8;
						int k = rand.nextInt(16) + 8;
						int l = rand.nextInt(world.getHeight(pos.add(j, 0, k)).getY() + 32);
						SHROOM_GENERATOR.generate(world, rand, pos.add(j, l, k));
					}
				}
				//grass
				for (int i = 0; i < 128; ++i) {
					if (rand.nextInt(5) == 0 && TerrainGen.decorate(world, rand, new ChunkPos(pos), pos, EventType.GRASS)) {
						int j = rand.nextInt(16) + 8;
						int k = rand.nextInt(16) + 8;
						int l = rand.nextInt(world.getHeight(pos.add(j, 0, k)).getY() + 32);
						if (rand.nextInt(3) == 1) DOUBLE_PLANT_GENERATOR.generate(world, rand, pos.add(j, l, k));
						else getRandomWorldGenForGrass(rand).generate(world, rand, pos.add(j, l, k));
					}
				}
				this.decorating = false;
			}
		}
		
		public void genTree(World world, Random rand, BlockPos pos, Collection<BlockPos> canopy_trees) {
			if (!TerrainGen.decorate(world, rand, new ChunkPos(pos), pos, EventType.TREE)) return;
			boolean canHuge = true;
			for (BlockPos tree : canopy_trees) {
				int dx = Math.abs(tree.getX() - pos.getX());
				int dz = Math.abs(tree.getZ() - pos.getZ());
				if (dx < 3 || dz < 3) return;
				if (dx < 6 || dz < 6) canHuge = false;
			}
			getRandomTreeFeature(rand, canHuge).generate(world, rand, pos);
		}
		
		public void genBigMushroom(World world, Random rand, BlockPos pos, Collection<BlockPos> canopy_trees) {
			if (!TerrainGen.decorate(world, rand, new ChunkPos(pos), pos, EventType.BIG_SHROOM)) return;
			boolean canHuge = true;
			for (BlockPos tree : canopy_trees) {
				int dx = Math.abs(tree.getX() - pos.getX());
				int dz = Math.abs(tree.getZ() - pos.getZ());
				if (dx < 3 || dz < 3) return;
				if (dx < 6 || dz < 6) canHuge = false;
			}
			getRandomBigMushroom(rand, canHuge).generate(world, rand, pos);
		}
		
		private WorldGenerator getRandomFeature(World world, BlockPos pos, Random rand, Set<BlockPos> canopyTrees) {
			return rand.nextInt(3) == 1 ? new WorldGenWitchCottage() : new WorldGenMushroomLog();
		}
		
		private WorldGenerator getRandomBigMushroom(Random rand, boolean canHuge) {
			return rand.nextFloat() < 0.3 ? canHuge ? new WorldGenBigGlowshroom(rand)
					: new WorldGenSmallGlowshroom(rand) : rand.nextFloat() < 0.4 ? canHuge ?
					new WorldGenBigFBMushroom(rand) : new WorldGenSmallFBMushroom(rand) : new WorldGenBigMushroom();
		}
		
		public WorldGenAbstractTree getRandomTreeFeature(Random rand, boolean canHuge) {
			return canHuge ? rand.nextFloat() < 0.2 ? new WorldGenCanopyTree(false) : new WorldGenBigRedOakTree(false, true) :
					rand.nextFloat() < 0.3 ? new WorldGenRedOakTree(false, true) : new WorldGenGoldenBirchTree(false, false, true);
		}
		
	}
		
}
