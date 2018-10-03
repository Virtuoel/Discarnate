package virtuoel.discarnate.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import virtuoel.discarnate.Discarnate;

@EventBusSubscriber(modid = Discarnate.MOD_ID)
public class RecipeRegistrar
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void registerOreDict(RegistryEvent.Register<IRecipe> event)
	{
		OreDictionary.registerOre("barsIron", Blocks.IRON_BARS);
	}
}
