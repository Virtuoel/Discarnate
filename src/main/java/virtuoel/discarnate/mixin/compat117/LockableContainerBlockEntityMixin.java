package virtuoel.discarnate.mixin.compat117;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.nbt.NbtCompound;

@Mixin(LockableContainerBlockEntity.class)
public abstract class LockableContainerBlockEntityMixin
{
	@Shadow(remap = false)
	protected NbtCompound method_11007(NbtCompound nbt)
	{
		return nbt;
	}
}
