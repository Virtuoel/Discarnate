package virtuoel.discarnate.screen;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.registry.Registry;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;
import virtuoel.discarnate.init.TaskRegistrar;

public class SpiritChannelerScreenHandler extends ScreenHandler
{
	public final ScreenHandlerContext context;
	public final Inventory inventory;
	public final PropertyDelegate propertyDelegate;
	
	public SpiritChannelerScreenHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleInventory(25), new ArrayPropertyDelegate(1), ScreenHandlerContext.EMPTY);
	}
	
	public SpiritChannelerScreenHandler(int syncId, PlayerInventory playerInventory, final Inventory inventory, PropertyDelegate propertyDelegate, ScreenHandlerContext screenHandlerContext)
	{
		super(ScreenHandlerRegistrar.SPIRIT_CHANNELER, syncId);
		this.context = screenHandlerContext;
		this.inventory = inventory;
		this.propertyDelegate = propertyDelegate;
		addProperties(this.propertyDelegate);
		addInventorySlots();
		addPlayerSlots(playerInventory);
	}
	
	public boolean isActive()
	{
		return this.propertyDelegate.get(0) == 1;
	}
	
	public void addPlayerSlots(PlayerInventory playerInventory)
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
		
		for (int row = 0; row < inventorySlotsVertical; row++)
		{
			for (int col = 0; col < inventorySlotsHorizontal; col++)
			{
				int x = xOffset + col * (slotWidth + (slotBorder * 2));
				int y = row * (slotHeight + (slotBorder * 2)) + yOffset;
				addSlot(new Slot(playerInventory, col + row * inventorySlotsHorizontal + hotbarSlotsHorizontal, x, y));
			}
		}
		
		int hotbarY = (inventorySlotsVertical * (slotHeight + (slotBorder * 2))) + hotbarSeparation + yOffset;
		for (int col = 0; col < inventorySlotsHorizontal; col++)
		{
			int x = xOffset + col * (slotWidth + (slotBorder * 2));
			addSlot(new Slot(playerInventory, col, x, hotbarY));
		}
	}
	
	public void addInventorySlots()
	{
		final int xOffset = 17;
		final int yOffset = 18;
		final int slotBorder = 1;
		final int slotWidth = 16;
		final int slotHeight = 16;
		final int inventorySlotsVertical = 5;
		final int inventorySlotsHorizontal = 5;
		
		for (int row = 0; row < inventorySlotsVertical; row++)
		{
			for (int col = 0; col < inventorySlotsHorizontal; col++)
			{
				int x = xOffset + col * (slotWidth + (slotBorder * 2));
				int y = row * (slotHeight + (slotBorder * 2)) + yOffset;
				this.addSlot(new Slot(inventory, col + row * inventorySlotsHorizontal, x, y)
				{
					@Override
					public boolean canTakeItems(PlayerEntity player)
					{
						return !isActive() && super.canTakeItems(player);
					}
					
					@Override
					public boolean canInsert(ItemStack stack)
					{
						return !isActive() && TaskRegistrar.REGISTRY.getOrEmpty(Registry.ITEM.getId(stack.getItem())).isPresent();
					}
				});
			}
		}
	}
	
	@Override
	public boolean onButtonClick(PlayerEntity player, int id)
	{
		return context.get((world, pos) ->
		{
			if (world.isChunkLoaded(pos))
			{
				BlockEntity be = world.getBlockEntity(pos);
				if (be instanceof SpiritChannelerBlockEntity)
				{
					SpiritChannelerBlockEntity channeler = ((SpiritChannelerBlockEntity) be);
					if (!channeler.isActive())
					{
						return channeler.activate(player);
					}
					else
					{
						channeler.deactivate();
						return true;
					}
				}
			}
			
			return false;
		})
		.orElse(false);
	}
	
	@Override
	public boolean canUse(PlayerEntity player)
	{
		return inventory.canPlayerUse(player);
	}
	
	@Override
	@NotNull
	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		
		if (slot != null && slot.hasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			int containerSlots = slots.size() - player.getInventory().main.size();
			
			if (index < containerSlots)
			{
				if (!this.insertItem(itemstack1, containerSlots, slots.size(), false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (isActive() || !this.insertItem(itemstack1, 0, containerSlots, false))
			{
				if (index < containerSlots + 27)
				{
					if (!this.insertItem(itemstack1, containerSlots + 27, slots.size(), false))
					{
						return ItemStack.EMPTY;
					}
				}
				else
				{
					if (!this.insertItem(itemstack1, containerSlots, containerSlots + 27, false))
					{
						return ItemStack.EMPTY;
					}
				}
			}
			
			if (itemstack1.isEmpty())
			{
				slot.setStack(ItemStack.EMPTY);
			}
			else
			{
				slot.markDirty();
			}
			
			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTakeItem(player, itemstack1);
		}
		
		return itemstack;
	}
}
