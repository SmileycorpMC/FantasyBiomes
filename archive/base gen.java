@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		int seaLevel = worldIn.getSeaLevel();
        IBlockState topBlock = this.topBlock;
        IBlockState fillerBlock = this.fillerBlock;
        int j = -1;
        int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        if (rand.nextInt(20)==1) System.out.println("at ("+x+", "+z+") noise = " + noiseVal + ", and k = " + k);
        int chunkX = x & 15;
        int chunkZ = z & 15;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

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