package TFC.Containers;

import TFC.*;
import TFC.TileEntities.TileEntityTerraSluice;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.World;

public class ContainerTerraSluice extends ContainerTFC
{
	private TileEntityTerraSluice sluice;
	private EntityPlayer player;


	public ContainerTerraSluice(InventoryPlayer inventoryplayer, TileEntityTerraSluice tileentitysluice, World world, int x, int y, int z)
	{
		sluice = tileentitysluice;
		player = inventoryplayer.player;
		addSlotToContainer(new SlotSluice(inventoryplayer.player, tileentitysluice, 0, 116, 16));
		addSlotToContainer(new SlotSluice(inventoryplayer.player, tileentitysluice, 1, 134, 16));
		addSlotToContainer(new SlotSluice(inventoryplayer.player, tileentitysluice, 2, 152, 16));
		addSlotToContainer(new SlotSluice(inventoryplayer.player, tileentitysluice, 3, 116, 34));
		addSlotToContainer(new SlotSluice(inventoryplayer.player, tileentitysluice, 4, 134, 34));
		addSlotToContainer(new SlotSluice(inventoryplayer.player, tileentitysluice, 5, 152, 34));
		addSlotToContainer(new SlotSluice(inventoryplayer.player, tileentitysluice, 6, 116, 52));
		addSlotToContainer(new SlotSluice(inventoryplayer.player, tileentitysluice, 7, 134, 52));
		addSlotToContainer(new SlotSluice(inventoryplayer.player, tileentitysluice, 8, 152, 52));
		for(int i = 0; i < 3; i++)
		{
			for(int k = 0; k < 9; k++)
			{
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
			}

		}

		for(int j = 0; j < 9; j++)
		{
			addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 142));
		}

	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}


	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i)
	{
		Slot slot = (Slot)inventorySlots.get(i);
		Slot slotpaper = (Slot)inventorySlots.get(1);
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			if(i <= 8)
			{
				if(!player.inventory.addItemStackToInventory(itemstack1.copy()))
				{
					return null;
				}
				slot.putStack(null);
			}
			if(itemstack1.stackSize == 0)
			{
				slot.putStack(null);
			} else
			{
				slot.onSlotChanged();
			}
		}
		return null;
	}
	
	private int soilamt = 0;
	private int progress = 0;
	@Override
	public void updateCraftingResults()
    {
        for (int var1 = 0; var1 < this.inventorySlots.size(); ++var1)
        {
            ItemStack var2 = ((Slot)this.inventorySlots.get(var1)).getStack();
            ItemStack var3 = (ItemStack)this.inventoryItemStacks.get(var1);

            if (!ItemStack.areItemStacksEqual(var3, var2))
            {
                var3 = var2 == null ? null : var2.copy();
                this.inventoryItemStacks.set(var1, var3);

                for (int var4 = 0; var4 < this.crafters.size(); ++var4)
                {
                    ((ICrafting)this.crafters.get(var4)).updateCraftingInventorySlot(this, var1, var3);
                }
            }
        }
        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.crafters.get(var1);
            if (this.soilamt != this.sluice.soilAmount)
            {
                var2.updateCraftingInventoryInfo(this, 0, this.sluice.soilAmount);
            }
            if (this.progress != this.sluice.processTimeRemaining)
            {
                var2.updateCraftingInventoryInfo(this, 1, this.sluice.processTimeRemaining);
            }
        }

        soilamt = this.sluice.soilAmount;
        progress = this.sluice.processTimeRemaining;
    }
	@Override
	public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.sluice.soilAmount = par2;
        }
        if (par1 == 1)
        {
            this.sluice.processTimeRemaining = par2;
        }
    }
}
