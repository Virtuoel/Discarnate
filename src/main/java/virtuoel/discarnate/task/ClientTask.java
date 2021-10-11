package virtuoel.discarnate.task;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.ITask;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.network.SPacketBuiltinClientTask;

public class ClientTask implements Task
{
	Task task;
	
	public ClientTask(Task task)
	{
		this.task = task;
	}
	
	@Override
	public void accept(@NotNull ItemStack s, @Nullable PlayerEntity p, @Nullable BlockEntity b)
	{
		if(p instanceof EntityPlayerMP && !p.getEntityWorld().isRemote)
		{
			Discarnate.NETWORK.sendTo(new SPacketBuiltinClientTask(this, getPosFromTileEntity(t), s, getSlotForStack(s, b), getDimensionFromTileEntity(b)), (EntityPlayerMP) p);
		}
		else
		{
			task.accept(s, p, b);
		}
	}
	
	private static BlockPos getPosFromTileEntity(TileEntity te)
	{
		return te != null ? te.getPos() : BlockPos.ORIGIN;
	}
	
	private static int getDimensionFromTileEntity(TileEntity te)
	{
		return te != null ? te.getWorld().provider.getDimension() : DimensionType.OVERWORLD.getId();
	}
	
	private static int getSlotForStack(ItemStack stack, TileEntity te)
	{
		if(te != null)
		{
			if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
			{
				IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				for(int i = 0; i < handler.getSlots(); i++)
				{
					if(stack == handler.getStackInSlot(i))
					{
						return i;
					}
				}
			}
		}
		return -1;
	}
}
