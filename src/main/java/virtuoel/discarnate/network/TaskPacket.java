package virtuoel.discarnate.network;

import java.util.function.Supplier;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.init.TaskRegistrar;

public class TaskPacket
{
	private Task task;
	private BlockPos pos;
	private ItemStack stack;
	private Identifier id;
	
	public TaskPacket(Task task, BlockPos pos, ItemStack stack, Identifier id)
	{
		this.task = task;
		this.pos = pos;
		this.stack = stack;
		this.id = id;
	}
	
	protected TaskPacket(PacketByteBuf buf)
	{
		this.task = TaskRegistrar.REGISTRY.get().getValue(buf.readIdentifier());
		this.pos = buf.readBlockPos();
		this.stack = buf.readItemStack();
		this.id = buf.readIdentifier();
	}
	
	public static void handle(TaskPacket msg, Supplier<NetworkEvent.Context> ctx)
	{
		Task task = msg.task;
		BlockPos pos = msg.pos;
		ItemStack stack = msg.stack;
		Identifier id = msg.id;
		
		ctx.get().enqueueWork(() ->
		{
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
			{
				MinecraftClient client = MinecraftClient.getInstance();
				PlayerEntity p = (PlayerEntity) (Object) client.player;
				ClientWorld w = client.world;
				if (p != null && w != null && id.equals(w.getRegistryKey().getValue()))
				{
					if (w.isChunkLoaded(pos))
					{
						BlockEntity be = w.getBlockEntity(pos);
						if (be != null)
						{
							task.accept(stack, p, be);
						}
					}
				}
			});
		});
		
		ctx.get().setPacketHandled(true);
	}
	
	public void encode(PacketByteBuf buf)
	{
		buf.writeIdentifier(task.getRegistryName());
		buf.writeBlockPos(pos);
		buf.writeItemStack(stack);
		buf.writeIdentifier(id);
	}
}
