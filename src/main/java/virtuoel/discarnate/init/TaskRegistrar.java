package virtuoel.discarnate.init;

import java.util.Optional;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.ITask;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.client.handler.ClientEventHandler;
import virtuoel.discarnate.reference.DiscarnateConfig;
import virtuoel.discarnate.task.ClientTask;
import virtuoel.discarnate.task.CommonTask;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

@EventBusSubscriber(modid = Discarnate.MOD_ID)
public class TaskRegistrar
{
	public static IForgeRegistry<Task> REGISTRY;
	
	@SubscribeEvent
	public static void registerTasks(RegistryEvent.Register<Task> event)
	{
		event.getRegistry().registerAll(
			createTask((i, p, t) ->
			{}, ItemRegistrar.BLANK_TASK),
			
			createTask((i, p, t) ->
			{
				p.sendMessage(new TextComponentString("" + i.getCount()));
			}, ItemRegistrar.INFO_TASK),
			
			createTask((i, p, t) ->
			{
				try
				{
					Thread.sleep(i.getCount() * 50);
				}
				catch(InterruptedException e)
				{}
			}, ItemRegistrar.WAIT_TASK),
			
			createTask((i, p, t) ->
			{
				ItemStack stack = p.inventory.getCurrentItem();
				
				if(!stack.isEmpty() && stack.getItem().onDroppedByPlayer(stack, p))
				{
					ForgeHooks.onPlayerTossEvent(p, p.inventory.decrStackSize(p.inventory.currentItem, i.getCount()), true);
				}
			}, ItemRegistrar.DROP_TASK),
			
			createTask((i, p, t) ->
			{
				if(!p.isSpectator())
				{
					ItemStack itemstack = p.getHeldItem(EnumHand.OFF_HAND);
					p.setHeldItem(EnumHand.OFF_HAND, p.getHeldItem(EnumHand.MAIN_HAND));
					p.setHeldItem(EnumHand.MAIN_HAND, itemstack);
				}
			}, ItemRegistrar.SWAP_TASK),
			
			createClientTask((i, p, t) ->
			{
				ClientEventHandler.addForwardTicks(i.getCount());
			}, ItemRegistrar.MOVE_FORWARD_TASK),
			
			createClientTask((i, p, t) ->
			{
				ClientEventHandler.addBackwardTicks(i.getCount());
			}, ItemRegistrar.MOVE_BACKWARD_TASK),
			
			createClientTask((i, p, t) ->
			{
				ClientEventHandler.addLeftTicks(i.getCount());
			}, ItemRegistrar.STRAFE_LEFT_TASK),
			
			createClientTask((i, p, t) ->
			{
				ClientEventHandler.addRightTicks(i.getCount());
			}, ItemRegistrar.STRAFE_RIGHT_TASK),
			
			createClientTask((i, p, t) ->
			{
				ClientEventHandler.addSneakTicks(i.getCount());
			}, ItemRegistrar.SNEAK_TASK),
			
			createClientTask((i, p, t) ->
			{
				ClientEventHandler.addJumpTicks(i.getCount());
			}, ItemRegistrar.JUMP_TASK),
			
			createClientTask((i, p, t) ->
			{
				ClientEventHandler.setForwardTicks(0);
				ClientEventHandler.setBackwardTicks(0);
				ClientEventHandler.setLeftTicks(0);
				ClientEventHandler.setRightTicks(0);
				ClientEventHandler.setSneakTicks(0);
				ClientEventHandler.setJumpTicks(0);
			}, ItemRegistrar.CANCEL_MOVEMENT_TASK),
			
			createClientTask((i, p, t) ->
			{
				p.prevRotationPitch = p.rotationPitch;
				p.rotationPitch = MathHelper.clamp(Math.round(p.rotationPitch) - i.getCount(), -90, 90);
			}, ItemRegistrar.LOOK_UP_TASK),
			
			createClientTask((i, p, t) ->
			{
				p.prevRotationPitch = p.rotationPitch;
				p.rotationPitch = MathHelper.clamp(Math.round(p.rotationPitch) + i.getCount(), -90, 90);
			}, ItemRegistrar.LOOK_DOWN_TASK),
			
			createClientTask((i, p, t) ->
			{
				p.prevRotationYaw = p.rotationYaw;
				p.rotationYaw = ((Math.round(p.rotationYaw) + 180 - i.getCount()) % 360) - 180;
			}, ItemRegistrar.LOOK_LEFT_TASK),
			
			createClientTask((i, p, t) ->
			{
				p.prevRotationYaw = p.rotationYaw;
				p.rotationYaw = ((Math.round(p.rotationYaw) + 180 + i.getCount()) % 360) - 180;
			}, ItemRegistrar.LOOK_RIGHT_TASK),
			
			createClientTask((i, p, t) ->
			{
				p.prevRotationPitch = p.rotationPitch;
				p.rotationPitch = 0.0F;
			}, ItemRegistrar.FACE_HORIZON_TASK),
			
			createClientTask((i, p, t) ->
			{
				p.prevRotationYaw = MathHelper.normalizeAngle(Math.round(p.rotationYaw) + 180, 360) - 180;
				p.rotationYaw = MathHelper.clamp(Math.round(p.prevRotationYaw / 90.0F) * 90, -180, 180);
			}, ItemRegistrar.FACE_CARDINAL_TASK),
			
			createClientTask((i, p, t) ->
			{
				ClientEventHandler.tryHoldKey(Minecraft.getMinecraft().gameSettings.keyBindAttack, i.getCount() * 50);
			}, ItemRegistrar.SWING_ITEM_TASK),
			
			createClientTask((i, p, t) ->
			{
				ClientEventHandler.tryHoldKey(Minecraft.getMinecraft().gameSettings.keyBindUseItem, i.getCount() * 50);
			}, ItemRegistrar.USE_ITEM_TASK),
			
			createClientTask((i, p, t) ->
			{
				p.inventory.currentItem = (i.getCount() - 1) % 9;
			}, ItemRegistrar.SWITCH_SLOT_TASK),
			
			new ClientTask((i, p, t) ->
			{
				ClientEventHandler.setForwardTicks(0);
				ClientEventHandler.setBackwardTicks(0);
				ClientEventHandler.setLeftTicks(0);
				ClientEventHandler.setRightTicks(0);
				ClientEventHandler.setSneakTicks(0);
				ClientEventHandler.setJumpTicks(0);
				ClientEventHandler.tryReleaseKey(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode());
				ClientEventHandler.tryReleaseKey(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode());
			}).setRegistryName(Discarnate.MOD_ID + ":reset_channeler_task"),
			
			createTask((i, p, t) ->
			{
				if(t instanceof TileEntitySpiritChanneler)
				{
					((TileEntitySpiritChanneler) t).deactivate();
				}
			}, ItemRegistrar.END_TASK)
		);
		
		ITask shulkerTask = (i, p, t) ->
		{
			NBTTagCompound nbttagcompound = i.getTagCompound();
			
			if(nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag", Constants.NBT.TAG_COMPOUND))
			{
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");
				
				if(nbttagcompound1.hasKey("Items", Constants.NBT.TAG_LIST))
				{
					NonNullList<ItemStack> stacks = NonNullList.<ItemStack> withSize(27, ItemStack.EMPTY);
					ItemStackHelper.loadAllItems(nbttagcompound1, stacks);
					
					if(stacks.stream().anyMatch(s -> s.getItem() == Item.getItemFromBlock(BlockRegistrar.SPIRIT_CHANNELER)))
					{
						for(ItemStack stack : stacks)
						{
							if(p != null && !p.isDead && TileEntitySpiritChanneler.isActive(t.getWorld(), t.getPos()))
							{
								if(!stack.isEmpty())
								{
									Optional.ofNullable(REGISTRY.getValue(stack.getItem().getRegistryName())).ifPresent(task -> task.accept(stack, p, t));
								}
							}
							else
							{
								break;
							}
						}
					}
				}
			}
		};
		
		ModContainer lastContainer = Loader.instance().activeModContainer();
		Loader.instance().setActiveModContainer(Loader.instance().getMinecraftModContainer());
		for(EnumDyeColor color : EnumDyeColor.values())
		{
			event.getRegistry().register(createTask(shulkerTask, BlockShulkerBox.getBlockByColor(color)));
		}
		Loader.instance().setActiveModContainer(lastContainer);
	}
	
	@SubscribeEvent
	public static void registerTaskRegistry(RegistryEvent.NewRegistry event)
	{
		REGISTRY = new RegistryBuilder<Task>()
			.setName(new ResourceLocation(Discarnate.MOD_ID, "tasks"))
			.setType(Task.class)
			.setDefaultKey(new ResourceLocation("empty"))
			.create();
	}
	
	@SubscribeEvent
	public static void onMissingTasks(RegistryEvent.MissingMappings<Task> event)
	{
		if(DiscarnateConfig.ignoreMissingTasks)
		{
			event.getMappings().forEach(RegistryEvent.MissingMappings.Mapping::ignore);
		}
	}
	
	public static <T extends IForgeRegistryEntry<?>> Task createTask(ITask task, T name)
	{
		return new CommonTask(task).setRegistryName(name.getRegistryName());
	}
	
	public static <T extends IForgeRegistryEntry<?>> Task createClientTask(ITask task, T name)
	{
		return new ClientTask(task).setRegistryName(name.getRegistryName());
	}
}
