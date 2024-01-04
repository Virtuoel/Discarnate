package virtuoel.discarnate.init;

import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.screen.SpiritChannelerScreenHandler;

public class ScreenHandlerRegistrar
{
	public static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLERS = DeferredRegister.create(Registries.SCREEN_HANDLER, Discarnate.MOD_ID);
	
	public static final DeferredHolder<ScreenHandlerType<?>, ScreenHandlerType<SpiritChannelerScreenHandler>> SPIRIT_CHANNELER = SCREEN_HANDLERS.register("spirit_channeler",
		() -> IMenuTypeExtension.create((id, inv, data) -> new SpiritChannelerScreenHandler(id, inv))
	);
}
