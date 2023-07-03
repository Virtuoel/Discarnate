package virtuoel.discarnate.mixin.client.compat1193minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(Screen.class)
public abstract class ScreenMixin implements DrawableHelperInvoker
{
	@Shadow(remap = false) // renderBackground
	public abstract void method_25420(MatrixStack matrices);
}
