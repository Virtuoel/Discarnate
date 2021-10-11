package virtuoel.discarnate.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface Task extends TriConsumer<ItemStack, PlayerEntity, BlockEntity>
{
	@Override
	void accept(@NotNull ItemStack s, @Nullable PlayerEntity p, @Nullable BlockEntity b);
}
