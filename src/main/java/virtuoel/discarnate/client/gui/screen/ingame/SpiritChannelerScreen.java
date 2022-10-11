package virtuoel.discarnate.client.gui.screen.ingame;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.DiscarnateConfig;
import virtuoel.discarnate.screen.SpiritChannelerScreenHandler;
import virtuoel.discarnate.util.I18nUtils;

@OnlyIn(Dist.CLIENT)
public class SpiritChannelerScreen extends HandledScreen<SpiritChannelerScreenHandler>
{
	private static final Identifier TEXTURE = Discarnate.id("textures/gui/container/spirit_channeler.png");
	
	public SpiritChannelerScreen(SpiritChannelerScreenHandler screenHandler, PlayerInventory playerInventory, Text text)
	{
		super(screenHandler, playerInventory, text);
		this.width = 176;
		this.height = 204;
		this.backgroundHeight = 204;
		this.playerInventoryTitleY = this.backgroundHeight - 94;
	}
	
	ButtonWidget confirmButton;
	
	private static final Text START_TEXT = I18nUtils.translate("gui.discarnate.spirit_channeler.start", "Start");
	private static final Text STOP_TEXT = I18nUtils.translate("gui.discarnate.spirit_channeler.stop", "Stop");
	
	@Override
	public void init()
	{
		super.init();
		
		final boolean active = handler.isActive();
		
		this.titleX = ((this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2) - 26;
		this.confirmButton = new ButtonWidget(this.x + 124, this.y + 52, 40, 20, active ? STOP_TEXT : START_TEXT, this::confirmAction);
		this.confirmButton.active = active || (this.client.player.experienceLevel >= DiscarnateConfig.COMMON.minLevel.get() && this.client.player.experienceLevel >= DiscarnateConfig.COMMON.levelCost.get()) || this.client.player.isCreative();
		this.addDrawableChild(confirmButton);
	}
	
	@Override
	public void handledScreenTick()
	{
		super.handledScreenTick();
		
		final boolean active = handler.isActive();
		
		this.confirmButton.setMessage(active ? STOP_TEXT : START_TEXT);
		this.confirmButton.active = active || (this.client.player.experienceLevel >= DiscarnateConfig.COMMON.minLevel.get() && this.client.player.experienceLevel >= DiscarnateConfig.COMMON.levelCost.get()) || this.client.player.isCreative();
	}
	
	private void confirmAction(ButtonWidget button)
	{
		client.interactionManager.clickButton(handler.syncId, 0);
	}
	
	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY)
	{
		super.drawForeground(matrices, mouseX, mouseY);
		if (confirmButton.isHovered() && !confirmButton.isFocused())
		{
			confirmButton.renderTooltip(matrices, mouseX - this.x, mouseY - this.y);
		}
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
}
