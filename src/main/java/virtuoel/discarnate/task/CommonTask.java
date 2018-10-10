package virtuoel.discarnate.task;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import virtuoel.discarnate.api.ITask;
import virtuoel.discarnate.api.Task;

public class CommonTask extends Task
{
	ITask task;
	
	public CommonTask(ITask task)
	{
		this.task = task;
	}
	
	@Override
	public void accept(@Nonnull ItemStack s, @Nullable EntityPlayer p, @Nullable TileEntity t)
	{
		task.accept(s, p, t);
	}
}
