package virtuoel.discarnate.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.inventory.ContainerSpiritChanneler;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

@SideOnly(Side.CLIENT)
public class GuiSpiritChanneler extends GuiContainer
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Discarnate.MOD_ID, "textures/gui/container/spirit_channeler.png");
	
	private IInventory playerInventory;
	private TileEntitySpiritChanneler tileEntity;
	
	public GuiSpiritChanneler(EntityPlayer player, TileEntitySpiritChanneler tileEntity)
	{
		super(new ContainerSpiritChanneler(player, tileEntity));
		this.playerInventory = player.inventory;
		this.tileEntity = tileEntity;
		this.xSize = 176;
		this.ySize = 204;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = this.tileEntity.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}
}
