package virtuoel.discarnate.network;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.init.TaskRegistrar;

public class SPacketBuiltinClientTask implements IMessage
{
	@ObjectHolder(Discarnate.MOD_ID + ":blank_task")
	private static final Task BLANK_TASK = null;
	
	@Nonnull
	Task task;
	@Nonnull
	BlockPos pos;
	@Nonnull
	ItemStack stack;
	int slot;
	int dimension;
	
	public SPacketBuiltinClientTask()
	{
		this(BLANK_TASK, BlockPos.ORIGIN, ItemStack.EMPTY, -1, 0);
	}
	
	public SPacketBuiltinClientTask(Task task, BlockPos pos, ItemStack stack, int slot, int dimension)
	{
		this.task = task;
		this.pos = pos;
		this.stack = stack;
		this.slot = slot;
		this.dimension = dimension;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		task = ByteBufUtils.readRegistryEntry(buf, TaskRegistrar.REGISTRY);
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		slot = buf.readInt();
		stack = slot == -1 ? ByteBufUtils.readItemStack(buf) : ItemStack.EMPTY;
		dimension = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeRegistryEntry(buf, task);
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(slot);
		if(slot == -1)
		{
			ByteBufUtils.writeItemStack(buf, stack);
		}
		buf.writeInt(dimension);
	}
	
	public static class Handler implements IMessageHandler<SPacketBuiltinClientTask, IMessage>
	{
		@Override
		public IMessage onMessage(SPacketBuiltinClientTask message, MessageContext ctx)
		{
			WorldClient w = Minecraft.getMinecraft().world;
			if((message.slot != -1 || !message.stack.isEmpty()) && w != null && w.provider.getDimension() == message.dimension)
			{
				Minecraft.getMinecraft().addScheduledTask(() ->
				{
					if(w.isBlockLoaded(message.pos))
					{
						TileEntity te = w.getTileEntity(message.pos);
						if(te != null)
						{
							EntityPlayer player = (EntityPlayer) (Object) Minecraft.getMinecraft().player;
							ItemStack stack = message.stack;
							if(message.slot != -1 && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
							{
								IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
								stack = handler.getStackInSlot(message.slot);
							}
							message.task.accept(stack, player, te);
						}
					}
				});
			}
			return null;
		}
	}
}
