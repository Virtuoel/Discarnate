package virtuoel.discarnate.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.api.TaskAction;
import virtuoel.discarnate.api.TaskContainer;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;
import virtuoel.discarnate.client.option.KeyBindingUtils;
import virtuoel.discarnate.task.ClientTask;
import virtuoel.discarnate.util.I18nUtils;

public class TaskRegistrar
{
	public static final Registry<Task> REGISTRY = FabricRegistryBuilder.createDefaulted(
		Task.class,
		Discarnate.id("tasks"),
		Registry.ITEM.getId(ItemRegistrar.BLANK_TASK)
	).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	
	private TaskRegistrar()
	{
		registerTask((s, p, b) ->
		{
			
		}, ItemRegistrar.BLANK_TASK);
		
		registerTask((s, p, b) ->
		{
			p.sendMessage(I18nUtils.literal("" + s.getCount()), false);
		}, ItemRegistrar.INFO_TASK);
		
		registerTask((s, p, b) ->
		{
			try
			{
				Thread.sleep(s.getCount() * 50);
			}
			catch (InterruptedException e)
			{
				
			}
		}, ItemRegistrar.WAIT_TASK);
		
		registerTask((s, p, b) ->
		{
			final ItemStack itemStack = p.getInventory().dropSelectedItem(false);
			
			if (!p.world.isClient)
			{
				p.dropItem(itemStack, false, true);
			}
		}, ItemRegistrar.DROP_TASK);
		
		registerTask((s, p, b) ->
		{
			if (!p.isSpectator())
			{
				ItemStack itemStack = p.getStackInHand(Hand.OFF_HAND);
				p.setStackInHand(Hand.OFF_HAND, p.getStackInHand(Hand.MAIN_HAND));
				p.setStackInHand(Hand.MAIN_HAND, itemStack);
				p.clearActiveItem();
			}
		}, ItemRegistrar.SWAP_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.forwardKey, s.getCount() * 50);
		}, ItemRegistrar.MOVE_FORWARD_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.backKey, s.getCount() * 50);
		}, ItemRegistrar.MOVE_BACKWARD_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.leftKey, s.getCount() * 50);
		}, ItemRegistrar.STRAFE_LEFT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.rightKey, s.getCount() * 50);
		}, ItemRegistrar.STRAFE_RIGHT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.sneakKey, s.getCount() * 50);
		}, ItemRegistrar.SNEAK_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.jumpKey, s.getCount() * 50);
		}, ItemRegistrar.JUMP_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryReleaseKey(mc.options.forwardKey);
			KeyBindingUtils.tryReleaseKey(mc.options.backKey);
			KeyBindingUtils.tryReleaseKey(mc.options.leftKey);
			KeyBindingUtils.tryReleaseKey(mc.options.rightKey);
			KeyBindingUtils.tryReleaseKey(mc.options.sneakKey);
			KeyBindingUtils.tryReleaseKey(mc.options.jumpKey);
		}, ItemRegistrar.CANCEL_MOVEMENT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			p.prevPitch = p.getPitch();
			p.setPitch(MathHelper.clamp(Math.round(p.getPitch()) - s.getCount(), -90, 90));
		}, ItemRegistrar.LOOK_UP_TASK);
		
		registerClientTask((s, p, b) ->
		{
			p.prevPitch = p.getPitch();
			p.setPitch(MathHelper.clamp(Math.round(p.getPitch()) + s.getCount(), -90, 90));
		}, ItemRegistrar.LOOK_DOWN_TASK);
		
		registerClientTask((s, p, b) ->
		{
			p.prevYaw = p.getYaw();
			p.prevHeadYaw = p.getHeadYaw();
			final float value = ((Math.round(p.getHeadYaw()) + 180 - s.getCount()) % 360) - 180;
			p.setYaw(value);
			p.setHeadYaw(value);
		}, ItemRegistrar.LOOK_LEFT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			p.prevYaw = p.getYaw();
			p.prevHeadYaw = p.getHeadYaw();
			final float value = ((Math.round(p.getHeadYaw()) + 180 + s.getCount()) % 360) - 180;
			p.setYaw(value);
			p.setHeadYaw(value);
		}, ItemRegistrar.LOOK_RIGHT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			p.prevPitch = p.getPitch();
			p.setPitch(0.0F);
		}, ItemRegistrar.FACE_HORIZON_TASK);
		
		registerClientTask((s, p, b) ->
		{
			p.prevYaw = p.getYaw();
			p.prevHeadYaw = p.getHeadYaw();
			final float value = MathHelper.wrapDegrees(Math.round(p.getHeadYaw() / 90.0F) * 90);
			p.setYaw(value);
			p.setHeadYaw(value);
		}, ItemRegistrar.FACE_CARDINAL_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.attackKey, s.getCount() * 50);
		}, ItemRegistrar.SWING_ITEM_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.useKey, s.getCount() * 50);
		}, ItemRegistrar.USE_ITEM_TASK);
		
		registerClientTask((s, p, b) ->
		{
			p.getInventory().selectedSlot = (s.getCount() - 1) % 9;
		}, ItemRegistrar.SWITCH_SLOT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryReleaseKey(mc.options.forwardKey);
			KeyBindingUtils.tryReleaseKey(mc.options.backKey);
			KeyBindingUtils.tryReleaseKey(mc.options.leftKey);
			KeyBindingUtils.tryReleaseKey(mc.options.rightKey);
			KeyBindingUtils.tryReleaseKey(mc.options.sneakKey);
			KeyBindingUtils.tryReleaseKey(mc.options.jumpKey);
			KeyBindingUtils.tryReleaseKey(mc.options.attackKey);
			KeyBindingUtils.tryReleaseKey(mc.options.useKey);
		}, Discarnate.id("reset_channeler_task"));
		
		registerTask((s, p, b) ->
		{
			if (b instanceof SpiritChannelerBlockEntity)
			{
				((SpiritChannelerBlockEntity) b).deactivate();
			}
		}, ItemRegistrar.END_TASK);
		
		final TaskContainer containedTasks = (s, p, b) ->
		{
			final NbtCompound stackNbt = s.getNbt();
			
			if (stackNbt != null && stackNbt.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE))
			{
				final NbtCompound beNbt = stackNbt.getCompound("BlockEntityTag");
				
				if (beNbt.contains("Items", NbtElement.LIST_TYPE))
				{
					final DefaultedList<ItemStack> stacks = DefaultedList.<ItemStack>ofSize(27, ItemStack.EMPTY);
					Inventories.readNbt(beNbt, stacks);
					
					final List<TaskAction> tasks = new ArrayList<>();
					
					for (final ItemStack stack : stacks)
					{
						if (!stack.isEmpty())
						{
							REGISTRY.getOrEmpty(Registry.ITEM.getId(stack.getItem()))
								.filter(t -> !t.getContainedTasks(stack, p, b).isEmpty())
								.map(t -> (TaskAction) (s1, p1, b1) -> t.accept(stack, p1, b1))
								.ifPresent(tasks::add);
						}
					}
					
					return tasks;
				}
			}
			
			return Collections.emptyList();
		};
		
		final TaskContainer shulkerTasks = (s, p, b) ->
		{
			final NbtCompound stackNbt = s.getNbt();
			
			if (stackNbt != null && stackNbt.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE))
			{
				final NbtCompound beNbt = stackNbt.getCompound("BlockEntityTag");
				
				if (beNbt.contains("Items", NbtElement.LIST_TYPE))
				{
					final DefaultedList<ItemStack> stacks = DefaultedList.<ItemStack>ofSize(27, ItemStack.EMPTY);
					Inventories.readNbt(beNbt, stacks);
					
					if (stacks.stream().anyMatch(i -> i.getItem() == BlockRegistrar.SPIRIT_CHANNELER.asItem()))
					{
						final List<TaskAction> tasks = new ArrayList<>();
						
						for (final ItemStack stack : stacks)
						{
							if (!stack.isEmpty())
							{
								REGISTRY.getOrEmpty(Registry.ITEM.getId(stack.getItem()))
									.filter(t -> !t.getContainedTasks(stack, p, b).isEmpty())
									.map(t -> (TaskAction) (s1, p1, b1) -> t.accept(stack, p1, b1))
									.ifPresent(tasks::add);
							}
						}
						
						return tasks;
					}
				}
			}
			
			return Collections.emptyList();
		};
		
		registerTasks(shulkerTasks, ShulkerBoxBlock.get(null));
		for (DyeColor color : DyeColor.values())
		{
			registerTasks(shulkerTasks, ShulkerBoxBlock.get(color));
		}
		
		registerTasks(containedTasks, BlockRegistrar.SPIRIT_CHANNELER.asItem());
	}
	
	private static Task registerTask(Task task, Identifier id)
	{
		return Registry.register(REGISTRY, id, task);
	}
	
	private static Task registerTask(TaskAction task, Identifier id)
	{
		return registerTask(new Task(task), id);
	}
	
	private static Task registerTask(TaskAction task, ItemConvertible item)
	{
		return registerTask(task, Registry.ITEM.getId(item.asItem()));
	}
	
	private static Task registerTasks(TaskContainer container, Identifier id)
	{
		return registerTask(new Task(container), id);
	}
	
	private static Task registerTasks(TaskContainer container, ItemConvertible item)
	{
		return registerTasks(container, Registry.ITEM.getId(item.asItem()));
	}
	
	private static Task registerClientTask(TaskAction task, Identifier id)
	{
		return registerTask(new ClientTask(task), id);
	}
	
	private static Task registerClientTask(TaskAction task, ItemConvertible item)
	{
		return registerClientTask(task, Registry.ITEM.getId(item.asItem()));
	}
	
	private static Task registerClientTasks(TaskContainer container, Identifier id)
	{
		return registerTask(new ClientTask(container), id);
	}
	
	protected static Task registerClientTasks(TaskContainer container, ItemConvertible item)
	{
		return registerClientTasks(container, Registry.ITEM.getId(item.asItem()));
	}
	
	public static final TaskRegistrar INSTANCE = new TaskRegistrar();
}
