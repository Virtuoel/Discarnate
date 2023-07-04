package virtuoel.discarnate.init;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.screen.SpiritChannelerScreenHandler;

public class ScreenHandlerRegistrar
{
	public static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Discarnate.MOD_ID);
	
	public static final RegistryObject<ScreenHandlerType<SpiritChannelerScreenHandler>> SPIRIT_CHANNELER = SCREEN_HANDLERS.register("spirit_channeler",
		() -> IForgeMenuType.create((id, inv, data) -> new SpiritChannelerScreenHandler(id, inv))
	);
}
