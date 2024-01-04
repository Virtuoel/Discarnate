package virtuoel.discarnate.api;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import virtuoel.discarnate.Discarnate;

public class DiscarnateConfig
{
	@ApiStatus.Internal
	public static final ModConfigSpec clientSpec;
	public static final Client CLIENT;
	@ApiStatus.Internal
	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;
	@ApiStatus.Internal
	public static final ModConfigSpec serverSpec;
	public static final Server SERVER;
	
	@ApiStatus.Internal
	@SubscribeEvent
	public static void onLoad(ModConfigEvent.Loading configEvent)
	{
		Discarnate.LOGGER.debug(
			"Loaded Discarnate config file {}", configEvent.getConfig().getFileName()
		);
	}
	
	@ApiStatus.Internal
	@SubscribeEvent
	public static void onFileChange(ModConfigEvent.Reloading configEvent)
	{
		Discarnate.LOGGER.debug(
			"Discarnate config just got changed on the file system!"
		);
	}
	
	static
	{
		Pair<?, ModConfigSpec> specPair;
		
		specPair = new ModConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = (Client) specPair.getLeft();
		
		specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = (Common) specPair.getLeft();
		
		specPair = new ModConfigSpec.Builder().configure(Server::new);
		serverSpec = specPair.getRight();
		SERVER = (Server) specPair.getLeft();
	}
	
	public static class Client
	{
		Client(ModConfigSpec.Builder builder)
		{
			builder
				.comment("Client only settings, mostly things related to rendering")
				.push("client");
			builder.pop();
		}
	}
	
	public static class Common
	{
		public final ModConfigSpec.BooleanValue pumpkinToStart;
		public final ModConfigSpec.BooleanValue pumpkinToContinue;
		public final ModConfigSpec.IntValue minLevel;
		public final ModConfigSpec.IntValue levelCost;
		
		Common(ModConfigSpec.Builder builder)
		{
			builder
				.comment("General configuration settings")
				.push("general");
			this.pumpkinToStart = builder
				.translation(Discarnate.MOD_ID + ".configgui.pumpkinToStart")
				.define("pumpkinToStart", true);
			this.pumpkinToContinue = builder
				.translation(Discarnate.MOD_ID + ".configgui.pumpkinToContinue")
				.define("pumpkinToContinue", true);
			this.minLevel = builder
				.translation(Discarnate.MOD_ID + ".configgui.minLevel")
				.defineInRange("minLevel", 1, 0, Integer.MAX_VALUE);
			this.levelCost = builder
				.translation(Discarnate.MOD_ID + ".configgui.levelCost")
				.defineInRange("levelCost", 1, 0, Integer.MAX_VALUE);
			
			builder.pop();
		}
	}
	
	public static class Server
	{
		Server(ModConfigSpec.Builder builder)
		{
			builder
				.comment("Server configuration settings")
				.push("server");
			builder.pop();
		}
	}
}
