package TFC.Blocks;

import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import TFC.TerraFirmaCraft;
import TFC.TileEntities.TileEntityTerraMetallurgy;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockMetallurgy extends BlockContainer
{
	private int meta;
	private int xCoord;
	private int yCoord;
	private int zCoord;

	public BlockMetallurgy(int i)
	{
		super(i, Material.rock);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	@SideOnly(Side.CLIENT)
    @Override
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List list)
	{
		list.add(new ItemStack(this,1,0));
	}

	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ)
	{
		meta = world.getBlockMetadata(i, j, k);
		xCoord = i;
		yCoord = j;
		zCoord = k;
		//Minecraft mc = ModLoader.getMinecraftInstance();
		ItemStack equippedItem = entityplayer.getCurrentEquippedItem();
		int itemid;
		if(equippedItem != null)
		{
			itemid = entityplayer.getCurrentEquippedItem().itemID;
		} else {
			itemid = 0;
		}

		if(world.isRemote)
		{
			return true;
		} 
		else
		{
			if((TileEntityTerraMetallurgy)world.getBlockTileEntity(i, j, k)!=null)
			{
				TileEntityTerraMetallurgy tileentityanvil;
				tileentityanvil = (TileEntityTerraMetallurgy)world.getBlockTileEntity(i, j, k);
				ItemStack is = entityplayer.getCurrentEquippedItem();

				entityplayer.openGui(TerraFirmaCraft.instance, 24, world, i, j, k);
				//ModLoader.openGUI(entityplayer, new GuiTerraMetallurgy(entityplayer.inventory, tileentityanvil, world));
			}
			return true;
		}
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j)
	{
		if(i == 1) {
			return 68;
		}
		return 69;
	}

	public String getItemNameIS(ItemStack itemstack) 
	{
		String s = "terraMetallurgy";
		return s;
	}

	@Override
	public String getTextureFile() {

		return "/bioxx/terrablocks.png";
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		// TODO Auto-generated method stub
		return new TileEntityTerraMetallurgy();
	}
}
