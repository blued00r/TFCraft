package TFC.WorldGen.Biomes;

import java.util.Random;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenVines;
import net.minecraft.src.WorldGenerator;

import TFC.Entities.Mobs.EntityChickenTFC;
import TFC.WorldGen.BiomeDecoratorTFC;
import TFC.WorldGen.TFCBiome;
import TFC.WorldGen.Generators.WorldGenCustomHugeTrees;
import TFC.WorldGen.Generators.WorldGenCustomShortTrees;
import TFC.WorldGen.Generators.WorldGenCustomShrub;
import TFC.WorldGen.Generators.WorldGenCustomTallGrass;

public class BiomeGenJungleTFC extends TFCBiome
{
	public BiomeGenJungleTFC(int id)
	{
		super(id);
		((BiomeDecoratorTFC)this.theBiomeDecorator).treesPerChunk = 50;
		((BiomeDecoratorTFC)this.theBiomeDecorator).grassPerChunk = 25;
		((BiomeDecoratorTFC)this.theBiomeDecorator).flowersPerChunk = 4;
		this.spawnableMonsterList.add(new SpawnListEntry(EntityOcelot.class, 2, 1, 1));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityChickenTFC.class, 10, 4, 4));

	}

	public void decorate(World par1World, Random par2Random, int par3, int par4)
	{
		super.decorate(par1World, par2Random, par3, par4);
		WorldGenVines var5 = new WorldGenVines();

		for (int var6 = 0; var6 < 50; ++var6)
		{
			int var7 = par3 + par2Random.nextInt(16) + 8;
			byte var8 = 64;
			int var9 = par4 + par2Random.nextInt(16) + 8;
			var5.generate(par1World, par2Random, var7, var8, var9);
		}
	}

	public WorldGenerator func_48410_b(Random par1Random)
	{
		return par1Random.nextInt(4) == 0 ? new WorldGenCustomTallGrass(Block.tallGrass.blockID, 2) : new WorldGenCustomTallGrass(Block.tallGrass.blockID, 1);
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 */
	@Override
	public WorldGenerator getRandomWorldGenForTrees(Random par1Random, World world)
	{
		return (WorldGenerator)(par1Random.nextInt(10) == 0 ? new WorldGenCustomShortTrees(false,15) : par1Random.nextInt(2) == 0 ? new WorldGenCustomShrub(15, 15) : par1Random.nextInt(3) == 0 ? new WorldGenCustomHugeTrees(false, 10 + par1Random.nextInt(20), 15, 15) : new WorldGenCustomShortTrees(false, 15));
	}
	
	protected float getMonthTemp(int month)
    {
        switch(month)
        {
            case 11:
                return 0.9F;
            case 0:
                return 0.92F;
            case 1:
                return 0.94F;
            case 2:
                return 0.96F;
            case 3:
                return 0.98F; 
            case 4:
                return 1F;
            case 5:
                return 0.98F;
            case 6:
                return 0.96F;
            case 7:
                return 0.94F;
            case 8:
                return 0.92F;
            case 9:
                return 0.90F;
            case 10:
                return 0.88F;
            default:
                return 1F;
        }
    }
}
