package TFC.WorldGen;

import java.util.List;
import java.util.Random;

import TFC.TFCBlocks;
import TFC.TerraFirmaCraft;
import TFC.Core.TFC_Climate;
import TFC.Core.TFC_Settings;
import TFC.Core.TFC_Core;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.Entity;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Vec3;
import net.minecraft.src.WorldChunkManager;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;
import net.minecraftforge.client.SkyProvider;

public class TFCProvider extends WorldProvider
{	
	public SkyProvider skyprovider;
	
	@Override
	protected void registerWorldChunkManager()
	{
		this.worldChunkMgr = terrainType.getChunkManager(this.worldObj);
		//TFC_Core.SetupWorld(this.worldObj);
		//TFC_Game.registerAnvilRecipes(this.worldObj.rand, this.worldObj);
		TerraFirmaCraft.proxy.registerSkyProvider(this);
		TFC_Climate.manager = (TFCWorldChunkManager) worldChunkMgr;
		TFC_Climate.worldObj = worldObj;
	}

	@Override
	/**
	 * Will check if the x, z position specified is alright to be set as the map spawn point
	 */
	public boolean canCoordinateBeSpawn(int par1, int par2)
	{
		int var3 = this.worldObj.getFirstUncoveredBlock(par1, par2);
		//return var3 == Block.grass.blockID;
		return TFC_Core.isGrass(var3);
	}

	@Override
	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
	 */
	public float calculateCelestialAngle(long par1, float par3)
	{
		int var4 = (int)(par1 % (long)TFC_Settings.dayLength);
		float var5 = ((float)var4 + par3) / (float)TFC_Settings.dayLength - 0.25F;

		if (var5 < 0.0F)
		{
			++var5;
		}

		if (var5 > 1.0F)
		{
			--var5;
		}

		float var6 = var5;
		var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		return var5;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getMoonPhase(long par1, float par3)
	{
		return (int)(par1 / (long)TFC_Settings.dayLength) % 8;
	}

	@SideOnly(Side.CLIENT)

	/**
	 * Calculates the color for the skybox
	 */
	public Vec3 getSkyColor(Entity par1Entity, float par2)
	{
		float var3 = worldObj.getCelestialAngle(par2);
		float var4 = MathHelper.cos(var3 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

		if (var4 < 0.0F)
		{
			var4 = 0.0F;
		}

		if (var4 > 1.0F)
		{
			var4 = 1.0F;
		}

		int X = MathHelper.floor_double(par1Entity.posX);
		int Y = MathHelper.floor_double(par1Entity.posY);
		int Z = MathHelper.floor_double(par1Entity.posZ);

		float var8 = TFC_Climate.getHeightAdjustedTemp(X, Y, Z);

		//Adjust to 0-1
		var8 = (var8+35)/(TFC_Climate.getMaxTemperature() + 35);

		int var9 = ((TFCSkyProvider)this.getSkyProvider()).getSkyColorByTemp(var8);

		float var10 = (float)(var9 >> 16 & 255) / 255.0F;
		float var11 = (float)(var9 >> 8 & 255) / 255.0F;
		float var12 = (float)(var9 & 255) / 255.0F;
		var10 *= var4;
		var11 *= var4;
		var12 *= var4;
		float var13 = worldObj.getRainStrength(par2);
		float var14;
		float var15;

		if (var13 > 0.0F)
		{
			var14 = (var10 * 0.3F + var11 * 0.59F + var12 * 0.11F) * 0.6F;
			var15 = 1.0F - var13 * 0.75F;
			var10 = var10 * var15 + var14 * (1.0F - var15);
			var11 = var11 * var15 + var14 * (1.0F - var15);
			var12 = var12 * var15 + var14 * (1.0F - var15);
		}

		var14 = worldObj.getWeightedThunderStrength(par2);

		if (var14 > 0.0F)
		{
			var15 = (var10 * 0.3F + var11 * 0.59F + var12 * 0.11F) * 0.2F;
			float var16 = 1.0F - var14 * 0.75F;
			var10 = var10 * var16 + var15 * (1.0F - var16);
			var11 = var11 * var16 + var15 * (1.0F - var16);
			var12 = var12 * var16 + var15 * (1.0F - var16);
		}

		if (worldObj.lightningFlash > 0)
		{
			var15 = (float)worldObj.lightningFlash - par2;

			if (var15 > 1.0F)
			{
				var15 = 1.0F;
			}

			var15 *= 0.45F;
			var10 = var10 * (1.0F - var15) + 0.8F * var15;
			var11 = var11 * (1.0F - var15) + 0.8F * var15;
			var12 = var12 * (1.0F - var15) + 1.0F * var15;
		}

		return Vec3.field_82592_a.getVecFromPool((double)var10, (double)var11, (double)var12);
	}

	@Override
	public float getCloudHeight()
	{
		return 256.0F;
	}

	@SideOnly(Side.CLIENT)
	public SkyProvider getSkyProvider()
	{
		return skyprovider;
	}

	@SideOnly(Side.CLIENT)
	public void setSkyProvider(SkyProvider skyProvider)
	{
		skyprovider = skyProvider;
	}

	public void createSpawnPosition()
	{
		if (!canRespawnHere())
		{
			worldObj.getWorldInfo().setSpawnPosition(0, getAverageGroundLevel(), 0);
		}
		else
		{

			TFCWorldChunkManager var2 = (TFCWorldChunkManager) worldChunkMgr;
			List var3 = var2.getBiomesToSpawnIn();
			long seed = worldObj.getWorldInfo().getSeed();
			Random var4 = new Random(seed);

			ChunkPosition var5 = null;
			int xOffset = 0;
			int var6 = 0;
			int var7 = getAverageGroundLevel();
			int var8 = 10000;
			int startingZ = 3000 + var4.nextInt(12000);

			while(var5 == null)
			{
				var5 = var2.findBiomePosition(xOffset, -startingZ, 64, var3, var4);

				if (var5 != null)
				{
					var6 = var5.x;
					var8 = var5.z;
				}
				else
				{
					xOffset += 512;
					//System.out.println("Unable to find spawn biome");
				}
			}

			int var9 = 0;

			while (!canCoordinateBeSpawn(var6, var8))
			{
				var6 += var4.nextInt(64) - var4.nextInt(64);
				var8 += var4.nextInt(64) - var4.nextInt(64);
				++var9;

				if (var9 == 1000)
				{
					break;
				}
			}

			worldObj.getWorldInfo().setSpawnPosition(var6, var7, var8);
		}
	}
	
	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
    {
		if(TFC_Climate.getHeightAdjustedTemp(x, y, z) <= 0 && (worldObj.getBlockMaterial(x, y, z) == Material.water || worldObj.getBlockMaterial(x, y, z) == Material.ice))
			return true;
		return false;
    }

    public boolean canSnowAt(int x, int y, int z)
    {
    	if(TFC_Climate.getHeightAdjustedTemp(x, y, z) <= 0)
			return true;
		return false;
    }

	@Override
	public String getDimensionName() {
		// TODO Auto-generated method stub
		return "DEFAULT";
	}

}
