package virtuoel.discarnate.client.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.LockButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.init.GameRuleRegistrar;
import virtuoel.discarnate.screen.SpiritChannelerScreenHandler;
import virtuoel.discarnate.util.I18nUtils;
import virtuoel.discarnate.util.ReflectionUtils;

@Environment(EnvType.CLIENT)
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
		
		final GameRules r = this.client.world.getGameRules();
		final boolean creative = this.client.player.isCreative();
		
		final boolean active = this.handler.isActive();
		final boolean locked = this.handler.isLocked();
		
		this.titleX = ((this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2) - 26;
		
		this.confirmButton = ReflectionUtils.Client.buildButtonWidget(this.x + 122, this.y + 52, 40, 20, active ? STOP_TEXT : START_TEXT, this::confirmAction);
		this.confirmButton.active = active || (this.client.player.experienceLevel >= r.getInt(GameRuleRegistrar.MIN_LEVEL) && this.client.player.experienceLevel >= r.getInt(GameRuleRegistrar.LEVEL_COST)) || creative;
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
		
		final GameRules r = this.client.world.getGameRules();
		final boolean creative = this.client.player.isCreative();
		
		final boolean active = this.handler.isActive();
		final boolean locked = this.handler.isLocked();
		
		this.confirmButton.setMessage(active ? STOP_TEXT : START_TEXT);
		this.confirmButton.active = active || (this.client.player.experienceLevel >= r.getInt(GameRuleRegistrar.MIN_LEVEL) && this.client.player.experienceLevel >= r.getInt(GameRuleRegistrar.LEVEL_COST)) || creative;
		
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
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		ReflectionUtils.Client.renderBackground(this, context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}
	
	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY)
	{
		final int i = (this.width - this.backgroundWidth) / 2;
		final int j = (this.height - this.backgroundHeight) / 2;
		context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
}
