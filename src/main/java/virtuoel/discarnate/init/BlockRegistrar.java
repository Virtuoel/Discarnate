package virtuoel.discarnate.init;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
public class BlockRegistrar
{
	public static final Block SPIRIT_WELL = Blocks.AIR;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		Stream.of(
			setRegistryNameAndTranslationKey(setHarvestLevel(
				new Block(Material.WOOD, MapColor.BROWN)
				.setHardness(5.0F)
				.setResistance(10.0F)
				.setSoundType(SoundType.WOOD)
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"axe", 0),
				"spirit_well"),
		null).filter(Objects::nonNull)
		.forEach(event.getRegistry()::register);
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event)
	{
		final Function<Block, Item> makeItemBlock = block -> new ItemBlock(block).setRegistryName(block.getRegistryName());
		
		Stream.of(
			SPIRIT_WELL,
		null).filter(b -> b != null && b != Blocks.AIR)
		.map(makeItemBlock)
		.forEach(event.getRegistry()::register);
	}
	
	@EventBusSubscriber(modid = Discarnate.MOD_ID, value = Side.CLIENT)
	public static class Client
	{
		@SubscribeEvent
		public static void registerItemBlockModels(ModelRegistryEvent event)
		{
			final Consumer<Item> setItemModel = item ->
			{
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			};
			
			Stream.of(
				SPIRIT_WELL,
			null).filter(b -> b != null && b != Blocks.AIR)
			.map(Item::getItemFromBlock)
			.filter(i -> i != null && i != Items.AIR)
			.forEach(setItemModel);
		}
	}
	
	public static <T extends Block> T setRegistryNameAndTranslationKey(T entry, String name, String key)
	{
		entry.setRegistryName(name).setTranslationKey(entry.getRegistryName().getNamespace() + "." + key);
		return entry;
	}
	
	public static <T extends Block> T setRegistryNameAndTranslationKey(T entry, String name)
	{
		return setRegistryNameAndTranslationKey(entry, name, name);
	}
	
	public static <T extends Block> T setHarvestLevel(T block, String toolClass, int level)
	{
		block.setHarvestLevel(toolClass, level);
		return block;
	}
}
