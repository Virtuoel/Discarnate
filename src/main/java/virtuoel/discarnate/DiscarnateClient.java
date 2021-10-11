package virtuoel.discarnate;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.client.gui.GuiSpiritChanneler;
import virtuoel.discarnate.client.handler.ClientEventHandler;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;
import virtuoel.discarnate.init.TaskRegistrar;

public class DiscarnateClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ClientTickEvents.START_WORLD_TICK.register(ClientEventHandler::onInputUpdate);
		
		ClientPlayNetworking.registerGlobalReceiver(Discarnate.TASK_PACKET, DiscarnateClient::handleTaskPacket);
		
		ScreenRegistry.register(ScreenHandlerRegistrar.SPIRIT_CHANNELER, GuiSpiritChanneler::new);
	}
	
	private static void handleTaskPacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		Task task = TaskRegistrar.REGISTRY.get(buf.readIdentifier());
		BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		int slot = buf.readInt();
		ItemStack stack = slot == -1 ? buf.readItemStack() : ItemStack.EMPTY;
		DimensionType dimension = handler.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).get(buf.readIdentifier());
		
		client.execute(() ->
		{
			ClientWorld w = client.world;
			if((slot != -1 || !stack.isEmpty()) && w != null && w.getDimension() == dimension)
			{
				if(w.isChunkLoaded(pos))
				{
					BlockEntity te = w.getBlockEntity(pos);
					if(te != null)
					{
						PlayerEntity player = (PlayerEntity) (Object) client.player;
						if(slot != -1 && te instanceof Inventory)
						{
							task.accept(((Inventory) te).getStack(slot), player, te);
						}
						else
						{
							task.accept(stack, player, te);
						}
					}
				}
			}
		});
	}
}
