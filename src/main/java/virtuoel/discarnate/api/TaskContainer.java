package virtuoel.discarnate.api;

import java.util.List;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@ApiStatus.Experimental
@FunctionalInterface
public interface TaskContainer
{
	List<TaskAction> getContainedTasks(@NotNull ItemStack s, @NotNull PlayerEntity p, @Nullable BlockEntity b);
}
