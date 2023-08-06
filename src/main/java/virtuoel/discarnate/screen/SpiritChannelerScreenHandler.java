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
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;
import virtuoel.discarnate.init.TaskRegistrar;
import virtuoel.discarnate.util.ReflectionUtils;
import virtuoel.discarnate.util.VersionUtils;

public class SpiritChannelerScreenHandler extends ScreenHandler
{
	public final ScreenHandlerContext context;
	public final Inventory inventory;
	public final PropertyDelegate propertyDelegate;
	
	public SpiritChannelerScreenHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleInventory(25), new ArrayPropertyDelegate(2), ScreenHandlerContext.EMPTY);
	}
	
	public SpiritChannelerScreenHandler(int syncId, PlayerInventory playerInventory, final Inventory inventory, PropertyDelegate propertyDelegate, ScreenHandlerContext screenHandlerContext)
	{
		super(ScreenHandlerRegistrar.SPIRIT_CHANNELER, syncId);
		this.context = screenHandlerContext;
		this.inventory = inventory;
		this.propertyDelegate = propertyDelegate;
		addProperties(this.propertyDelegate);
		addInventorySlots(playerInventory.player);
		addPlayerSlots(playerInventory);
	}
	
	public boolean isActive()
	{
		return this.propertyDelegate.get(SpiritChannelerBlockEntity.ACTIVE) == 1;
	}
	
	public boolean isLocked()
	{
		return this.propertyDelegate.get(SpiritChannelerBlockEntity.LOCKED) == 1;
	}
	
	private boolean isMutable()
	{
		return !isActive() && !isLocked();
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
				final int x = xOffset + col * (slotWidth + (slotBorder * 2));
				final int y = row * (slotHeight + (slotBorder * 2)) + yOffset;
				addSlot(new Slot(playerInventory, col + row * inventorySlotsHorizontal + hotbarSlotsHorizontal, x, y));
			}
		}
		
		final int hotbarY = (inventorySlotsVertical * (slotHeight + (slotBorder * 2))) + hotbarSeparation + yOffset;
		for (int col = 0; col < inventorySlotsHorizontal; col++)
		{
			final int x = xOffset + col * (slotWidth + (slotBorder * 2));
			addSlot(new Slot(playerInventory, col, x, hotbarY));
		}
	}
	
	public void addInventorySlots(PlayerEntity player)
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
				final int x = xOffset + col * (slotWidth + (slotBorder * 2));
				final int y = row * (slotHeight + (slotBorder * 2)) + yOffset;
				this.addSlot(new Slot(this.inventory, col + row * inventorySlotsHorizontal, x, y)
				{
					@Override
					public boolean canTakeItems(PlayerEntity player)
					{
						return isMutable() && super.canTakeItems(player);
					}
					
					@Override
					public boolean canInsert(ItemStack stack)
					{
						return isMutable() && ReflectionUtils.getOrEmpty(TaskRegistrar.REGISTRY, ReflectionUtils.getId(ReflectionUtils.ITEM_REGISTRY, stack.getItem())).map(t -> !t.getContainedTasks(stack, player, this.inventory instanceof BlockEntity ? (BlockEntity) this.inventory : null).isEmpty()).orElse(false);
					}
				});
			}
		}
	}
	
	@Override
	public boolean onButtonClick(PlayerEntity player, int id)
	{
		return this.context.get((world, pos) ->
		{
			if (world.isChunkLoaded(pos))
			{
				final BlockEntity be = world.getBlockEntity(pos);
				if (be instanceof SpiritChannelerBlockEntity)
				{
					final SpiritChannelerBlockEntity channeler = ((SpiritChannelerBlockEntity) be);
					
					switch (id)
					{
						case 0:
							if (!channeler.isActive())
							{
								return channeler.activate(player);
							}
							else
							{
								channeler.deactivate();
								return true;
							}
						case 1:
							if (player.isCreative())
							{
								channeler.setLocked(!channeler.isLocked());
								return true;
							}
							break;
						default:
							break;
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
		return this.inventory.canPlayerUse(player);
	}
	
	@Override
	@NotNull
	public ItemStack quickMove(PlayerEntity player, int index)
	{
		final Slot slot = this.slots.get(index);
		
		if (slot != null && slot.hasStack())
		{
			final ItemStack itemstack1 = slot.getStack();
			final ItemStack itemstack = itemstack1.copy();
			
			final int containerSlots = this.slots.size() - player.getInventory().main.size();
			
			if (index < containerSlots)
			{
				if (!this.insertItem(itemstack1, containerSlots, this.slots.size(), false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!isMutable() || !this.insertItem(itemstack1, 0, containerSlots, false))
			{
				if (index < containerSlots + 27)
				{
					if (!this.insertItem(itemstack1, containerSlots + 27, this.slots.size(), false))
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
				if (VersionUtils.MINOR > 19 || (VersionUtils.MINOR == 19 && VersionUtils.PATCH == 4))
				{
					slot.setStack(ItemStack.EMPTY);
				}
				else
				{
					slot.setStackNoCallbacks(ItemStack.EMPTY);
				}
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
			
			return itemstack;
		}
		
		return ItemStack.EMPTY;
	}
}
