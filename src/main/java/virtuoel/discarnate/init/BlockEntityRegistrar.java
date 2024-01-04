package virtuoel.discarnate.init;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;

public class BlockEntityRegistrar
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Discarnate.MOD_ID);
	
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpiritChannelerBlockEntity>> SPIRIT_CHANNELER = BLOCK_ENTITY_TYPES.register("spirit_channeler",
		() -> BlockEntityType.Builder.create(SpiritChannelerBlockEntity::new, BlockRegistrar.SPIRIT_CHANNELER.get()).build(null)
	);
}
