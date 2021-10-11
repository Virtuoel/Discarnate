package virtuoel.discarnate.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;

public class BlockEntityRegistrar
{
	public static final BlockEntityType<?> SPIRIT_CHANNELER = register("spirit_channeler",
		FabricBlockEntityTypeBuilder.create(SpiritChannelerBlockEntity::new, BlockRegistrar.SPIRIT_CHANNELER)
	);
	
	public static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder<T> builder)
	{
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, Discarnate.id(name), builder.build());
	}
	
	public static final BlockEntityRegistrar INSTANCE = new BlockEntityRegistrar();
	
	private BlockEntityRegistrar()
	{
		
	}
}