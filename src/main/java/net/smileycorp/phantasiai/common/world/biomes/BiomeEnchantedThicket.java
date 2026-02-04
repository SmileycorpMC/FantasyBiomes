package net.smileycorp.phantasiai.common.world.biomes;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
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
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.entities.EntityPixie;
import net.smileycorp.phantasiai.common.world.gen.WorldGenDisc;
import net.smileycorp.phantasiai.common.world.gen.features.enchantedthicket.WorldGenHollowLog;
import net.smileycorp.phantasiai.common.world.gen.features.enchantedthicket.WorldGenLog;
import net.smileycorp.phantasiai.common.world.gen.features.enchantedthicket.WorldGenMysticStump;
import net.smileycorp.phantasiai.common.world.gen.features.enchantedthicket.WorldGenWitchCottage;
import net.smileycorp.phantasiai.common.world.gen.mushroom.*;
import net.smileycorp.phantasiai.common.world.gen.tree.WorldGenBigRedOakTree;
import net.smileycorp.phantasiai.common.world.gen.tree.WorldGenGoldenBirchTree;
import net.smileycorp.phantasiai.common.world.gen.tree.WorldGenOrantikkuTree;
import net.smileycorp.phantasiai.common.world.gen.tree.WorldGenRedOakTree;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

public class BiomeEnchantedThicket extends Biome {
	
	public BiomeEnchantedThicket() {
		super(new BiomeProperties("Enchanted Thicket").setBaseHeight(0.3F).setHeightVariation(0.0F));
		topBlock = Blocks.GRASS.getDefaultState();
		fillerBlock = Blocks.DIRT.getDefaultState();
		setRegistryName(Constants.loc("enchanted_thicket"));
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPixie.class, 5, 2, 6));
		//spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySheep.class, 3, 1, 4));
		//spawnableCreatureList.add(new Biome.SpawnListEntry(EntityMooshroom.class, 1, 1, 2));
	}
	
	/*@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noise) {
		double grassNoise = PODZOL_NOISE.getValue((double) x / 100d, (double) z / 100d);
		topBlock = grassNoise > 0 ? Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL)
				: Blocks.GRASS.getDefaultState();
		generateBiomeTerrain(world, rand, chunkPrimerIn, x, z, noise);
	}*/
	
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return 0x4D602F;
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
		
		private final WorldGenDisc podzol = new WorldGenDisc(8,
				Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL), Blocks.GRASS.getDefaultState());
		
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
				generateOres(world, rand);
				//podzol
				for (int i = 0; i < 6; ++i) podzol.generate(world, rand,
						world.getTopSolidOrLiquidBlock(pos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8)));
				WorldGenerator SHROOM_GENERATOR = new WorldGenShroom();
				WorldGenOrantikkuTree CANOPY_TREE_GENERATOR = new WorldGenOrantikkuTree(false, true);
				Set<BlockPos> canopy_trees = Sets.newHashSet();
				//canopy trees
				BlockPos treepos = getHeight(world, pos.add(9 + rand.nextInt(3), 0, 9 + rand.nextInt(3)));
				if (TerrainGen.decorate(world, rand, new ChunkPos(treepos), treepos, EventType.TREE)
						&& CANOPY_TREE_GENERATOR.canGenerate(world, treepos)) CANOPY_TREE_GENERATOR.generate(world, rand, treepos);
				//features
				if (rand.nextBoolean()) {
					BlockPos pos1 = getHeight(world, pos.add(rand.nextInt(16) - rand.nextInt(16), 0, rand.nextInt(16) - rand.nextInt(16)));
					WorldGenerator feature = getRandomFeature(world, pos1, rand, canopy_trees);
					if (feature != null) feature.generate(world, rand, pos1);
				}
				//trees and big mushrooms
				for (int i = 0; i < 2; ++i) for (int j = 0; j < 2; ++j) {
					BlockPos blockpos =  getHeight(world, pos.add(i * 8 + 5 + 8 + rand.nextInt(3), 0, j * 8 + 5 + 8 + rand.nextInt(3)));
                    if (rand.nextInt(10) < 3) genBigMushroom(world, rand, blockpos, canopy_trees);
                    else genTree(world, rand, blockpos, canopy_trees);
                }
				//mushrooms
				for (int i = 0; i < 128; ++i) {
					if (TerrainGen.decorate(world, rand, new ChunkPos(pos), pos, EventType.SHROOM)) {
						int j = rand.nextInt(16) + 8;
						int k = rand.nextInt(16) + 8;
						int l = rand.nextInt(getHeight(world, pos.add(j, 0, k)).getY() + 32);
						SHROOM_GENERATOR.generate(world, rand, pos.add(j, l, k));
					}
				}
				//grass
				for (int i = 0; i < 128; ++i) {
					if (rand.nextInt(5) == 0 && TerrainGen.decorate(world, rand, new ChunkPos(pos), pos, EventType.GRASS)) {
						int j = rand.nextInt(16) + 8;
						int k = rand.nextInt(16) + 8;
						int l = rand.nextInt(getHeight(world, pos.add(j, 0, k)).getY() + 32);
						if (rand.nextInt(3) == 1) DOUBLE_PLANT_GENERATOR.generate(world, rand, pos.add(j, l, k));
						else getRandomWorldGenForGrass(rand).generate(world, rand, pos.add(j, l, k));
					}
				}
				this.decorating = false;
			}
		}

		private BlockPos getHeight(World world, BlockPos pos) {
			int x = pos.getX();
			int z = pos.getZ();
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos);
			for (int i = world.getChunkFromChunkCoords(x >> 4, z >> 4).getHeightValue(x & 15, z & 15); i > 0; i--) {
				mutable.setY(i);
				Material material = world.getBlockState(mutable).getMaterial();
				if (material == Material.GROUND || material == Material.GRASS || material == STONE) return mutable.toImmutable().up();
			}
			return pos;
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
			getRandomBigMushroom(rand, rand.nextInt(3) == 0).generate(world, rand, pos);
		}
		
		private WorldGenerator getRandomFeature(World world, BlockPos pos, Random rand, Set<BlockPos> canopyTrees) {
			int r = rand.nextInt(15);
			if (r < 2) return new WorldGenWitchCottage();
			if (r < 5) return new WorldGenMysticStump();
			if (r < 9) return new WorldGenHollowLog();
			return new WorldGenLog();
		}
		
		private WorldGenerator getRandomBigMushroom(Random rand, boolean canHuge) {
			return rand.nextInt(10) < 3 ? canHuge ? new WorldGenHugeGlowshroom(rand)
					: new WorldGenBigGlowshroom(rand) : rand.nextInt(5) < 2 ? canHuge ?
					new WorldGenHugeFBMushroom(rand) : rand.nextInt(3) == 0 ? new WorldGenSmallToadstool(true) :
					new WorldGenBigFBMushroom(rand) : new WorldGenBigMushroom();
		}
		
		public WorldGenAbstractTree getRandomTreeFeature(Random rand, boolean canHuge) {
			return canHuge ? rand.nextInt(5) == 0 ? new WorldGenCanopyTree(false) : new WorldGenBigRedOakTree(false, true) :
					rand.nextInt(10) < 3 ? new WorldGenRedOakTree(false, true) : new WorldGenGoldenBirchTree(false, true);
		}
		
	}
		
}
