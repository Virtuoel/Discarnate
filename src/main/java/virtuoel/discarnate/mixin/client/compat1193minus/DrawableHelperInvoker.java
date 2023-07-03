package virtuoel.discarnate.mixin.client.compat1193minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.util.math.MatrixStack;

@Mixin(targets = "net.minecraft.class_332", remap = false)
public interface DrawableHelperInvoker
{
	@Invoker(value = "method_25302", remap = false)
	void callMethod_25302(MatrixStack matrices, int i, int j, int k, int l, int backgroundWidth, int backgroundHeight);
}
