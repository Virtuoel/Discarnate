package virtuoel.discarnate.init;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.screen.SpiritChannelerScreenHandler;

public class ScreenHandlerRegistrar
{
	public static final ScreenHandlerType<SpiritChannelerScreenHandler> SPIRIT_CHANNELER =
		ScreenHandlerRegistry.registerExtended(
			Discarnate.id("spirit_channeler"),
			SpiritChannelerScreenHandler::new
		);
	
	public static final ScreenHandlerRegistrar INSTANCE = new ScreenHandlerRegistrar();
	
	private ScreenHandlerRegistrar()
	{
		
	}
}
