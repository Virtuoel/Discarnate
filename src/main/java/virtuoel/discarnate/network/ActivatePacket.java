package virtuoel.discarnate.network;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neoforged.neoforge.network.NetworkEvent;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;

public class ActivatePacket
{
	BlockPos pos;
	boolean activating;
	
	public ActivatePacket(BlockPos pos, boolean activating)
	{
		this.pos = pos;
		this.activating = activating;
	}
	
	protected ActivatePacket(PacketByteBuf buf)
	{
		pos = buf.readBlockPos();
		activating = buf.readBoolean();
	}
	
	public static void handle(ActivatePacket msg, NetworkEvent.Context ctx)
	{
		ServerPlayerEntity player = ctx.getSender();
		
		ctx.enqueueWork(() ->
		{
			final World world = player.getEntityWorld();
			if (world.isChunkLoaded(msg.pos))
			{
				BlockEntity be = world.getBlockEntity(msg.pos);
				if (be instanceof SpiritChannelerBlockEntity)
				{
					SpiritChannelerBlockEntity channeler = ((SpiritChannelerBlockEntity) be);
					if (msg.activating)
					{
						if (!channeler.isActive())
						{
							channeler.activate(player);
						}
					}
					else if (channeler.isActive())
					{
						channeler.deactivate();
					}
				}
			}
		});
		
		ctx.setPacketHandled(true);
	}
	
	public void encode(PacketByteBuf buf)
	{
		
	}
}
