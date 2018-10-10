package virtuoel.discarnate.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface ITask extends TriConsumer<ItemStack, EntityPlayer, TileEntity>
{
	@Override
	void accept(@Nonnull ItemStack s, @Nullable EntityPlayer p, @Nullable TileEntity t);
}
