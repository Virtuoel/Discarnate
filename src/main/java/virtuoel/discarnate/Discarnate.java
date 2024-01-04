package virtuoel.discarnate;

import java.util.stream.Stream;

import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.service.MixinService;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
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
	
	public static final ILogger LOGGER = MixinService.getService().getLogger(MOD_ID);
	
	public static final DeferredHolder<ItemGroup, ItemGroup> ITEM_GROUP = ItemRegistrar.ITEM_GROUPS.register(
		"general",
		() -> ItemGroup.builder()
			.icon(() -> new ItemStack(BlockRegistrar.SPIRIT_CHANNELER.get()))
			.displayName(Text.translatable("itemGroup." + MOD_ID + ".general"))
			.build()
	);
	
	@SubscribeEvent
	public void buildContents(BuildCreativeModeTabContentsEvent event)
	{
		if (event.getTabKey() != ((RegistryEntry<ItemGroup>) ITEM_GROUP).getKey().orElse(null))
		{
			return;
		}
		
		Stream.of(
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
		.map(DeferredHolder::get)
		.forEach(event::add);
	}
	
	public Discarnate()
	{
		IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
		
		BlockRegistrar.BLOCKS.register(modBus);
		ItemRegistrar.ITEMS.register(modBus);
		BlockEntityRegistrar.BLOCK_ENTITY_TYPES.register(modBus);
		ScreenHandlerRegistrar.SCREEN_HANDLERS.register(modBus);
		ItemRegistrar.ITEM_GROUPS.register(modBus);
		TaskRegistrar.TASKS.register(modBus);
		modBus.register(TaskRegistrar.class);
		modBus.register(this);
		
		modBus.register(DiscarnateConfig.class);
		
		ModLoadingContext ctx = ModLoadingContext.get();
		ctx.registerConfig(ModConfig.Type.CLIENT, DiscarnateConfig.clientSpec);
		ctx.registerConfig(ModConfig.Type.SERVER, DiscarnateConfig.serverSpec);
		ctx.registerConfig(ModConfig.Type.COMMON, DiscarnateConfig.commonSpec);
		
		if (FMLEnvironment.dist == Dist.CLIENT)
		{
			modBus.addListener(DiscarnateClient::setupClient);
		}
		
		modBus.register(DiscarnatePacketHandler.class);
	}
	
	public static Item.Settings commonItemSettings()
	{
		return new Item.Settings();
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
