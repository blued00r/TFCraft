package TFC.WorldGen.GenLayers;

import TFC.WorldGen.TFCBiome;
import net.minecraft.src.GenLayer;
import net.minecraft.src.IntCache;

public class GenLayerRiverMixTFC extends GenLayerTFC
{
	private GenLayer biomePatternGeneratorChain;
	private GenLayer riverPatternGeneratorChain;

	public GenLayerRiverMixTFC(long par1, GenLayer par3GenLayer, GenLayer par4GenLayer)
	{
		super(par1);
		this.biomePatternGeneratorChain = par3GenLayer;
		this.riverPatternGeneratorChain = par4GenLayer;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
	 * amounts, or biomeList[] indices based on the particular GenLayer subclass.
	 */
	public int[] getInts(int par1, int par2, int par3, int par4)
	{
		int[] layerBiomes = this.biomePatternGeneratorChain.getInts(par1, par2, par3, par4);
		int[] layerRivers = this.riverPatternGeneratorChain.getInts(par1, par2, par3, par4);
		int[] layerOut = IntCache.getIntCache(par3 * par4);

		for (int var8 = 0; var8 < par3 * par4; ++var8)
		{
			int b = layerBiomes[var8];
			int r = layerRivers[var8];

			if (layerBiomes[var8] == TFCBiome.ocean.biomeID || layerBiomes[var8] == -1)
            {
                layerOut[var8] = TFCBiome.ocean.biomeID;
            }
            else if (layerRivers[var8] >= 0)
            {
                layerOut[var8] = TFCBiome.river.biomeID;
			}
			else
			{
				layerOut[var8] = layerBiomes[var8];
			}

			//            if(layerOut[var8] == 7)
				//            	System.out.println("Made a Normal River");
		}

		return layerOut;
	}

	/**
	 * Initialize layer's local worldGenSeed based on its own baseSeed and the world's global seed (passed in as an
	 * argument).
	 */
	public void initWorldGenSeed(long par1)
	{
		this.biomePatternGeneratorChain.initWorldGenSeed(par1);
		this.riverPatternGeneratorChain.initWorldGenSeed(par1);
		super.initWorldGenSeed(par1);
	}
}
