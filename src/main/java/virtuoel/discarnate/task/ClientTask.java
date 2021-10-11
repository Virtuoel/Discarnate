package virtuoel.discarnate.task;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.init.TaskRegistrar;

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
		if (p instanceof ServerPlayerEntity && !p.getEntityWorld().isClient)
		{
			final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			final BlockPos pos = getPosFromBlockEntity(b);
			buf.writeIdentifier(TaskRegistrar.REGISTRY.getId(this));
			buf.writeInt(pos.getX());
			buf.writeInt(pos.getY());
			buf.writeInt(pos.getZ());
			int slot = getSlotForStack(s, b);
			buf.writeInt(slot);
			if (slot == -1)
			{
				buf.writeItemStack(s);
			}
			buf.writeIdentifier(getDimensionFromBlockEntity(b));
			
			ServerPlayNetworking.send((ServerPlayerEntity) p, Discarnate.TASK_PACKET, buf);
		}
		else
		{
			task.accept(s, p, b);
		}
	}
	
	private static BlockPos getPosFromBlockEntity(BlockEntity be)
	{
		return be != null ? be.getPos() : BlockPos.ORIGIN;
	}
	
	private static Identifier getDimensionFromBlockEntity(BlockEntity be)
	{
		if (be != null)
		{
			World w = be.getWorld();
			return w.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).getId(w.getDimension());
		}
		return DimensionType.OVERWORLD_ID;
	}
	
	private static int getSlotForStack(ItemStack stack, BlockEntity te)
	{
		if (te instanceof Inventory)
		{
			Inventory handler = (Inventory) te;
			for (int i = 0; i < handler.size(); i++)
			{
				if (stack == handler.getStack(i))
				{
					return i;
				}
			}
		}
		
		return -1;
	}
}
