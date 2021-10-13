package virtuoel.discarnate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import virtuoel.discarnate.api.DiscarnateConfig;
import virtuoel.discarnate.init.BlockEntityRegistrar;
import virtuoel.discarnate.init.BlockRegistrar;
import virtuoel.discarnate.init.ItemRegistrar;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;
import virtuoel.discarnate.init.TaskRegistrar;
import virtuoel.discarnate.network.DiscarnatePacketHandler;

@Mod(Discarnate.MOD_ID)
public class Discarnate
{
	public static final String MOD_ID = "discarnate";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID + ".general")
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(BlockRegistrar.SPIRIT_CHANNELER.get());
		}
	};
	
	public Discarnate()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		BlockRegistrar.BLOCKS.register(modBus);
		ItemRegistrar.ITEMS.register(modBus);
		BlockEntityRegistrar.BLOCK_ENTITIES.register(modBus);
		ScreenHandlerRegistrar.SCREEN_HANDLERS.register(modBus);
		TaskRegistrar.TASKS.register(modBus);
		
		MinecraftForge.EVENT_BUS.register(DiscarnateConfig.class);
		
		ModLoadingContext ctx = ModLoadingContext.get();
		ctx.registerConfig(ModConfig.Type.CLIENT, DiscarnateConfig.clientSpec);
		ctx.registerConfig(ModConfig.Type.SERVER, DiscarnateConfig.serverSpec);
		ctx.registerConfig(ModConfig.Type.COMMON, DiscarnateConfig.commonSpec);
		
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
		{
			modBus.addListener(DiscarnateClient::setupClient);
		});
		
		DiscarnatePacketHandler.init();
	}
	
	public static Identifier id(String path)
	{
		return new Identifier(MOD_ID, path);
	}
	
	public static Identifier id(String path, String... paths)
	{
		return id(paths.length == 0 ? path : path + "/" + String.join("/", paths));
	}
	
	public static final Identifier TASK_PACKET = id("task");
}
