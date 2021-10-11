package virtuoel.discarnate.init;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.block.SpiritChannelerBlock;

public class BlockRegistrar
{
	public static final Block SPIRIT_CHANNELER = registerBlock(
		Discarnate.id("spirit_channeler"),
		SpiritChannelerBlock::new,
		FabricBlockSettings.of(Material.METAL, MapColor.BROWN)
		.strength(5.0F, 10.0F)
		.sounds(BlockSoundGroup.METAL)
		.breakByTool(FabricToolTags.PICKAXES, 1),
		s -> s.group(Discarnate.ITEM_GROUP)
	);
	
	public static Block registerBlock(Identifier name, Function<Block.Settings, Block> blockFunc, Block.Settings blockSettings, UnaryOperator<Item.Settings> itemSettings)
	{
		return registerBlock(name, blockFunc, blockSettings, BlockItem::new, itemSettings);
	}
	
	public static Block registerBlock(Identifier name, Function<Block.Settings, Block> blockFunc, Block.Settings blockSettings, BiFunction<Block, Item.Settings, BlockItem> itemFunc, UnaryOperator<Item.Settings> itemSettings)
	{
		final Block block = registerBlock(name, blockFunc, blockSettings);
		
		Registry.register(Registry.ITEM, name, itemFunc.apply(block, itemSettings.apply(new Item.Settings())));
		
		return block;
	}
	
	public static Block registerBlock(Identifier name, Function<Block.Settings, Block> blockFunc, Block.Settings blockSettings)
	{
		return registerBlock(name, () -> blockFunc.apply(blockSettings));
	}
	
	public static Block registerBlock(Identifier name, Supplier<Block> blockSupplier)
	{
		return Registry.register(Registry.BLOCK, name, blockSupplier.get());
	}
	
	public static final BlockRegistrar INSTANCE = new BlockRegistrar();
	
	private BlockRegistrar()
	{
		
	}
}
