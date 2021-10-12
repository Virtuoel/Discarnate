package virtuoel.discarnate.init;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.LiteralText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;
import virtuoel.discarnate.client.handler.ClientEventHandler;
import virtuoel.discarnate.task.ClientTask;

public class TaskRegistrar
{
	public static final Registry<Task> REGISTRY = FabricRegistryBuilder.createDefaulted(
		Task.class,
		Discarnate.id("tasks"),
		Registry.ITEM.getId(ItemRegistrar.BLANK_TASK)
	).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	
	private TaskRegistrar()
	{
		registerTask((i, p, t) ->
		{
			
		}, ItemRegistrar.BLANK_TASK);
		
		registerTask((i, p, t) ->
		{
			p.sendMessage(new LiteralText("" + i.getCount()), false);
		}, ItemRegistrar.INFO_TASK);
		
		registerTask((i, p, t) ->
		{
			try
			{
				Thread.sleep(i.getCount() * 50);
			}
			catch (InterruptedException e)
			{
				
			}
		}, ItemRegistrar.WAIT_TASK);
		
		registerTask((i, p, t) ->
		{
			final ItemStack itemStack = p.getInventory().dropSelectedItem(false);
			
			if (!p.world.isClient)
			{
				p.dropItem(itemStack, false, true);
			}
		}, ItemRegistrar.DROP_TASK);
		
		registerTask((i, p, t) ->
		{
			if (!p.isSpectator())
			{
				ItemStack itemStack = p.getStackInHand(Hand.OFF_HAND);
				p.setStackInHand(Hand.OFF_HAND, p.getStackInHand(Hand.MAIN_HAND));
				p.setStackInHand(Hand.MAIN_HAND, itemStack);
				p.clearActiveItem();
			}
		}, ItemRegistrar.SWAP_TASK);
		
		registerClientTask((i, p, t) ->
		{
			ClientEventHandler.addForwardTicks(i.getCount());
		}, ItemRegistrar.MOVE_FORWARD_TASK);
		
		registerClientTask((i, p, t) ->
		{
			ClientEventHandler.addBackwardTicks(i.getCount());
		}, ItemRegistrar.MOVE_BACKWARD_TASK);
		
		registerClientTask((i, p, t) ->
		{
			ClientEventHandler.addLeftTicks(i.getCount());
		}, ItemRegistrar.STRAFE_LEFT_TASK);
		
		registerClientTask((i, p, t) ->
		{
			ClientEventHandler.addRightTicks(i.getCount());
		}, ItemRegistrar.STRAFE_RIGHT_TASK);
		
		registerClientTask((i, p, t) ->
		{
			ClientEventHandler.addSneakTicks(i.getCount());
		}, ItemRegistrar.SNEAK_TASK);
		
		registerClientTask((i, p, t) ->
		{
			ClientEventHandler.addJumpTicks(i.getCount());
		}, ItemRegistrar.JUMP_TASK);
		
		registerClientTask((i, p, t) ->
		{
			ClientEventHandler.setForwardTicks(0);
			ClientEventHandler.setBackwardTicks(0);
			ClientEventHandler.setLeftTicks(0);
			ClientEventHandler.setRightTicks(0);
			ClientEventHandler.setSneakTicks(0);
			ClientEventHandler.setJumpTicks(0);
		}, ItemRegistrar.CANCEL_MOVEMENT_TASK);
		
		registerClientTask((i, p, t) ->
		{
			p.prevPitch = p.getPitch();
			p.setPitch(MathHelper.clamp(Math.round(p.getPitch()) - i.getCount(), -90, 90));
		}, ItemRegistrar.LOOK_UP_TASK);
		
		registerClientTask((i, p, t) ->
		{
			p.prevPitch = p.getPitch();
			p.setPitch(MathHelper.clamp(Math.round(p.getPitch()) + i.getCount(), -90, 90));
		}, ItemRegistrar.LOOK_DOWN_TASK);
		
		registerClientTask((i, p, t) ->
		{
			p.prevYaw = p.getYaw();
			p.prevHeadYaw = p.getHeadYaw();
			final float value = ((Math.round(p.getHeadYaw()) + 180 - i.getCount()) % 360) - 180;
			p.setYaw(value);
			p.setHeadYaw(value);
		}, ItemRegistrar.LOOK_LEFT_TASK);
		
		registerClientTask((i, p, t) ->
		{
			p.prevYaw = p.getYaw();
			p.prevHeadYaw = p.getHeadYaw();
			final float value = ((Math.round(p.getHeadYaw()) + 180 + i.getCount()) % 360) - 180;
			p.setYaw(value);
			p.setHeadYaw(value);
		}, ItemRegistrar.LOOK_RIGHT_TASK);
		
		registerClientTask((i, p, t) ->
		{
			p.prevPitch = p.getPitch();
			p.setPitch(0.0F);
		}, ItemRegistrar.FACE_HORIZON_TASK);
		
		registerClientTask((i, p, t) ->
		{
			p.prevYaw = p.getYaw();
			p.prevHeadYaw = p.getHeadYaw();
			final float value = MathHelper.clamp(Math.round(p.getHeadYaw() / 90.0F) * 90, -180, 180);
			p.setYaw(value);
			p.setHeadYaw(value);
		}, ItemRegistrar.FACE_CARDINAL_TASK);
		
		registerClientTask((i, p, t) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			ClientEventHandler.tryHoldKey(mc.options.keyAttack, i.getCount() * 50);
		}, ItemRegistrar.SWING_ITEM_TASK);
		
		registerClientTask((i, p, t) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			ClientEventHandler.tryHoldKey(mc.options.keyUse, i.getCount() * 50);
		}, ItemRegistrar.USE_ITEM_TASK);
		
		registerClientTask((i, p, t) ->
		{
			p.getInventory().selectedSlot = (i.getCount() - 1) % 9;
		}, ItemRegistrar.SWITCH_SLOT_TASK);
		
		registerTask(new ClientTask((i, p, t) ->
		{
			ClientEventHandler.setForwardTicks(0);
			ClientEventHandler.setBackwardTicks(0);
			ClientEventHandler.setLeftTicks(0);
			ClientEventHandler.setRightTicks(0);
			ClientEventHandler.setSneakTicks(0);
			ClientEventHandler.setJumpTicks(0);
			MinecraftClient mc = MinecraftClient.getInstance();
			ClientEventHandler.tryReleaseKey(mc.options.keyAttack);
			ClientEventHandler.tryReleaseKey(mc.options.keyUse);
		}), Discarnate.id("reset_channeler_task"));
		
		registerTask((i, p, t) ->
		{
			if (t instanceof SpiritChannelerBlockEntity)
			{
				((SpiritChannelerBlockEntity) t).deactivate();
			}
		}, ItemRegistrar.END_TASK);
		
		Task shulkerTask = (i, p, t) ->
		{
			NbtCompound nbt = i.getNbt();
			
			if (nbt != null && nbt.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE))
			{
				NbtCompound be = nbt.getCompound("BlockEntityTag");
				
				if (be.contains("Items", NbtElement.LIST_TYPE))
				{
					DefaultedList<ItemStack> stacks = DefaultedList.<ItemStack>ofSize(27, ItemStack.EMPTY);
					Inventories.readNbt(be, stacks);
					
					if (stacks.stream().anyMatch(s -> s.getItem() == Item.fromBlock(BlockRegistrar.SPIRIT_CHANNELER)))
					{
						for (ItemStack stack : stacks)
						{
							if (p != null && SpiritChannelerBlockEntity.canPlayerContinue(p) && SpiritChannelerBlockEntity.isActive(t.getWorld(), t.getPos()))
							{
								if (!stack.isEmpty())
								{
									REGISTRY.getOrEmpty(Registry.ITEM.getId(stack.getItem())).ifPresent(task -> task.accept(stack, p, t));
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
		
		registerTask((i, p, t) -> shulkerTask.accept(i, p, t), ShulkerBoxBlock.get(null));
		for (DyeColor color : DyeColor.values())
		{
			registerTask((i, p, t) -> shulkerTask.accept(i, p, t), ShulkerBoxBlock.get(color));
		}
	}
	
	public static Task registerTask(Task task, Identifier id)
	{
		return Registry.register(REGISTRY, id, task);
	}
	
	public static Task registerTask(Task task, ItemConvertible item)
	{
		return registerTask(task, Registry.ITEM.getId(item.asItem()));
	}
	
	public static Task registerClientTask(Task task, ItemConvertible item)
	{
		return registerTask(new ClientTask(task), item);
	}
	
	public static final TaskRegistrar INSTANCE = new TaskRegistrar();
}
