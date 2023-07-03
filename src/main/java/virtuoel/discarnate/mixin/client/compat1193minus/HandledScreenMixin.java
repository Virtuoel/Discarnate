package virtuoel.discarnate.mixin.client.compat1193minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends ScreenMixin
{
	@Shadow
	protected int backgroundWidth;
	
	@Shadow
	protected int backgroundHeight;
	
	@Shadow
	protected int x;
	
	@Shadow
	protected int y;
	
	@Shadow(remap = false) // drawForeground
	protected void method_2388(MatrixStack matrices, int mouseX, int mouseY)
	{
		throw new AbstractMethodError();
	}
	
	@Shadow(remap = false) // render
	public void method_25394(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		throw new AbstractMethodError();
	}
	
	@Shadow(remap = false) // drawMouseoverTooltip
	protected abstract void method_2380(MatrixStack matrices, int mouseX, int mouseY);
	
	@Shadow(remap = false) // drawBackground
	protected abstract void method_2389(MatrixStack matrices, float delta, int mouseX, int mouseY);
}
