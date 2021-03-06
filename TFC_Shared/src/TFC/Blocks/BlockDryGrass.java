package TFC.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import TFC.*;
import TFC.Core.ColorizerGrassTFC;
import TFC.Core.TFC_Climate;
import TFC.Core.TFC_Core;
import TFC.Core.TFC_Settings;
import TFC.TileEntities.TileEntityPartial;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;

public class BlockDryGrass extends BlockGrass
{
    public BlockDryGrass(int par1, int par2)
    {
        super(par1, par2);
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    @Override
    public int getBlockTexture(IBlockAccess access, int xCoord, int yCoord, int zCoord, int par5)
    {
    	Block blk = Block.blocksList[TFC_Core.getTypeForDirt(access.getBlockMetadata(xCoord, yCoord, zCoord))];
    	
        if (par5 == 1)//top
        {
            return 252;
        }
        else
        {
            return this.blockIndexInTexture + access.getBlockMetadata(xCoord, yCoord, zCoord);
        }
    }
}
