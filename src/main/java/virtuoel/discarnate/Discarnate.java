package virtuoel.discarnate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import virtuoel.discarnate.init.BlockRegistrar;
import virtuoel.discarnate.init.ItemRegistrar;
import virtuoel.discarnate.init.ScreenHandlerRegistrar;
import virtuoel.discarnate.init.TaskRegistrar;
import virtuoel.discarnate.init.TileEntityRegistrar;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

public class Discarnate implements ModInitializer
{
	public static final String MOD_ID = "discarnate";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
		id("general"))
		.icon(() -> new ItemStack(BlockRegistrar.SPIRIT_CHANNELER))
		.build();
	
	@Override
	public void onInitialize()
	{
		BlockRegistrar.INSTANCE.getClass();
		ItemRegistrar.INSTANCE.getClass();
		ScreenHandlerRegistrar.INSTANCE.getClass();
		TaskRegistrar.INSTANCE.getClass();
		TileEntityRegistrar.INSTANCE.getClass();
		
		ServerPlayNetworking.registerGlobalReceiver(
			ACTIVATE_PACKET,
			(server, player, handler, buf, responseSender) ->
			{
				BlockPos pos = buf.readBlockPos();
				boolean activating = buf.readBoolean();
				server.execute(() ->
				{
					if(player.world.isChunkLoaded(pos))
					{
						BlockEntity te = player.world.getBlockEntity(pos);
						if(te instanceof TileEntitySpiritChanneler)
						{
							TileEntitySpiritChanneler channeler = ((TileEntitySpiritChanneler) te);
							if(activating)
							{
								if(!channeler.isActive())
								{
									channeler.activate(player);
								}
							}
							else if(channeler.isActive())
							{
								channeler.deactivate();
							}
						}
					}
				});
			}
		);
	}
	
	public static Identifier id(String path)
	{
		return new Identifier(MOD_ID, path);
	}
	
	public static Identifier id(String path, String... paths)
	{
		return id(paths.length == 0 ? path : path + "/" + String.join("/", paths));
	}
	
	public static final Identifier ACTIVATE_PACKET = id("activate");
	public static final Identifier TASK_PACKET = id("task");
}
