package virtuoel.discarnate.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

public class CPacketActivateChanneler implements IMessage
{
	BlockPos pos;
	boolean activating = false;
	
	public CPacketActivateChanneler()
	{
		pos = BlockPos.ORIGIN;
		activating = false;
	}
	
	public CPacketActivateChanneler(BlockPos pos, boolean activating)
	{
		this.pos = pos;
		this.activating = activating;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		activating = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(activating);
	}
	
	public static class Handler implements IMessageHandler<CPacketActivateChanneler, IMessage>
	{
		@Override
		public IMessage onMessage(CPacketActivateChanneler message, MessageContext ctx)
		{
			EntityPlayerMP activator = ctx.getServerHandler().player;
			WorldServer world = activator.getServerWorld();
			world.addScheduledTask(() ->
			{
				if(world.isBlockLoaded(message.pos))
				{
					TileEntity te = world.getTileEntity(message.pos);
					if(te instanceof TileEntitySpiritChanneler)
					{
						TileEntitySpiritChanneler channeler = ((TileEntitySpiritChanneler) te);
						if(message.activating)
						{
							if(!channeler.isActive())
							{
								channeler.activate(activator);
							}
						}
						else if(channeler.isActive())
						{
							channeler.deactivate();
						}
					}
				}
			});
			return null;
		}
	}
}
