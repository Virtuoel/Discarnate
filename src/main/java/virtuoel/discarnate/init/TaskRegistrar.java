package virtuoel.discarnate.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.jarjar.nio.util.Lazy;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.api.TaskAction;
import virtuoel.discarnate.api.TaskContainer;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;
import virtuoel.discarnate.client.option.KeyBindingUtils;
import virtuoel.discarnate.task.ClientTask;
import virtuoel.discarnate.util.DiscarnateMinecraftClientExtensions;
import virtuoel.discarnate.util.I18nUtils;
import virtuoel.discarnate.util.ReflectionUtils;

public class TaskRegistrar
{
	public static final DeferredRegister<Task> TASKS = DeferredRegister.create(Discarnate.id("task"), Discarnate.MOD_ID);
	public static final Lazy<Registry<Task>> REGISTRY = Lazy.of(TASKS.makeRegistry(builder -> {}));
	
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
				Thread.sleep((long) (s.getCount() * ReflectionUtils.getMspt(p::getEntityWorld)));
			}
			catch (InterruptedException e)
			{
				SpiritChannelerBlockEntity.onPlayerStop(p, b);
			}
		}, ItemRegistrar.WAIT_TASK);
		
		registerTask((s, p, b) ->
		{
			final PlayerInventory inv = p.getInventory();
			ItemStack itemStack = inv.getMainHandStack();
			itemStack = itemStack.isEmpty() ? ItemStack.EMPTY : inv.removeStack(inv.selectedSlot, s.getCount());
			
			final World world = p.getEntityWorld();
			
			if (!world.isClient)
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
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.forwardKey, (long) (s.getCount() * ReflectionUtils.getMspt(p::getEntityWorld)));
		}, ItemRegistrar.MOVE_FORWARD_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryToggleKey(mc.options.forwardKey);
		}, ItemRegistrar.TOGGLE_MOVE_FORWARD_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.backKey, (long) (s.getCount() * ReflectionUtils.getMspt(p::getEntityWorld)));
		}, ItemRegistrar.MOVE_BACKWARD_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryToggleKey(mc.options.backKey);
		}, ItemRegistrar.TOGGLE_MOVE_BACKWARD_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.leftKey, (long) (s.getCount() * ReflectionUtils.getMspt(p::getEntityWorld)));
		}, ItemRegistrar.STRAFE_LEFT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryToggleKey(mc.options.leftKey);
		}, ItemRegistrar.TOGGLE_STRAFE_LEFT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.rightKey, (long) (s.getCount() * ReflectionUtils.getMspt(p::getEntityWorld)));
		}, ItemRegistrar.STRAFE_RIGHT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryToggleKey(mc.options.rightKey);
		}, ItemRegistrar.TOGGLE_STRAFE_RIGHT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.sneakKey, (long) (s.getCount() * ReflectionUtils.getMspt(p::getEntityWorld)));
		}, ItemRegistrar.SNEAK_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryToggleKey(mc.options.sneakKey);
		}, ItemRegistrar.TOGGLE_SNEAK_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.jumpKey, (long) (s.getCount() * ReflectionUtils.getMspt(p::getEntityWorld)));
		}, ItemRegistrar.JUMP_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryToggleKey(mc.options.jumpKey);
		}, ItemRegistrar.TOGGLE_JUMP_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
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
			final MinecraftClient mc = MinecraftClient.getInstance();
			((DiscarnateMinecraftClientExtensions) mc).discarnate_doAttack();
			KeyBindingUtils.tryHoldKey(mc.options.attackKey, (long) (s.getCount() * ReflectionUtils.getMspt(p::getEntityWorld)));
		}, ItemRegistrar.SWING_ITEM_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			((DiscarnateMinecraftClientExtensions) mc).discarnate_doAttack();
			KeyBindingUtils.tryToggleKey(mc.options.attackKey);
		}, ItemRegistrar.TOGGLE_SWING_ITEM_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.useKey, (long) (s.getCount() * ReflectionUtils.getMspt(p::getEntityWorld)));
		}, ItemRegistrar.USE_ITEM_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryToggleKey(mc.options.useKey);
		}, ItemRegistrar.TOGGLE_USE_ITEM_TASK);
		
		registerClientTask((s, p, b) ->
		{
			p.getInventory().selectedSlot = (s.getCount() - 1) % 9;
		}, ItemRegistrar.SWITCH_SLOT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			final MinecraftClient mc = MinecraftClient.getInstance();
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
				Optional.ofNullable(b.getWorld()).map(World::getServer).ifPresent(server ->
				{
					server.execute(((SpiritChannelerBlockEntity) b)::deactivate);
				});
			}
		}, ItemRegistrar.END_TASK);
	}
	
	@SubscribeEvent
	public static void registerVanillaTasks(RegisterEvent event)
	{
		if (!event.getRegistryKey().equals(TASKS.getRegistryKey()))
		{
			return;
		}
		
		final TaskContainer channelerTasks = (s, p, b) ->
		{
			final NbtCompound stackNbt = s.getNbt();
			
			if (stackNbt != null && stackNbt.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE))
			{
				final NbtCompound beNbt = stackNbt.getCompound("BlockEntityTag");
				
				if (beNbt.contains("Items", NbtElement.LIST_TYPE))
				{
					final DefaultedList<ItemStack> stacks = DefaultedList.<ItemStack>ofSize(25, ItemStack.EMPTY);
					Inventories.readNbt(beNbt, stacks);
					
					final List<TaskAction> tasks = new ArrayList<>();
					
					for (final ItemStack stack : stacks)
					{
						if (!stack.isEmpty())
						{
							Optional.ofNullable(REGISTRY.get().get(Registries.ITEM.getId(stack.getItem())))
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
					
					final List<TaskAction> tasks = new ArrayList<>();
					
					for (final ItemStack stack : stacks)
					{
						if (!stack.isEmpty())
						{
							Optional.ofNullable(REGISTRY.get().get(Registries.ITEM.getId(stack.getItem())))
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
		
		final TaskContainer bundleTasks = (s, p, b) ->
		{
			final NbtCompound stackNbt = s.getNbt();
			
			if (stackNbt != null && stackNbt.contains("Items", NbtElement.LIST_TYPE))
			{
				final List<ItemStack> stacks = stackNbt.getList("Items", NbtElement.COMPOUND_TYPE)
					.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt).collect(Collectors.toList());
				
				final List<TaskAction> tasks = new ArrayList<>();
				
				for (final ItemStack stack : stacks)
				{
					if (!stack.isEmpty())
					{
						Optional.ofNullable(REGISTRY.get().get(Registries.ITEM.getId(stack.getItem())))
							.filter(t -> !t.getContainedTasks(stack, p, b).isEmpty())
							.map(t -> (TaskAction) (s1, p1, b1) -> t.accept(stack, p1, b1))
							.ifPresent(tasks::add);
					}
				}
				
				return tasks;
			}
			
			return Collections.emptyList();
		};
		
		ModContainer container = ModLoadingContext.get().getActiveContainer();
		ModLoadingContext.get().setActiveContainer(ModList.get().getModContainerById("minecraft").orElseThrow());
		
		event.register(TASKS.getRegistryKey(), Registries.BLOCK.getId(ShulkerBoxBlock.get(null)), () -> new Task(shulkerTasks));
		for (DyeColor color : DyeColor.values())
		{
			event.register(TASKS.getRegistryKey(), Registries.BLOCK.getId(ShulkerBoxBlock.get(color)), () -> new Task(shulkerTasks));
		}
		
		event.register(TASKS.getRegistryKey(), Registries.ITEM.getId(Items.BUNDLE), () -> new Task(bundleTasks));
		
		ModLoadingContext.get().setActiveContainer(container);
		
		event.register(TASKS.getRegistryKey(), BlockRegistrar.SPIRIT_CHANNELER.getId(), () -> new Task(channelerTasks));
	}
	
	private static DeferredHolder<Task, Task> registerTask(Supplier<Task> task, Identifier id)
	{
		return TASKS.register(id.getPath(), task);
	}
	
	private static DeferredHolder<Task, Task> registerTask(TaskAction task, Identifier id)
	{
		return registerTask(() -> new Task(task), id);
	}
	
	private static DeferredHolder<Task, Task> registerTask(TaskAction task, DeferredHolder<?, ?> entry)
	{
		return registerTask(task, entry.getId());
	}
	
	private static DeferredHolder<Task, Task> registerTasks(TaskContainer container, Identifier id)
	{
		return registerTask(() -> new Task(container), id);
	}
	
	protected static DeferredHolder<Task, Task> registerTasks(TaskContainer container, ItemConvertible item)
	{
		return registerTasks(container, Registries.ITEM.getId(item.asItem()));
	}
	
	private static DeferredHolder<Task, Task> registerClientTask(TaskAction task, Identifier id)
	{
		return registerTask(() -> new ClientTask(task), id);
	}
	
	private static DeferredHolder<Task, Task> registerClientTask(TaskAction task, DeferredHolder<?, ?> entry)
	{
		return registerClientTask(task, entry.getId());
	}
	
	private static DeferredHolder<Task, Task> registerClientTasks(TaskContainer container, Identifier id)
	{
		return registerTask(() -> new ClientTask(container), id);
	}
	
	protected static DeferredHolder<Task, Task> registerClientTasks(TaskContainer container, ItemConvertible item)
	{
		return registerClientTasks(container, Registries.ITEM.getId(item.asItem()));
	}
	
	public static final TaskRegistrar INSTANCE = new TaskRegistrar();
}
