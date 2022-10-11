package virtuoel.discarnate.init;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;

public class BlockEntityRegistrar
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Discarnate.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<?>> SPIRIT_CHANNELER = BLOCK_ENTITY_TYPES.register("spirit_channeler",
		() -> BlockEntityType.Builder.create(SpiritChannelerBlockEntity::new, BlockRegistrar.SPIRIT_CHANNELER.get()).build(null)
	);
}
