package net.smileycorp.fbiomes.common.world.biomes;

import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.fbiomes.common.Constants;

import java.util.Random;

public class BiomeFloatingMountain extends Biome {
	
	protected NoiseGeneratorOctaves mountainnoise = null;
	protected World world = null;

	public BiomeFloatingMountain() {
		super(new BiomeProperties("Floating Mountain").setBaseHeight(-0.2F).setHeightVariation(0.1F));
		topBlock=Blocks.GRASS.getDefaultState();
		setRegistryName(Constants.loc("floating_mountain"));
		flowers.clear();
	}
	
	@Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return getFoliageColorAtPos(pos);
    }
	
	 @Override
	@SideOnly(Side.CLIENT)
	 public int getFoliageColorAtPos(BlockPos pos) {
		 return 0x4ACE9B;
	 }
	
	@Override
    public BiomeDecorator createBiomeDecorator(){
        return getModdedBiomeDecorator(new BiomeFloatingMountainDecorator());
    }
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunk, int x, int z, double noise) {
		if (this.world != world) {
			mountainnoise = new NoiseGeneratorOctaves(new Random(new Random(world.getSeed()).nextLong()), 12);
			this.world = world;
		}
		double[] mnoise = mountainnoise.generateNoiseOctaves(new double[] {noise, noise}, x, z, 1, 1, 3, 3, 1);
		int i = x & 15;
        int k = z & 15;
		int h = (int) Math.round(mnoise[0]/20);
		int h0 = 80 - (int)(noise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int h1 = Math.max(120 + h, 120 - h);
		System.out.println(h1 + ", " + h0 + " at " + x + ", " + z);
		for (int j = 255; j >= 0; --j) {
			if (j > h0 && j < h1) {
				chunk.setBlockState(i, j, k, Blocks.STONE.getDefaultState());
			}
		}
		super.genTerrainBlocks(world, rand, chunk, x, z, noise);
	}
	
	
	public class BiomeFloatingMountainDecorator extends BiomeDecorator {
		
		BiomeFloatingMountainDecorator(){
			waterlilyPerChunk = 0;
			treesPerChunk = 0;
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
	
    }
}
