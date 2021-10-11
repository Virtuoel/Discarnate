package virtuoel.discarnate.inventory;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;
import virtuoel.discarnate.init.TaskRegistrar;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

public class ContainerSpiritChanneler extends ScreenHandler
{
	public final TileEntitySpiritChanneler tileEntity;
	
	public ContainerSpiritChanneler(int syncId, PlayerInventory playerInventory, final PacketByteBuf buffer)
	{
		this(syncId, playerInventory, getAtPos(playerInventory.player.world, buffer.readBlockPos()));
	}
	
	private static TileEntitySpiritChanneler getAtPos(World world, BlockPos pos)
	{
		if (world.isChunkLoaded(pos))
		{
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileEntitySpiritChanneler)
			{
				return (TileEntitySpiritChanneler) be;
			}
		}
		return null;
	}
	
	public ContainerSpiritChanneler(int syncId, PlayerInventory playerInventory, final TileEntitySpiritChanneler tileEntity)
	{
		super(ScreenHandlerRegistrar.SPIRIT_CHANNELER, syncId);
		this.tileEntity = tileEntity;
		if (this.tileEntity != null)
		{
			addTileEntitySlots(this.tileEntity);
		}
		addPlayerSlots(playerInventory);
	}
	
	public void addPlayerSlots(Inventory playerInventory)
	{
		final int xOffset = 8;
		final int yOffset = 122;
		final int slotBorder = 1;
		final int slotWidth = 16;
		final int slotHeight = 16;
		final int inventorySlotsVertical = 3;
		final int inventorySlotsHorizontal = 9;
		final int hotbarSlotsHorizontal = 9;
		final int hotbarSeparation = 4;
		
		for(int row = 0; row < inventorySlotsVertical; row++)
		{
			for(int col = 0; col < inventorySlotsHorizontal; col++)
			{
				int x = xOffset + col * (slotWidth + (slotBorder * 2));
				int y = row * (slotHeight + (slotBorder * 2)) + yOffset;
				addSlot(new Slot(playerInventory, col + row * inventorySlotsHorizontal + hotbarSlotsHorizontal, x, y));
			}
		}
		
		int hotbarY = (inventorySlotsVertical * (slotHeight + (slotBorder * 2))) + hotbarSeparation + yOffset;
		for(int col = 0; col < inventorySlotsHorizontal; col++)
		{
			int x = xOffset + col * (slotWidth + (slotBorder * 2));
			addSlot(new Slot(playerInventory, col, x, hotbarY));
		}
	}
	
	public void addTileEntitySlots(Inventory inventory)
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
				this.addSlot(new Slot(inventory, col + row * inventorySlotsHorizontal, x, y)
				{
					@Override
					public boolean canTakeItems(PlayerEntity playerIn)
					{
						return tileEntity != null && !tileEntity.isActive() && super.canTakeItems(playerIn);
					}
					
					@Override
					public boolean canInsert(ItemStack stack)
					{
						return TaskRegistrar.REGISTRY.getOrEmpty(Registry.ITEM.getId(stack.getItem())).isPresent();
					}
				});
			}
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity playerIn)
	{
		return tileEntity != null && tileEntity.canPlayerUse(playerIn);
	}
	
	@Override
	@NotNull
	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		
		if(slot != null && slot.hasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			int containerSlots = slots.size() - player.getInventory().main.size();
			
			if(index < containerSlots)
			{
				if(!this.insertItem(itemstack1, containerSlots, slots.size(), false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if((tileEntity != null && tileEntity.isActive()) || !this.insertItem(itemstack1, 0, containerSlots, false))
			{
				if(index < containerSlots + 27)
				{
					if(!this.insertItem(itemstack1, containerSlots + 27, slots.size(), false))
					{
						return ItemStack.EMPTY;
					}
				}
				else
				{
					if(!this.insertItem(itemstack1, containerSlots, containerSlots + 27, false))
					{
						return ItemStack.EMPTY;
					}
				}
			}
			
			if(itemstack1.isEmpty())
			{
				slot.setStack(ItemStack.EMPTY);
			}
			else
			{
				slot.markDirty();
			}
			
			if(itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTakeItem(player, itemstack1);
		}
		
		return itemstack;
	}
}
