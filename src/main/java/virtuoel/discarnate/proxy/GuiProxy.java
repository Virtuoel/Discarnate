package virtuoel.discarnate.proxy;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import virtuoel.discarnate.client.gui.GuiSpiritChanneler;
import virtuoel.discarnate.inventory.ContainerSpiritChanneler;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

public class GuiProxy implements IGuiHandler
{
	public static final int SPIRIT_CHANNELER = 0;
	
	@Override
	@Nullable
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		if(world.isBlockLoaded(pos))
		{
			TileEntity te = world.getTileEntity(pos);
			switch(ID)
			{
				case SPIRIT_CHANNELER:
					if(te instanceof TileEntitySpiritChanneler)
					{
						TileEntitySpiritChanneler inventoryTE = (TileEntitySpiritChanneler) te;
						return new ContainerSpiritChanneler(player, inventoryTE);
					}
					break;
				default:
					break;
			}
		}
		return null;
	}
	
	@Override
	@Nullable
	public GuiScreen getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		if(world.isBlockLoaded(pos))
		{
			TileEntity te = world.getTileEntity(pos);
			switch(ID)
			{
				case SPIRIT_CHANNELER:
					if(te instanceof TileEntitySpiritChanneler)
					{
						TileEntitySpiritChanneler inventoryTE = (TileEntitySpiritChanneler) te;
						return new GuiSpiritChanneler(player, inventoryTE);
					}
					break;
				default:
					break;
			}
		}
		return null;
	}
}
