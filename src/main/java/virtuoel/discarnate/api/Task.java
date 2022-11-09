package virtuoel.discarnate.api;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class Task implements TaskAction, TaskContainer
{
	@Deprecated
	protected final TaskAction action;
	@ApiStatus.Experimental
	protected final TaskContainer container;
	
	public Task(@NotNull TaskAction action)
	{
		this((TaskContainer) (s, p, b) -> Collections.singletonList(action));
	}
	
	@ApiStatus.Experimental
	public Task(@NotNull TaskContainer container)
	{
		this.container = container;
		this.action = this::accept;
	}
	
	@Override
	public void accept(@NotNull ItemStack s, @NotNull PlayerEntity p, @Nullable BlockEntity b)
	{
		for (final TaskAction task : getContainedTasks(s, p, b))
		{
			task.accept(s, p, b);
		}
	}
	
	@Override
	@ApiStatus.Experimental
	public List<TaskAction> getContainedTasks(@NotNull ItemStack s, @NotNull PlayerEntity p, @Nullable BlockEntity b)
	{
		return container.getContainedTasks(s, p, b);
	}
}
