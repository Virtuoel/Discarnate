package virtuoel.discarnate.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface ITask extends TriConsumer<ItemStack, EntityPlayer, TileEntity>
{
	
}
