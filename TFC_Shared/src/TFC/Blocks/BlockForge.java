package TFC.Blocks;

import java.util.Random;

import TFC.TFCBlocks;
import TFC.TFCItems;
import TFC.TerraFirmaCraft;
import TFC.Items.ItemFirestarter;
import TFC.TileEntities.TileEntityTerraFirepit;
import TFC.TileEntities.TileEntityTerraForge;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockForge extends BlockContainer
{

	private int meta;
	private int xCoord;
	private int yCoord;
	private int zCoord;

	public BlockForge(int i, int tex)
	{
		super(i, Material.sand);
		this.blockIndexInTexture = tex;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1F, 0.9F, 1F);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ)
	{
		meta = world.getBlockMetadata(i, j, k);
		xCoord = i;
		yCoord = j;
		zCoord = k;
		ItemStack equippedItem = entityplayer.getCurrentEquippedItem();
		int itemid;
		if(equippedItem != null)
		{
			itemid = equippedItem.itemID;
		} else {
			itemid = 0;
		}

		if(world.isRemote)
		{
			return true;
		} 
		else if(equippedItem != null && equippedItem.getItem() instanceof ItemFirestarter)
		{
			if((TileEntityTerraForge)world.getBlockTileEntity(i, j, k) != null)
			{
				TileEntityTerraForge tileentityforge;
				tileentityforge = (TileEntityTerraForge)world.getBlockTileEntity(i, j, k);
				if(tileentityforge.fireTemperature < 210 && tileentityforge.fireItemStacks[7] != null && tileentityforge.isValid)
				{
					tileentityforge.fireTemperature = 300;
					int ss = entityplayer.inventory.getCurrentItem().stackSize;
					int dam = entityplayer.inventory.getCurrentItem().getItemDamage()+1;

					if(dam >= entityplayer.getCurrentEquippedItem().getItem().getMaxDamage())
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, 
								null);
					}
					else
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, 
								new ItemStack(entityplayer.getCurrentEquippedItem().getItem(),ss,dam));
					}
					world.setBlockMetadata(i, j, k, 2);
				}
			}
			return true;
		}
		else
		{
			if((TileEntityTerraForge)world.getBlockTileEntity(i, j, k)!=null)
			{
				TileEntityTerraForge tileentityforge;
				tileentityforge = (TileEntityTerraForge)world.getBlockTileEntity(i, j, k);
				ItemStack is =entityplayer.getCurrentEquippedItem();

				if(tileentityforge.isValid)
				{
					entityplayer.openGui(TerraFirmaCraft.instance, 23, world, i, j, k);
					//ModLoader.openGUI(entityplayer, new GuiTerraForge(entityplayer.inventory, tileentityforge));
				}
			}
			return true;
		}
	}
	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j)
	{
		return blockIndexInTexture+j;
	}

	public boolean getIsFireLit(int i)
	{
		return (i & 1) != 0;
	}

	@Override
	public int getRenderType()
	{
		return TFCBlocks.terraForgeRenderId;
	}

	@Override
	public String getTextureFile() {

		return "/bioxx/terrablocks.png";
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
	{		
		Eject(world,i,j,k);
	}
	
	@Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    	Eject(par1World,par2,par3,par4);
    }
	
	public void Eject(World par1World, int par2, int par3, int par4)
    {
        if((TileEntityTerraForge)par1World.getBlockTileEntity(par2, par3, par4)!=null)
        {
        	TileEntityTerraForge tileentityanvil;
            tileentityanvil = (TileEntityTerraForge)par1World.getBlockTileEntity(par2, par3, par4);
            tileentityanvil.ejectContents();
            par1World.removeBlockTileEntity(par2, par3, par4);
        }
    }
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int l)
	{
		if((world.getBlockMaterial(x+1, y, z) == Material.rock && world.getBlockMaterial(x-1, y, z) == Material.rock && 
				world.getBlockMaterial(x, y, z+1) == Material.rock && world.getBlockMaterial(x, y, z-1) == Material.rock &&
				world.isBlockNormalCube(x, y, z) && ((world.isBlockNormalCube(x+1, y, z) && world.isBlockNormalCube(x-1, y, z) && 
						world.isBlockNormalCube(x, y, z+1) && world.isBlockNormalCube(x, y, z-1)) || (ItemFirestarter.checkSlabsAround(world, x, y, z)))))
		{
			((TileEntityTerraForge)world.getBlockTileEntity(x, y, z)).ejectContents();
			world.setBlock(x, y, z, 0);
		}
		else
		{
			int numAirBlocks = 0;
			for (int i = -1; i < 2; i++)
			{
				for (int j = 0; j < 2; j++)
				{
					for (int k = -1; k < 2; k++)
					{
						if(world.getBlockId(xCoord+i, yCoord+j, zCoord+k) == 0) {
							numAirBlocks++;
						}
					}
				}
			}
			if(world.getBlockTileEntity(x, y, z) != null)
			{
				((TileEntityTerraForge)world.getBlockTileEntity(x, y, z)).setNumAirBlocks(numAirBlocks);
				((TileEntityTerraForge)world.getBlockTileEntity(x, y, z)).isValid = false;
			}
		}
	}
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (this.blockID == TFCBlocks.Forge.blockID)
		{
			return;
		}
		else
		{
			float f = (float)i + 0.5F;
			float f1 = (float)j + 0.9F + random.nextFloat() * 6F / 16F;
			float f2 = (float)k + 0.5F;
			float f3 = 0.52F;
			float f4 = random.nextFloat() * 0.6F;
			float f5 = random.nextFloat() * -0.6F;
			float f6 = random.nextFloat() * -0.6F;
			world.spawnParticle("smoke", f+f4 - 0.3F, f1,  f2 + f5 + 0.3F, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", f+f4 - 0.3F, f1,  f2 + f5 + 0.3F, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("smoke", f+f5 + 0.3F , f1, f2 + f4 - 0.3F, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", f+f5 + 0.3F , f1, f2 + f4 - 0.3F, 0.0D, 0.0D, 0.0D);
			if (((TileEntityTerraForge)world.getBlockTileEntity(i, j, k)).fireTemperature > 550)
			{
				world.spawnParticle("flame", f+f5 + 0.3F , f1, f2 + f6 + 0.2F, 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", f+f4 - 0.3F , f1, f2 + f6 + 0.1F, 0.0D, 0.0D, 0.0D);
			}
		}
	}
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) 
    {
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 0)
			return 0;
		else if(meta == 1)
			return 15;
		else
			return 10;
    }

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	 * cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		return AxisAlignedBB.getBoundingBox((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ);
	}

	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		return AxisAlignedBB.getBoundingBox((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		// TODO Auto-generated method stub
		return new TileEntityTerraForge();
	}


}
