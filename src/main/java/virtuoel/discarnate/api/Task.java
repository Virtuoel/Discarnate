package virtuoel.discarnate.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class Task implements TaskAction
{
	protected final TaskAction action;
	
	public Task(TaskAction action)
	{
		this.action = action;
	}
	
	@Override
	public void accept(@NotNull ItemStack s, @NotNull PlayerEntity p, @Nullable BlockEntity b)
	{
		action.accept(s, p, b);
	}
}
