package virtuoel.discarnate.mixin.client.compat1193minus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import virtuoel.discarnate.client.gui.screen.ingame.SpiritChannelerScreen;
import virtuoel.discarnate.util.ReflectionUtils;

@Mixin(SpiritChannelerScreen.class)
public abstract class SpiritChannelerScreenMixin extends HandledScreenMixin
{
	@Shadow
	private static @Final @Mutable Identifier TEXTURE;
	
	@Shadow
	ButtonWidget confirmButton;
	
	@Override
	protected void method_2388(MatrixStack matrices, int mouseX, int mouseY)
	{
		super.method_2388(matrices, mouseX, mouseY);
		ReflectionUtils.Client.renderClickableWidgetTooltip(this.confirmButton, matrices, mouseX - this.x, mouseY - this.y);
	}
	
	@Override
	public void method_25394(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.method_25420(matrices);
		super.method_25394(matrices, mouseX, mouseY, delta);
		this.method_2380(matrices, mouseX, mouseY);
	}
	
	@Override
	protected void method_2389(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		final SpiritChannelerScreen self = (SpiritChannelerScreen) (Object) this;
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		final int i = (self.width - this.backgroundWidth) / 2;
		final int j = (self.height - this.backgroundHeight) / 2;
		this.callMethod_25302(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
}
