package virtuoel.discarnate;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import virtuoel.discarnate.client.gui.screen.ingame.SpiritChannelerScreen;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;

public class DiscarnateClient
{
	public static void setupClient(final FMLClientSetupEvent event)
	{
		HandledScreens.register(ScreenHandlerRegistrar.SPIRIT_CHANNELER.get(), SpiritChannelerScreen::new);
	}
}
