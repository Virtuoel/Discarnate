package virtuoel.discarnate.api;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import virtuoel.discarnate.Discarnate;

public class DiscarnateConfig
{
	@ApiStatus.Internal
	public static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;
	@ApiStatus.Internal
	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;
	@ApiStatus.Internal
	public static final ForgeConfigSpec serverSpec;
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
		Pair<?, ForgeConfigSpec> specPair;
		
		specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = (Client) specPair.getLeft();
		
		specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = (Common) specPair.getLeft();
		
		specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		serverSpec = specPair.getRight();
		SERVER = (Server) specPair.getLeft();
	}
	
	public static class Client
	{
		Client(ForgeConfigSpec.Builder builder)
		{
			builder
				.comment("Client only settings, mostly things related to rendering")
				.push("client");
			builder.pop();
		}
	}
	
	public static class Common
	{
		public final ForgeConfigSpec.BooleanValue pumpkinToStart;
		public final ForgeConfigSpec.BooleanValue pumpkinToContinue;
		public final ForgeConfigSpec.IntValue minLevel;
		public final ForgeConfigSpec.IntValue levelCost;
		
		Common(ForgeConfigSpec.Builder builder)
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
		Server(ForgeConfigSpec.Builder builder)
		{
			builder
				.comment("Server configuration settings")
				.push("server");
			builder.pop();
		}
	}
}
