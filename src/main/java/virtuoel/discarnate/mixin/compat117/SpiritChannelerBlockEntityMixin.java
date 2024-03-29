package virtuoel.discarnate.mixin.compat117;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;

@Mixin(SpiritChannelerBlockEntity.class)
public abstract class SpiritChannelerBlockEntityMixin extends LockableContainerBlockEntityMixin
{
	@Shadow(remap = false)
	DefaultedList<ItemStack> inventory;
	
	@Shadow(remap = false)
	abstract boolean isLocked();
	
	@Override
	public NbtCompound method_11007(NbtCompound nbt)
	{
		super.method_11007(nbt);
		Inventories.writeNbt(nbt, this.inventory);
		nbt.putBoolean("Locked", isLocked());
		return nbt;
	}
}
