package virtuoel.discarnate.init;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.screen.SpiritChannelerScreenHandler;

public class ScreenHandlerRegistrar
{
	public static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Discarnate.MOD_ID);
	
	public static final RegistryObject<ScreenHandlerType<SpiritChannelerScreenHandler>> SPIRIT_CHANNELER = SCREEN_HANDLERS.register("spirit_channeler",
		() -> new ScreenHandlerType<>(SpiritChannelerScreenHandler::new)
	);
}
