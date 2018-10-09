package virtuoel.discarnate.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Task extends IForgeRegistryEntry.Impl<Task> implements ITask
{
	public static final IForgeRegistry<Task> REGISTRY = GameRegistry.findRegistry(Task.class);
	
	ITask task;
	
	public Task(ITask task)
	{
		this.task = task;
	}
	
	@Override
	public void accept(ItemStack k, EntityPlayer v, TileEntity s)
	{
		task.accept(k, v, s);
	}
}
