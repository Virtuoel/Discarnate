package virtuoel.discarnate;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.client.gui.screen.ingame.SpiritChannelerScreen;
import virtuoel.discarnate.init.BlockRegistrar;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;
import virtuoel.discarnate.init.TaskRegistrar;
import virtuoel.discarnate.util.ReflectionUtils;

public class DiscarnateClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistrar.SPIRIT_CHANNELER, RenderLayer.getCutout());
		
		ClientPlayNetworking.registerGlobalReceiver(Discarnate.TASK_PACKET, DiscarnateClient::handleTaskPacket);
		
		ScreenRegistry.register(ScreenHandlerRegistrar.SPIRIT_CHANNELER, SpiritChannelerScreen::new);
	}
	
	private static void handleTaskPacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		Task task = ReflectionUtils.get(TaskRegistrar.REGISTRY, buf.readIdentifier());
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
					task.accept(stack, p, w.getBlockEntity(pos));
				}
			}
		});
	}
}
