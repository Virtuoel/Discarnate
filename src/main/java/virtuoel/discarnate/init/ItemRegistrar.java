package virtuoel.discarnate.init;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import virtuoel.discarnate.Discarnate;

@EventBusSubscriber(modid = Discarnate.MOD_ID)
@ObjectHolder(Discarnate.MOD_ID)
public class ItemRegistrar
{
	public static final Item BLANK_TASK = Items.AIR;
	public static final Item INFO_TASK = Items.AIR;
	public static final Item WAIT_TASK = Items.AIR;
	public static final Item JUMP_TASK = Items.AIR;
	public static final Item DROP_TASK = Items.AIR;
	public static final Item END_TASK = Items.AIR;
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		Stream.of(
			setRegistryNameAndTranslationKey(
				new Item()
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"blank_task"),
			setRegistryNameAndTranslationKey(
				new Item()
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"info_task"),
			setRegistryNameAndTranslationKey(
				new Item()
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"wait_task"),
			setRegistryNameAndTranslationKey(
				new Item()
				.setMaxStackSize(1)
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"jump_task"),
			setRegistryNameAndTranslationKey(
				new Item()
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"drop_task"),
			setRegistryNameAndTranslationKey(
				new Item()
				.setMaxStackSize(1)
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"end_task"),
		null).filter(Objects::nonNull)
		.forEach(event.getRegistry()::register);
	}
	
	@EventBusSubscriber(modid = Discarnate.MOD_ID, value = Side.CLIENT)
	public static class Client
	{
		@SubscribeEvent
		public static void registerItemModels(ModelRegistryEvent event)
		{
			final Consumer<Item> setItemModel = item ->
			{
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			};
			
			Stream.of(
				BLANK_TASK,
				INFO_TASK,
				WAIT_TASK,
				JUMP_TASK,
				DROP_TASK,
				END_TASK,
			null).filter(i -> i != null && i != Items.AIR)
			.forEach(setItemModel);
		}
	}
	
	public static <T extends Item> T setRegistryNameAndTranslationKey(T entry, String name, String key)
	{
		entry.setRegistryName(name).setTranslationKey(entry.getRegistryName().getNamespace() + "." + key);
		return entry;
	}
	
	public static <T extends Item> T setRegistryNameAndTranslationKey(T entry, String name)
	{
		return setRegistryNameAndTranslationKey(entry, name, name);
	}
}
