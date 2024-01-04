package virtuoel.discarnate.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent;
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
		this.task = TaskRegistrar.REGISTRY.get().get(buf.readIdentifier());
		this.pos = buf.readBlockPos();
		this.stack = buf.readItemStack();
		this.id = buf.readIdentifier();
	}
	
	public static void handle(TaskPacket msg, NetworkEvent.Context ctx)
	{
		Task task = msg.task;
		BlockPos pos = msg.pos;
		ItemStack stack = msg.stack;
		Identifier id = msg.id;
		
		ctx.enqueueWork(() ->
		{
			if (FMLEnvironment.dist == Dist.CLIENT)
			{
				MinecraftClient client = MinecraftClient.getInstance();
				PlayerEntity p = (PlayerEntity) (Object) client.player;
				ClientWorld w = client.world;
				if (p != null && w != null && id.equals(w.getRegistryKey().getValue()))
				{
					if (w.isChunkLoaded(pos))
					{
						task.accept(stack, p, w.getBlockEntity(pos));
					}
				}
			}
		});
		
		ctx.setPacketHandled(true);
	}
	
	public void encode(PacketByteBuf buf)
	{
		buf.writeIdentifier(TaskRegistrar.REGISTRY.get().getId(task));
		buf.writeBlockPos(pos);
		buf.writeItemStack(stack);
		buf.writeIdentifier(id);
	}
}
