package net.smileycorp.fbiomes.common.world.biomes;

import java.util.Random;

import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.biome.Biome.FlowerEntry;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.fbiomes.common.blocks.BlockBrambleBush;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.world.biomes.BiomePrairie.BiomePrarieDecorator;
import net.smileycorp.fbiomes.common.world.gen.features.WorldGenBrambles;

public class BiomeFloatingMountain extends Biome {

	public BiomeFloatingMountain() {
		super(new BiomeProperties("Floating Mountain").setBaseHeight(-1.4F).setHeightVariation(0.45F));
		topBlock=Blocks.GRASS.getDefaultState();
		fillerBlock=Blocks.DIRT.getDefaultState();
		setRegistryName("fbiomes:FloatingMountain");
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
	
	 @SideOnly(Side.CLIENT)
	 public int getFoliageColorAtPos(BlockPos pos) {
		 return 0x4ACE9B;
	 }
	
	@Override
    public BiomeDecorator createBiomeDecorator(){
        return getModdedBiomeDecorator(new BiomeFloatingMountainDecorator());
    }
	
	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		int seaLevel = worldIn.getSeaLevel();
        IBlockState topBlock = this.topBlock;
        IBlockState fillerBlock = this.fillerBlock;
        int j = -1;
        int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        //if (rand.nextInt(20)==1) System.out.println("at ("+x+", "+z+") noise = " + noiseVal + ", and k = " + k);
        int chunkX = x & 15;
        int chunkZ = z & 15;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        
        int craterHeight = -1;
        
        for (int i = 45; i <= 255; i++){
        	IBlockState state = chunkPrimerIn.getBlockState(chunkZ, i, chunkX);
        	if (state.getMaterial()==Material.AIR || state.getMaterial()==Material.WATER){
        		craterHeight = i;
        		//System.out.print(i+", ");
        		break;
        	}
        }
        
        for (int primerY = 255; primerY >= 0; --primerY)
        {
            if (primerY <= rand.nextInt(5))
            {
                chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, BEDROCK);
            }
            else
            {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(chunkZ, primerY, chunkX);

                if (iblockstate2.getMaterial() == Material.AIR)
                {
                    j = -1;
                    if (primerY==147-Math.ceil(noiseVal*2.5)) {
                    	chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, topBlock);
                    }
                    else if (primerY>=144-Math.ceil(noiseVal*2.5)&&primerY<147-Math.ceil(noiseVal*2.5)) {
                    	chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, fillerBlock);
                    }
                    else if (primerY<144-Math.ceil(noiseVal*2.5)&&primerY>craterHeight+72-Math.ceil(noiseVal*2.5)) {
	                 	chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, STONE);
                    }
                    else if (primerY<144-Math.ceil(noiseVal*2.5)&&primerY==craterHeight+71-Math.ceil(noiseVal*2.5)){
                    	if (rand.nextInt(25)==1) {
                    		chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, FBiomesBlocks.LOOSE_ROCK.getDefaultState());
                    	} else {
                    		chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, STONE);
                    	}
                    }
                    else if (primerY<144-Math.ceil(noiseVal*2.5)&&primerY==craterHeight+70-Math.ceil(noiseVal*2.5)){
                    	if (rand.nextInt(18)==1) {
                    		chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, FBiomesBlocks.LOOSE_ROCK.getDefaultState());
                    	} else {
                    		chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, STONE);
                    	}
                    }
                    else if (primerY<144-Math.ceil(noiseVal*2.5)&&primerY==craterHeight+69-Math.ceil(noiseVal*2.5)){
                    	if (rand.nextInt(12)==1) {
                    		chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, FBiomesBlocks.LOOSE_ROCK.getDefaultState());
                    	} else {
                    		chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, STONE);
                    	}
                    }
                }
                else if (iblockstate2.getBlock() == Blocks.STONE)
                {
                    if (j == -1)
                    {
                    
                        if (k <= 0)
                        {
                            topBlock = AIR;
                            fillerBlock = STONE;
                        }
                        else if (primerY >= seaLevel - 4 && primerY <= seaLevel + 1)
                        {
                            topBlock = this.topBlock;
                            fillerBlock = this.fillerBlock;
                        }

                        if (primerY < seaLevel && (topBlock == null || topBlock.getMaterial() == Material.AIR))
                        {
                            if (this.getTemperature(pos.setPos(x, primerY, z)) < 0.15F)
                            {
                                topBlock = ICE;
                            }
                            else
                            {
                                topBlock = WATER;
                            }
                        }

                        j = k;

                        if (primerY >= seaLevel - 1)
                        {
                            chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, topBlock);
                        }
                        else if (primerY < seaLevel - 7 - k)
                        {
                            topBlock = AIR;
                            fillerBlock = STONE;
                            chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, GRAVEL);
                        }
                        else
                        {
                            chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, fillerBlock);
                        }
                    }
                    else if (j > 0)
                    {
                        --j;
                        chunkPrimerIn.setBlockState(chunkZ, primerY, chunkX, fillerBlock);

                        if (j == 0 && fillerBlock.getBlock() == Blocks.SAND && k > 1)
                        {
                            j = rand.nextInt(4) + Math.max(0, primerY - 63);
                            fillerBlock = fillerBlock.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
                        }
                    }
                }
            }
        }
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
		
		            if (treeGen.generate(worldIn, random, blockpos)){
		                treeGen.generateSaplings(worldIn, random, blockpos);
		            }
		        }
	        }
    	}
	
    }
}
