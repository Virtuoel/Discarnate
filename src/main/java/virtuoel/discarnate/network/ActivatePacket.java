package virtuoel.discarnate.network;

import java.util.function.Supplier;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.network.NetworkEvent;
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
	
	public static void handle(ActivatePacket msg, Supplier<NetworkEvent.Context> ctx)
	{
		ServerPlayerEntity player = ctx.get().getSender();
		
		ctx.get().enqueueWork(() ->
		{
			if (player.world.isChunkLoaded(msg.pos))
			{
				BlockEntity be = player.world.getBlockEntity(msg.pos);
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
		
		ctx.get().setPacketHandled(true);
	}
	
	public void encode(PacketByteBuf buf)
	{
		
	}
}
