package virtuoel.discarnate.init;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.block.SpiritChannelerBlock;

public class BlockRegistrar
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Discarnate.MOD_ID);
	private static final DeferredRegister<Item> ITEMS = ItemRegistrar.ITEMS;
	
	public static final RegistryObject<Block> SPIRIT_CHANNELER = BLOCKS.register(
		"spirit_channeler",
		() -> new SpiritChannelerBlock(
			Block.Settings.of(Material.METAL, MapColor.BROWN)
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
				SPIRIT_CHANNELER.get(), new Item.Settings().group(Discarnate.ITEM_GROUP)
			)
		);
	}
	
	public static final BlockRegistrar INSTANCE = new BlockRegistrar();
	
	private BlockRegistrar()
	{
		
	}
}
