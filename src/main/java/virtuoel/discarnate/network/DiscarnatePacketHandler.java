package virtuoel.discarnate.network;

import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import virtuoel.discarnate.Discarnate;

public class DiscarnatePacketHandler
{
	private static final String PROTOCOL_VERSION = Integer.toString(3);
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		Discarnate.id("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
	);
	
	public static void init()
	{
		int regId = 0;
		INSTANCE.registerMessage(regId++, TaskPacket.class, TaskPacket::encode, TaskPacket::new, TaskPacket::handle);
	}
}
