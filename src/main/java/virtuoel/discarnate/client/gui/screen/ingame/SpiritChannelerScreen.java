package virtuoel.discarnate.client.gui.screen.ingame;

import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;
import virtuoel.discarnate.reference.DiscarnateConfig;
import virtuoel.discarnate.screen.SpiritChannelerScreenHandler;

@Environment(EnvType.CLIENT)
public class SpiritChannelerScreen extends HandledScreen<SpiritChannelerScreenHandler>
{
	private static final Identifier TEXTURE = Discarnate.id("textures/gui/container/spirit_channeler.png");
	
	private SpiritChannelerBlockEntity blockEntity;
	
	public SpiritChannelerScreen(SpiritChannelerScreenHandler screenHandler, PlayerInventory playerInventory, Text text)
	{
		super(screenHandler, playerInventory, text);
		this.blockEntity = handler.blockEntity;
		this.width = 176;
		this.height = 204;
		this.backgroundHeight = 204;
		this.playerInventoryTitleY = this.backgroundHeight - 94;
	}
	
	ButtonWidget confirmButton;
	boolean active = false;
	
	private static final Text START_TEXT = new TranslatableText("gui.discarnate.spirit_channeler.start");
	private static final Text STOP_TEXT = new TranslatableText("gui.discarnate.spirit_channeler.stop");
	
	@Override
	public void init()
	{
		super.init();
		this.titleX = ((this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2) - 26;
		this.active = blockEntity.isActive();
		this.confirmButton = new ButtonWidget(this.x + 124, this.y + 52, 40, 20, active ? STOP_TEXT : START_TEXT, this::confirmAction);
		this.confirmButton.active = this.active || (this.client.player.experienceLevel >= DiscarnateConfig.minExpLevel && this.client.player.experienceLevel >= DiscarnateConfig.expLevelCost) || this.client.player.isCreative();
		this.addDrawableChild(confirmButton);
	}
	
	@Override
	public void handledScreenTick()
	{
		super.handledScreenTick();
		
		this.active = blockEntity.isActive();
		
		this.confirmButton.setMessage(active ? STOP_TEXT : START_TEXT);
		this.confirmButton.active = this.active || (this.client.player.experienceLevel >= DiscarnateConfig.minExpLevel && this.client.player.experienceLevel >= DiscarnateConfig.expLevelCost) || this.client.player.isCreative();
	}
	
	private void confirmAction(ButtonWidget button)
	{
		if (button.active)
		{
			final PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
			buffer.writeBlockPos(blockEntity.getPos());
			buffer.writeBoolean(!active);
			ClientPlayNetworking.send(Discarnate.ACTIVATE_PACKET, buffer);
			if (!active)
			{
				Optional.ofNullable(this.client.player).ifPresent(ClientPlayerEntity::closeHandledScreen);
			}
		}
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
