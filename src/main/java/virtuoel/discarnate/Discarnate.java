package virtuoel.discarnate;

import java.util.stream.Stream;

import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.service.MixinService;

import net.minecraft.item.Item;
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
import virtuoel.discarnate.util.ReflectionUtils;

@Mod(Discarnate.MOD_ID)
public class Discarnate
{
	public static final String MOD_ID = "discarnate";
	
	public static final ILogger LOGGER = MixinService.getService().getLogger(MOD_ID);
	
	public static final ItemGroup ITEM_GROUP = ReflectionUtils.buildItemGroup(
		id("general"),
		() -> new ItemStack(BlockRegistrar.SPIRIT_CHANNELER.get()),
		() -> Stream.of(
			BlockRegistrar.SPIRIT_CHANNELER,
			ItemRegistrar.BLANK_TASK,
			ItemRegistrar.INFO_TASK,
			ItemRegistrar.WAIT_TASK,
			ItemRegistrar.DROP_TASK,
			ItemRegistrar.SWAP_TASK,
			ItemRegistrar.MOVE_FORWARD_TASK,
			ItemRegistrar.TOGGLE_MOVE_FORWARD_TASK,
			ItemRegistrar.MOVE_BACKWARD_TASK,
			ItemRegistrar.TOGGLE_MOVE_BACKWARD_TASK,
			ItemRegistrar.STRAFE_LEFT_TASK,
			ItemRegistrar.TOGGLE_STRAFE_LEFT_TASK,
			ItemRegistrar.STRAFE_RIGHT_TASK,
			ItemRegistrar.TOGGLE_STRAFE_RIGHT_TASK,
			ItemRegistrar.CANCEL_MOVEMENT_TASK,
			ItemRegistrar.LOOK_UP_TASK,
			ItemRegistrar.LOOK_DOWN_TASK,
			ItemRegistrar.LOOK_LEFT_TASK,
			ItemRegistrar.LOOK_RIGHT_TASK,
			ItemRegistrar.FACE_HORIZON_TASK,
			ItemRegistrar.FACE_CARDINAL_TASK,
			ItemRegistrar.SNEAK_TASK,
			ItemRegistrar.TOGGLE_SNEAK_TASK,
			ItemRegistrar.JUMP_TASK,
			ItemRegistrar.TOGGLE_JUMP_TASK,
			ItemRegistrar.SWING_ITEM_TASK,
			ItemRegistrar.TOGGLE_SWING_ITEM_TASK,
			ItemRegistrar.USE_ITEM_TASK,
			ItemRegistrar.TOGGLE_USE_ITEM_TASK,
			ItemRegistrar.SWITCH_SLOT_TASK,
			ItemRegistrar.END_TASK
		)
	);
	
	public Discarnate()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		BlockRegistrar.BLOCKS.register(modBus);
		ItemRegistrar.ITEMS.register(modBus);
		BlockEntityRegistrar.BLOCK_ENTITIES.register(modBus);
		ScreenHandlerRegistrar.SCREEN_HANDLERS.register(modBus);
		TaskRegistrar.TASKS.register(modBus);
		modBus.register(TaskRegistrar.class);
		
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
	
	public static Item.Settings commonItemSettings()
	{
		final Item.Settings settings = new Item.Settings();
		
		ReflectionUtils.setItemSettingsGroup(settings, ITEM_GROUP);
		
		return settings;
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
