package virtuoel.discarnate.reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import virtuoel.discarnate.Discarnate;

@Config(modid = Discarnate.MOD_ID)
public class DiscarnateConfig
{
	public static boolean ignoreMissingTasks = true;
	public static boolean requirePumpkinToStart = true;
	public static boolean requirePumpkinToContinue = false;
	@Config.RangeInt(min = 0)
	public static int minExpLevel = 1;
	@Config.RangeInt(min = 0)
	public static int expLevelCost = 1;
	
	@EventBusSubscriber(modid = Discarnate.MOD_ID)
	private static class EventHandler
	{
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
		{
			if(event.getModID().equals(Discarnate.MOD_ID))
			{
				ConfigManager.sync(Discarnate.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
