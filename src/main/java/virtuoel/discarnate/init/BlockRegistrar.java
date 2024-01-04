package virtuoel.discarnate.init;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.block.SpiritChannelerBlock;

public class BlockRegistrar
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Discarnate.MOD_ID);
	private static final DeferredRegister<Item> ITEMS = ItemRegistrar.ITEMS;
	
	public static final DeferredHolder<Block, SpiritChannelerBlock> SPIRIT_CHANNELER = BLOCKS.register(
		"spirit_channeler",
		() -> new SpiritChannelerBlock(
			Block.Settings.create()
			.mapColor(MapColor.BROWN)
			.strength(5.0F, 10.0F)
			.sounds(BlockSoundGroup.METAL)
			.requiresTool()
		)
	);
	
	static
	{
		ITEMS.register(
			SPIRIT_CHANNELER.getId().getPath(),
			() -> new BlockItem(
				SPIRIT_CHANNELER.get(), new Item.Settings()
			)
		);
	}
	
	public static final BlockRegistrar INSTANCE = new BlockRegistrar();
	
	private BlockRegistrar()
	{
		
	}
}
