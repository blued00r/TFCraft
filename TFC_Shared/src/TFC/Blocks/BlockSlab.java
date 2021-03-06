package TFC.Blocks;

import java.util.ArrayList;
import java.util.Random;

import TFC.TFCBlocks;
import TFC.Core.Helper;
import TFC.Core.TFC_Settings;
import TFC.Core.TFC_Core;
import TFC.Core.Player.PlayerInfo;
import TFC.Core.Player.PlayerManagerTFC;
import TFC.Items.ItemChisel;
import TFC.Items.ItemHammer;
import TFC.TileEntities.TileEntityPartial;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class BlockSlab extends BlockPartial
{
	public BlockSlab(int par1)
	{
		super(par1, Material.rock);
	}

	@Override
	public int getRenderType()
	{
		return TFCBlocks.slabRenderId;
	}

	@Override
	public boolean isLadder(World world, int x, int y, int z) 
	{
		TileEntityPartial te = (TileEntityPartial) world.getBlockTileEntity(x, y, z);
		if(10 - (getTopChiselLevel(te.extraData) + getBottomChiselLevel(te.extraData)) < 3)
			return true;

		return false;
	}

	public static int getTopChiselLevel(long data)
	{
		return (int) ((data >> 16) & 0xf);
	}

	public static int getBottomChiselLevel(long data)
	{
		return (int) ((data >> 4) & 0xf);
	}

	public static int getPosXChiselLevel(long data)
	{
		return (int) ((data >> 12) & 0xf);
	}

	public static int getNegXChiselLevel(long data)
	{
		return (int) ((data) & 0xf);
	}

	public static int getPosZChiselLevel(long data)
	{
		return (int) ((data >> 20) & 0xf);
	}

	public static int getNegZChiselLevel(long data)
	{
		return (int) ((data >> 8) & 0xf);
	}

	/**
	 * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
	 */
	 @Override
	 public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)  
	 {
		 boolean hasHammer = false;
		 for(int i = 0; i < 9;i++)
		 {
			 if(entityplayer.inventory.mainInventory[i] != null && entityplayer.inventory.mainInventory[i].getItem() instanceof ItemHammer)
				 hasHammer = true;
		 }
		 if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() instanceof ItemChisel && hasHammer && !world.isRemote)
		 {
			 MovingObjectPosition objectMouseOver = Helper.getMouseOverObject(entityplayer, world);
			 if(objectMouseOver == null) {
				 return false;
			 }       
			 int side = objectMouseOver.sideHit;

			 int id = world.getBlockId(x, y, z);
			 byte meta = (byte) world.getBlockMetadata(x, y, z);

			 int mode = 0;
			 if(!world.isRemote)
			 {
				 PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(entityplayer);

				 if(pi!=null) mode = pi.ChiselMode;
			 }
			 else
	             mode = PlayerManagerTFC.getInstance().getClientPlayer().ChiselMode;

			 if(mode == 2)
			 {
				 ItemChisel.CreateSlab(world, x, y, z, id, meta, side);
				 return true;
			 }
			 else if(mode == 3)
			 {
				 ItemChisel.CreateDetailed(world, x, y, z, id, meta, side, par7, par8, par9);
				 return true;
			 }
		 }
		 return false;
	 }

	 /**
	  * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	  * cleared to be reused)
	  */
	 @Override
	 public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	 {
		 TileEntityPartial te = (TileEntityPartial) world.getBlockTileEntity(i, j, k);
		 int md = world.getBlockMetadata(i, j, k);
		 if(te != null)
		 {
			 short type = te.TypeID;

			 if (type <= 0)
				 return super.getCollisionBoundingBoxFromPool(world, i, j, k);

			 byte extraX = (byte) ((te.extraData) & 0xf);
			 byte extraY = (byte) ((te.extraData >> 4) & 0xf);
			 byte extraZ = (byte) ((te.extraData >> 8) & 0xf);
			 byte extraX2 = (byte) ((te.extraData >> 12) & 0xf);
			 byte extraY2 = (byte) ((te.extraData >> 16) & 0xf);
			 byte extraZ2 = (byte) ((te.extraData >> 20) & 0xf);

			 return AxisAlignedBB.getBoundingBox(i + (0.1F * extraX), j + (0.1F * extraY),  k + (0.1F * extraZ), i + (1 - (0.1F * extraX2)), j + (1 - (0.1F * extraY2)), k + (1 - (0.1F * extraZ2)));
		 }
		 return AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1, k + 1);
	 }

	 /**
	  * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	  * cleared to be reused)
	  */
	 public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	 {
		 return getCollisionBoundingBoxFromPool(world,i,j,k);
	 }

	 public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int i, int j, int k) 
	 {
		 TileEntityPartial te = (TileEntityPartial) par1IBlockAccess.getBlockTileEntity(i, j, k);

		 long extraX = (te.extraData) & 0xf;
		 long extraY = (te.extraData >> 4) & 0xf;
		 long extraZ = (te.extraData >> 8) & 0xf;
		 long extraX2 = (te.extraData >> 12) & 0xf;
		 long extraY2 = (te.extraData >> 16) & 0xf;
		 long extraZ2 = (te.extraData >> 20) & 0xf;

		 setBlockBounds(0.0F+ (0.1F * extraX), 0.0F+ (0.1F * extraY), 0.0F+ (0.1F * extraZ), 1.0F-(0.1F * extraX2), 1-(0.1F * extraY2), 1.0F-(0.1F * extraZ2));
	 }

	 public void onBlockDestroyedByExplosion(World world, int i, int j, int k) 
	 {
		 if(!world.isRemote)
		 {
		 }
	 }

	 public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
	 {   

	 }

	 public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int i, int j, int k)
	 {
		 return true;
	 }
}
