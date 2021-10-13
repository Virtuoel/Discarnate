package virtuoel.discarnate.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface TaskAction
{
	void accept(@NotNull ItemStack s, @NotNull PlayerEntity p, @Nullable BlockEntity b);
}
