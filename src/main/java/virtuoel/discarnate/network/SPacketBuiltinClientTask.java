package virtuoel.discarnate.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketBuiltinClientTask implements IMessage
{
	ResourceLocation name;
	
	public SPacketBuiltinClientTask()
	{
		this.name = new ResourceLocation("air");
	}
	
	public SPacketBuiltinClientTask(ResourceLocation name)
	{
		this.name = name;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		name = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, name.toString());
	}
	
	public static class Handler implements IMessageHandler<SPacketBuiltinClientTask, IMessage>
	{
		@Override
		public IMessage onMessage(SPacketBuiltinClientTask message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				// TODO
			});
			return null;
		}
	}
}
