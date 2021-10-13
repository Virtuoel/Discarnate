package virtuoel.discarnate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import virtuoel.discarnate.init.BlockEntityRegistrar;
import virtuoel.discarnate.init.BlockRegistrar;
import virtuoel.discarnate.init.GameRuleRegistrar;
import virtuoel.discarnate.init.ItemRegistrar;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;
import virtuoel.discarnate.init.TaskRegistrar;

public class Discarnate implements ModInitializer
{
	public static final String MOD_ID = "discarnate";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
		id("general"))
		.icon(() -> new ItemStack(BlockRegistrar.SPIRIT_CHANNELER))
		.build();
	
	@Override
	public void onInitialize()
	{
		BlockRegistrar.INSTANCE.getClass();
		ItemRegistrar.INSTANCE.getClass();
		BlockEntityRegistrar.INSTANCE.getClass();
		ScreenHandlerRegistrar.INSTANCE.getClass();
		GameRuleRegistrar.INSTANCE.getClass();
		TaskRegistrar.INSTANCE.getClass();
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
