package virtuoel.discarnate.init;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.DiscarnateAPI;

@EventBusSubscriber(modid = Discarnate.MOD_ID)
@ObjectHolder(Discarnate.MOD_ID)
public class ItemRegistrar
{
	public static final Item BLANK_TASK = Items.AIR;
	public static final Item INFO_TASK = Items.AIR;
	public static final Item WAIT_TASK = Items.AIR;
	
	public static void init()
	{
		DiscarnateAPI.instance().addTask(BLANK_TASK, (i, p, t) ->
		{});
		
		DiscarnateAPI.instance().addTask(INFO_TASK, (i, p, t) ->
		{
			p.sendMessage(new TextComponentString("" + i.getCount()));
		});
		
		DiscarnateAPI.instance().addTask(WAIT_TASK, (i, p, t) ->
		{
			try
			{
				Thread.sleep(i.getCount() * 50);
			}
			catch(InterruptedException e)
			{}
		});
	}
	
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
