package virtuoel.discarnate.init;

import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.api.TaskAction;
import virtuoel.discarnate.block.entity.SpiritChannelerBlockEntity;
import virtuoel.discarnate.client.option.KeyBindingUtils;
import virtuoel.discarnate.task.ClientTask;
import virtuoel.discarnate.util.I18nUtils;

public class TaskRegistrar
{
	public static final DeferredRegister<Task> TASKS = DeferredRegister.create(Task.class, Discarnate.MOD_ID);
	public static final Lazy<IForgeRegistry<Task>> REGISTRY = Lazy.of(TASKS.makeRegistry("task", () ->
	{
		return new RegistryBuilder<Task>().setDefaultKey(ItemRegistrar.BLANK_TASK.getId());
	}));
	
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
			KeyBindingUtils.tryHoldKey(mc.options.keyForward, s.getCount() * 50);
		}, ItemRegistrar.MOVE_FORWARD_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.keyBack, s.getCount() * 50);
		}, ItemRegistrar.MOVE_BACKWARD_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.keyLeft, s.getCount() * 50);
		}, ItemRegistrar.STRAFE_LEFT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.keyRight, s.getCount() * 50);
		}, ItemRegistrar.STRAFE_RIGHT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.keySneak, s.getCount() * 50);
		}, ItemRegistrar.SNEAK_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.keyJump, s.getCount() * 50);
		}, ItemRegistrar.JUMP_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryReleaseKey(mc.options.keyForward);
			KeyBindingUtils.tryReleaseKey(mc.options.keyBack);
			KeyBindingUtils.tryReleaseKey(mc.options.keyLeft);
			KeyBindingUtils.tryReleaseKey(mc.options.keyRight);
			KeyBindingUtils.tryReleaseKey(mc.options.keySneak);
			KeyBindingUtils.tryReleaseKey(mc.options.keyJump);
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
			KeyBindingUtils.tryHoldKey(mc.options.keyAttack, s.getCount() * 50);
		}, ItemRegistrar.SWING_ITEM_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryHoldKey(mc.options.keyUse, s.getCount() * 50);
		}, ItemRegistrar.USE_ITEM_TASK);
		
		registerClientTask((s, p, b) ->
		{
			p.getInventory().selectedSlot = (s.getCount() - 1) % 9;
		}, ItemRegistrar.SWITCH_SLOT_TASK);
		
		registerClientTask((s, p, b) ->
		{
			MinecraftClient mc = MinecraftClient.getInstance();
			KeyBindingUtils.tryReleaseKey(mc.options.keyForward);
			KeyBindingUtils.tryReleaseKey(mc.options.keyBack);
			KeyBindingUtils.tryReleaseKey(mc.options.keyLeft);
			KeyBindingUtils.tryReleaseKey(mc.options.keyRight);
			KeyBindingUtils.tryReleaseKey(mc.options.keySneak);
			KeyBindingUtils.tryReleaseKey(mc.options.keyJump);
			KeyBindingUtils.tryReleaseKey(mc.options.keyAttack);
			KeyBindingUtils.tryReleaseKey(mc.options.keyUse);
		}, Discarnate.id("reset_channeler_task"));
		
		registerTask((s, p, b) ->
		{
			if (b instanceof SpiritChannelerBlockEntity)
			{
				((SpiritChannelerBlockEntity) b).deactivate();
			}
		}, ItemRegistrar.END_TASK);
	}
	
	@SubscribeEvent
	public static void registerVanillaTasks(RegistryEvent.Register<Task> event)
	{
		final TaskAction shulkerTask = (s, p, b) ->
		{
			NbtCompound stackNbt = s.getNbt();
			
			if (stackNbt != null && stackNbt.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE))
			{
				NbtCompound beNbt = stackNbt.getCompound("BlockEntityTag");
				
				if (beNbt.contains("Items", NbtElement.LIST_TYPE))
				{
					DefaultedList<ItemStack> stacks = DefaultedList.<ItemStack>ofSize(27, ItemStack.EMPTY);
					Inventories.readNbt(beNbt, stacks);
					
					if (stacks.stream().anyMatch(i -> i.getItem() == BlockRegistrar.SPIRIT_CHANNELER.get().asItem()))
					{
						for (ItemStack stack : stacks)
						{
							if (SpiritChannelerBlockEntity.canPlayerContinue(p) && SpiritChannelerBlockEntity.isActive(b.getWorld(), b.getPos()))
							{
								if (!stack.isEmpty())
								{
									Optional.ofNullable(REGISTRY.get().getValue(stack.getItem().getRegistryName())).ifPresent(task -> task.accept(stack, p, b));
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
		
		ModContainer container = ModLoadingContext.get().getActiveContainer();
		ModLoadingContext.get().setActiveContainer(ModList.get().getModContainerById("minecraft").orElseThrow());
		
		event.getRegistry().register(new Task(shulkerTask).setRegistryName(ShulkerBoxBlock.get(null).getRegistryName()));
		for (DyeColor color : DyeColor.values())
		{
			event.getRegistry().register(new Task(shulkerTask).setRegistryName(ShulkerBoxBlock.get(color).getRegistryName()));
		}
		
		ModLoadingContext.get().setActiveContainer(container);
	}
	
	private static RegistryObject<Task> registerTask(Supplier<Task> task, Identifier id)
	{
		return TASKS.register(id.getPath(), task);
	}
	
	private static RegistryObject<Task> registerTask(TaskAction task, Identifier id)
	{
		return registerTask(() -> new Task(task), id);
	}
	
	private static RegistryObject<Task> registerTask(TaskAction task, RegistryObject<?> entry)
	{
		return registerTask(task, entry.getId());
	}
	
	private static RegistryObject<Task> registerClientTask(TaskAction task, Identifier id)
	{
		return registerTask(() -> new ClientTask(task), id);
	}
	
	private static RegistryObject<Task> registerClientTask(TaskAction task, RegistryObject<?> entry)
	{
		return registerClientTask(task, entry.getId());
	}
	
	public static final TaskRegistrar INSTANCE = new TaskRegistrar();
}
