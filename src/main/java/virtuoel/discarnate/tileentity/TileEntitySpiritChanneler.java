package virtuoel.discarnate.tileentity;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.DiscarnateAPI;
import virtuoel.discarnate.block.BlockSpiritChanneler;

public class TileEntitySpiritChanneler extends TileEntity
{
	IItemHandlerModifiable itemHandler;
	
	public TileEntitySpiritChanneler(int inventorySize)
	{
		itemHandler = new ItemStackHandler(inventorySize)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				super.onContentsChanged(slot);
				TileEntitySpiritChanneler.this.markDirty();
				
				Optional.ofNullable(getWorld()).ifPresent(world ->
				{
					IBlockState state = world.getBlockState(getPos());
					getWorld().notifyBlockUpdate(getPos(), state, state, 3);
				});
			}
		};
	}
	
	public TileEntitySpiritChanneler()
	{
		this(25);
	}

	@Override
	public void invalidate()
	{
		deactivate();
		super.invalidate();
	}
	
	@Nullable
	Thread taskThread = null;
	
	public boolean activate(EntityPlayer player)
	{
		synchronized(this)
		{
			if(taskThread == null)
			{
				World w = getWorld();
				taskThread = new Thread(() ->
				{
					for(int i = 0; i < itemHandler.getSlots(); i++)
					{
						if(isActive())
						{
							ItemStack stack = itemHandler.getStackInSlot(i);
							DiscarnateAPI.instance().getTask(stack).ifPresent(task -> task.accept(stack, player, this));
						}
						else
						{
							break;
						}
					}
					
					if(w != null)
					{
						w.playSound(null, player.getPosition(), SoundEvents.ENTITY_VEX_DEATH, SoundCategory.BLOCKS, 0.5F, 1.0F);
					}
					
					deactivate();
				}, "SpiritChannelerTasks");
				
				if(w != null)
				{
					IBlockState state = w.getBlockState(getPos());
					if(state.getPropertyKeys().contains(BlockSpiritChanneler.ACTIVE))
					{
						w.setBlockState(getPos(), state.withProperty(BlockSpiritChanneler.ACTIVE, true));
					}
					w.playSound(null, player.getPosition(), SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.BLOCKS, 0.5F, 1.0F);
				}
				
				taskThread.start();
				return true;
			}
			return false;
		}
	}
	
	public boolean deactivate()
	{
		synchronized(this)
		{
			Optional.ofNullable(getWorld()).ifPresent(w ->
			{
				IBlockState state = w.getBlockState(getPos());
				if(state.getPropertyKeys().contains(BlockSpiritChanneler.ACTIVE))
				{
					w.setBlockState(getPos(), state.withProperty(BlockSpiritChanneler.ACTIVE, false));
				}
			});
			
			if(taskThread != null)
			{
				taskThread.interrupt();
				taskThread = null;
				return true;
			}
			return false;
		}
	}
	
	public boolean isActive()
	{
		synchronized(this)
		{
			World w = getWorld();
			if(w != null)
			{
				IBlockState state = w.getBlockState(getPos());
				if(state.getPropertyKeys().contains(BlockSpiritChanneler.ACTIVE))
				{
					return state.getValue(BlockSpiritChanneler.ACTIVE);
				}
			}
			return false;
		}
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(Discarnate.MOD_ID + ".spirit_channeler");
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		if(compound.hasKey("Items"))
		{
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(itemHandler, null, compound.getTagList("Items", Constants.NBT.TAG_COMPOUND));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		
		compound.setTag("Items", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(itemHandler, null));
		
		return compound;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return (facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
		}
		return super.getCapability(capability, facing);
	}
}
