package virtuoel.discarnate.task;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.network.NetworkDirection;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.api.TaskAction;
import virtuoel.discarnate.network.DiscarnatePacketHandler;
import virtuoel.discarnate.network.TaskPacket;

public class ClientTask extends Task
{
	public ClientTask(TaskAction action)
	{
		super(action);
	}
	
	@Override
	public void accept(@NotNull ItemStack s, @Nullable PlayerEntity p, @Nullable BlockEntity b)
	{
		if (p instanceof ServerPlayerEntity && !p.getEntityWorld().isClient)
		{
			TaskPacket packet = new TaskPacket(
				this,
				getPosFromBlockEntity(b),
				s,
				getWorldIdFromBlockEntity(b)
			);
			
			((ServerPlayerEntity) p).networkHandler.sendPacket(DiscarnatePacketHandler.INSTANCE.toVanillaPacket(packet, NetworkDirection.PLAY_TO_CLIENT));
		}
		else
		{
			super.accept(s, p, b);
		}
	}
	
	private static BlockPos getPosFromBlockEntity(BlockEntity be)
	{
		return be != null ? be.getPos() : BlockPos.ORIGIN;
	}
	
	private static Identifier getWorldIdFromBlockEntity(BlockEntity be)
	{
		if (be != null)
		{
			return be.getWorld().getRegistryKey().getValue();
		}
		
		return World.OVERWORLD.getValue();
	}
}
