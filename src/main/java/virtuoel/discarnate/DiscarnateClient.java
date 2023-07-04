package virtuoel.discarnate;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import virtuoel.discarnate.client.gui.screen.ingame.SpiritChannelerScreen;
import virtuoel.discarnate.init.BlockRegistrar;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;

public class DiscarnateClient
{
	public static void setupClient(final FMLClientSetupEvent event)
	{
		HandledScreens.register(ScreenHandlerRegistrar.SPIRIT_CHANNELER.get(), SpiritChannelerScreen::new);
		
		RenderLayers.setRenderLayer(BlockRegistrar.SPIRIT_CHANNELER.get(), RenderLayer.getCutout());
	}
}
