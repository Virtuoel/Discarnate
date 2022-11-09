package virtuoel.discarnate.task;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.api.TaskAction;
import virtuoel.discarnate.api.TaskContainer;
import virtuoel.discarnate.init.TaskRegistrar;

public class ClientTask extends Task
{
	public ClientTask(TaskAction action)
	{
		super(action);
	}
	
	@ApiStatus.Experimental
	public ClientTask(TaskContainer container)
	{
		super(container);
	}
	
	@Override
	@ApiStatus.Experimental
	public List<TaskAction> getContainedTasks(@NotNull ItemStack s, @NotNull PlayerEntity p, @Nullable BlockEntity b)
	{
		if (p instanceof ServerPlayerEntity && !p.getEntityWorld().isClient)
		{
			return Collections.singletonList((s1, p1, b1) ->
			{
				final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				final BlockPos pos = getPosFromBlockEntity(b1);
				buf.writeIdentifier(TaskRegistrar.REGISTRY.getId(this));
				buf.writeBlockPos(pos);
				buf.writeItemStack(s1);
				buf.writeIdentifier(getWorldIdFromBlockEntity(b1));
				
				ServerPlayNetworking.send((ServerPlayerEntity) p, Discarnate.TASK_PACKET, buf);
			});
		}
		
		return super.getContainedTasks(s, p, b);
	}
	
	private static BlockPos getPosFromBlockEntity(BlockEntity be)
	{
		return be != null ? be.getPos() : BlockPos.ORIGIN;
	}
	
	private static Identifier getWorldIdFromBlockEntity(BlockEntity be)
	{
		if (be != null)
		{
			return be.getWorld().getRegistryKey().getValue();
		}
		
		return World.OVERWORLD.getValue();
	}
}
