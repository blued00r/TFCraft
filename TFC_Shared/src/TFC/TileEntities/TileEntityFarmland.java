package TFC.TileEntities;

import TFC.*;
import TFC.Blocks.BlockFarmland;
import TFC.Core.TFC_Time;
import TFC.Food.CropIndex;
import TFC.Food.CropManager;
import TFC.Handlers.PacketHandler;
import TFC.Items.ItemOre;
import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class TileEntityFarmland extends TileEntity
{
    public long nutrientTimer = -1;
    public int[] nutrients = {18000,18000,18000};

    public TileEntityFarmland()
    {
    }

    @Override
    public void updateEntity()
    {
        if(!worldObj.isRemote)
        {
            if(nutrientTimer <= 0)
                nutrientTimer = TFC_Time.totalHours();
            
            if(nutrientTimer < TFC_Time.totalHours())
            {
                CropIndex crop = null;
                float timeMultiplier = TFC_Time.daysInYear/360;
                int soilMax = (int) (25000 * timeMultiplier);
                int restoreAmount = 65;
                
                if((worldObj.getBlockId(xCoord, yCoord+1, zCoord) == Block.crops.blockID))
                {
                    crop = CropManager.getInstance().getCropFromId(((TileEntityCrop)worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord)).cropId);
                    
                    if((crop.cycleType != 0))
                    {
                        if(nutrients[0] < soilMax)
                            nutrients[0] += restoreAmount + crop.nutrientExtraRestore[0];
                    }
                    if((crop.cycleType != 1))
                    {
                        if(nutrients[1] < soilMax)
                            nutrients[1] += restoreAmount + crop.nutrientExtraRestore[1];
                    }
                    if((crop.cycleType != 2))
                    {
                        if(nutrients[2] < soilMax)
                            nutrients[2] += restoreAmount + crop.nutrientExtraRestore[2];
                    }
                }
                else
                {
                    if(nutrients[0] < soilMax)
                        nutrients[0] += restoreAmount;
                    if(nutrients[1] < soilMax)
                        nutrients[1] += restoreAmount;
                    if(nutrients[2] < soilMax)
                        nutrients[2] += restoreAmount;
                }

                nutrientTimer+=24;
                
//                if(BlockFarmland.isWaterNearby(worldObj, xCoord, yCoord, zCoord))
//                {
//                    waterSaturation += 1;
//                    if(waterSaturation > 30)
//                        waterSaturation = 30;
//                }
//                else if((worldObj.getBlockId(xCoord, yCoord+1, zCoord) == Block.crops.blockID) && crop != null)
//                {
//                    waterSaturation -= 1*crop.waterUsageMult;
//                }
//                
//                if(worldObj.isRaining() && worldObj.canBlockSeeTheSky(xCoord, yCoord+1, zCoord))
//                {
//                    waterSaturation += 3;
//                    if(waterSaturation > 30)
//                        waterSaturation = 30;
//                }
            }
        }
    }

    public void DrainNutrients(int type, float multiplier)
    {
        nutrients[type] -= 100*multiplier;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        
        nutrients = par1NBTTagCompound.getIntArray("nutrients");
        nutrientTimer = par1NBTTagCompound.getLong("nutrientTimer");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setIntArray("nutrients", nutrients);
        par1NBTTagCompound.setLong("nutrientTimer", nutrientTimer);    
    }
}
