package virtuoel.discarnate.inventory;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import virtuoel.discarnate.api.DiscarnateAPI;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

public class ContainerSpiritChanneler extends Container
{
	protected TileEntitySpiritChanneler tileEntity;
	
	public ContainerSpiritChanneler(EntityPlayer player, final TileEntitySpiritChanneler tileEntity)
	{
		this.tileEntity = tileEntity;
		onContainerOpened(player);
	}
	
	public void onContainerOpened(EntityPlayer player)
	{
		if(tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			IItemHandler inventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			addTileEntitySlots(inventory, tileEntity);
		}
		
		final int xOffset = 8;
		final int yOffset = 122;
		final int slotBorder = 1;
		final int slotWidth = 16;
		final int slotHeight = 16;
		final int inventorySlotsVertical = 3;
		final int inventorySlotsHorizontal = 9;
		final int hotbarSlotsHorizontal = 9;
		final int hotbarSeparation = 4;
		
		IInventory playerInventory = player.inventory;
		
		for(int row = 0; row < inventorySlotsVertical; row++)
		{
			for(int col = 0; col < inventorySlotsHorizontal; col++)
			{
				int x = xOffset + col * (slotWidth + (slotBorder * 2));
				int y = row * (slotHeight + (slotBorder * 2)) + yOffset;
				addSlotToContainer(new Slot(playerInventory, col + row * inventorySlotsHorizontal + hotbarSlotsHorizontal, x, y));
			}
		}
		
		int hotbarY = (inventorySlotsVertical * (slotHeight + (slotBorder * 2))) + hotbarSeparation + yOffset;
		for(int col = 0; col < inventorySlotsHorizontal; col++)
		{
			int x = xOffset + col * (slotWidth + (slotBorder * 2));
			addSlotToContainer(new Slot(playerInventory, col, x, hotbarY));
		}
	}
	
	public void addTileEntitySlots(IItemHandler inventory, TileEntitySpiritChanneler tileEntity)
	{
		final int xOffset = 17;
		final int yOffset = 18;
		final int slotBorder = 1;
		final int slotWidth = 16;
		final int slotHeight = 16;
		final int inventorySlotsVertical = 5;
		final int inventorySlotsHorizontal = 5;
		
		for(int row = 0; row < inventorySlotsVertical; row++)
		{
			for(int col = 0; col < inventorySlotsHorizontal; col++)
			{
				int x = xOffset + col * (slotWidth + (slotBorder * 2));
				int y = row * (slotHeight + (slotBorder * 2)) + yOffset;
				this.addSlotToContainer(new SlotItemHandler(inventory, col + row * inventorySlotsHorizontal, x, y)
				{
					@Override
					public boolean canTakeStack(EntityPlayer playerIn)
					{
						return !tileEntity.isActive() && super.canTakeStack(playerIn);
					}
					
					@Override
					public boolean isItemValid(ItemStack stack)
					{
						return DiscarnateAPI.instance().getTask(stack).isPresent();
					}
					
					@Override
					public void onSlotChanged()
					{
						tileEntity.markDirty();
					}
				});
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		if(tileEntity != null && tileEntity.getWorld() != null)
		{
			BlockPos pos = tileEntity.getPos();
			return tileEntity.getWorld().getTileEntity(pos) == tileEntity && playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
		}
		return false;
	}
	
	@Override
	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
			
			if(index < containerSlots)
			{
				if(!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(tileEntity.isActive() || !this.mergeItemStack(itemstack1, 0, containerSlots, false))
			{
				if(index < containerSlots + 27)
				{
					if(!this.mergeItemStack(itemstack1, containerSlots + 27, inventorySlots.size(), false))
					{
						return ItemStack.EMPTY;
					}
				}
				else
				{
					if(!this.mergeItemStack(itemstack1, containerSlots, containerSlots + 27, false))
					{
						return ItemStack.EMPTY;
					}
				}
			}
			
			if(itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if(itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, itemstack1);
		}
		
		return itemstack;
	}
}
