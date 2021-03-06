package TFC.WorldGen.Generators;

import java.util.Random;

import TFC.*;

import net.minecraft.src.Block;
import net.minecraft.src.BlockFlower;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public class WorldGenCustomFlowers extends WorldGenerator
{
	/** The ID of the plant block used in this plant generator. */
	private int plantBlockId;

	public WorldGenCustomFlowers(int par1)
	{
		this.plantBlockId = par1;
	}

	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
	{
		for (int var6 = 0; var6 < 2; ++var6)
		{
			int var7 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int var8 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int var9 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);

			if (par1World.isAirBlock(var7, var8, var9) && ((BlockFlower)Block.blocksList[this.plantBlockId]).canBlockStay(par1World, var7, var8, var9))
			{
				par1World.setBlock(var7, var8, var9, this.plantBlockId);
			}
		}

		return true;
	}
}
