package TFC.Containers;

import TFC.*;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotForgeFuel extends Slot
{
    public SlotForgeFuel(EntityPlayer entityplayer, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);

    }

    public boolean isItemValid(ItemStack itemstack)
    {
        if(itemstack.itemID == Item.coal.shiftedIndex) {
            return true;
        }
        return false;
    }

    public int getSlotStackLimit()
    {
        return 1;
    }
}
