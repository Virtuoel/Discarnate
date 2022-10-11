package virtuoel.discarnate.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import virtuoel.discarnate.Discarnate;

public class DiscarnatePacketHandler
{
	private static final String PROTOCOL_VERSION = Integer.toString(2);
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		Discarnate.id("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
	);
	
	public static void init()
	{
		int regId = 0;
		INSTANCE.registerMessage(regId++, ActivatePacket.class, ActivatePacket::encode, ActivatePacket::new, ActivatePacket::handle);
		INSTANCE.registerMessage(regId++, TaskPacket.class, TaskPacket::encode, TaskPacket::new, TaskPacket::handle);
	}
}
