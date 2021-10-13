package virtuoel.discarnate;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.client.gui.screen.ingame.SpiritChannelerScreen;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;
import virtuoel.discarnate.init.TaskRegistrar;

public class DiscarnateClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ClientPlayNetworking.registerGlobalReceiver(Discarnate.TASK_PACKET, DiscarnateClient::handleTaskPacket);
		
		ScreenRegistry.register(ScreenHandlerRegistrar.SPIRIT_CHANNELER, SpiritChannelerScreen::new);
	}
	
	private static void handleTaskPacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		Task task = TaskRegistrar.REGISTRY.get(buf.readIdentifier());
		BlockPos pos = buf.readBlockPos();
		ItemStack stack = buf.readItemStack();
		Identifier id = buf.readIdentifier();
		
		client.execute(() ->
		{
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
	}
}
