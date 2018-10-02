package virtuoel.discarnate.init;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

@EventBusSubscriber(modid = Discarnate.MOD_ID)
public class TileEntityRegistrar
{
	@SubscribeEvent
	public static void registerTileEntities(RegistryEvent.Register<Block> event)
	{
		GameRegistry.registerTileEntity(TileEntitySpiritChanneler.class, new ResourceLocation(Discarnate.MOD_ID, "spirit_channeler"));
	}
}
