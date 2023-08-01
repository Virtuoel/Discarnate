package virtuoel.discarnate.client.gui.screen.ingame;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.LockButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.DiscarnateConfig;
import virtuoel.discarnate.screen.SpiritChannelerScreenHandler;
import virtuoel.discarnate.util.I18nUtils;
import virtuoel.discarnate.util.ReflectionUtils;

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
	LockButtonWidget lockButton;
	
	private static final Text START_TEXT = I18nUtils.translate("gui.discarnate.spirit_channeler.start", "Start");
	private static final Text STOP_TEXT = I18nUtils.translate("gui.discarnate.spirit_channeler.stop", "Stop");
	
	@Override
	public void init()
	{
		super.init();
		
		final boolean creative = this.client.player.isCreative();
		
		final boolean active = this.handler.isActive();
		final boolean locked = this.handler.isLocked();
		
		this.titleX = ((this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2) - 26;
		
		this.confirmButton = ReflectionUtils.Client.buildButtonWidget(this.x + 122, this.y + 52, 40, 20, active ? STOP_TEXT : START_TEXT, this::confirmAction);
		this.confirmButton.active = active || (this.client.player.experienceLevel >= DiscarnateConfig.COMMON.minLevel.get() && this.client.player.experienceLevel >= DiscarnateConfig.COMMON.levelCost.get()) || creative;
		this.addDrawableChild(this.confirmButton);
		
		this.lockButton = new LockButtonWidget(this.x + 132, this.y + 88, this::lockAction);
		this.lockButton.setLocked(locked);
		this.lockButton.active = !active && creative;
		if (this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE)
		{
			this.addDrawableChild(this.lockButton);
		}
	}
	
	@Override
	public void handledScreenTick()
	{
		super.handledScreenTick();
		
		final boolean creative = this.client.player.isCreative();
		
		final boolean active = this.handler.isActive();
		final boolean locked = this.handler.isLocked();
		
		this.confirmButton.setMessage(active ? STOP_TEXT : START_TEXT);
		this.confirmButton.active = active || (this.client.player.experienceLevel >= DiscarnateConfig.COMMON.minLevel.get() && this.client.player.experienceLevel >= DiscarnateConfig.COMMON.levelCost.get()) || creative;
		
		this.lockButton.setLocked(locked);
		this.lockButton.active = !active && creative;
	}
	
	private void confirmAction(ButtonWidget button)
	{
		this.client.interactionManager.clickButton(this.handler.syncId, 0);
	}
	
	private void lockAction(ButtonWidget button)
	{
		if (this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE)
		{
			this.client.interactionManager.clickButton(this.handler.syncId, 1);
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
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		final int i = (this.width - this.backgroundWidth) / 2;
		final int j = (this.height - this.backgroundHeight) / 2;
		drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
}
