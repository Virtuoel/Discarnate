package virtuoel.discarnate.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import virtuoel.discarnate.Discarnate;

public class DiscarnatePacketHandler
{
	@SubscribeEvent
	public static void register(final RegisterPayloadHandlerEvent event)
	{
		final IPayloadRegistrar registrar = event.registrar(Discarnate.MOD_ID);
		
		registrar.play(Discarnate.TASK_PACKET, TaskPacket::new, TaskPacket::handle);
	}
}
