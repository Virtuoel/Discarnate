package virtuoel.discarnate.init;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;

public class BlockEntityRegistrar
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Discarnate.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<?>> SPIRIT_CHANNELER = BLOCK_ENTITIES.register("spirit_channeler",
		() -> BlockEntityType.Builder.create(SpiritChannelerBlockEntity::new, BlockRegistrar.SPIRIT_CHANNELER.get()).build(null)
	);
}
