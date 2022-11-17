package virtuoel.discarnate.init;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.util.I18nUtils;
import virtuoel.discarnate.util.ReflectionUtils;

public class ItemRegistrar
{
	public static final Item BLANK_TASK = register("blank_task",
		new Item(commonSettings())
	);
	
	public static final Item INFO_TASK = register("info_task",
		new Item(commonSettings())
	);
	
	public static final Item WAIT_TASK = register("wait_task",
		withDelayTooltip(commonSettings())
	);
	
	public static final Item DROP_TASK = register("drop_task",
		withItemCountTooltip(commonSettings())
	);
	
	public static final Item SWAP_TASK = register("swap_task",
		new Item(commonSettings())
	);
	
	public static final Item MOVE_FORWARD_TASK = register("move_forward_task",
		withDurationTooltip(commonSettings())
	);
	
	public static final Item TOGGLE_MOVE_FORWARD_TASK = register("toggle_move_forward_task",
		withToggleTooltip(commonSettings())
	);
	
	public static final Item MOVE_BACKWARD_TASK = register("move_backward_task",
		withDurationTooltip(commonSettings())
	);
	
	public static final Item TOGGLE_MOVE_BACKWARD_TASK = register("toggle_move_backward_task",
		withToggleTooltip(commonSettings())
	);
	
	public static final Item STRAFE_LEFT_TASK = register("strafe_left_task",
		withDurationTooltip(commonSettings())
	);
	
	public static final Item TOGGLE_STRAFE_LEFT_TASK = register("toggle_strafe_left_task",
		withToggleTooltip(commonSettings())
	);
	
	public static final Item STRAFE_RIGHT_TASK = register("strafe_right_task",
		withDurationTooltip(commonSettings())
	);
	
	public static final Item TOGGLE_STRAFE_RIGHT_TASK = register("toggle_strafe_right_task",
		withToggleTooltip(commonSettings())
	);
	
	public static final Item CANCEL_MOVEMENT_TASK = register("cancel_movement_task",
		new Item(commonSettings())
	);
	
	public static final Item LOOK_UP_TASK = register("look_up_task",
		withAngleTooltip(commonSettings())
	);
	
	public static final Item LOOK_DOWN_TASK = register("look_down_task",
		withAngleTooltip(commonSettings())
	);
	
	public static final Item LOOK_LEFT_TASK = register("look_left_task",
		withAngleTooltip(commonSettings())
	);
	
	public static final Item LOOK_RIGHT_TASK = register("look_right_task",
		withAngleTooltip(commonSettings())
	);
	
	public static final Item FACE_HORIZON_TASK = register("face_horizon_task",
		new Item(commonSettings())
	);
	
	public static final Item FACE_CARDINAL_TASK = register("face_cardinal_task",
		new Item(commonSettings())
	);
	
	public static final Item SNEAK_TASK = register("sneak_task",
		withDurationTooltip(commonSettings())
	);
	
	public static final Item TOGGLE_SNEAK_TASK = register("toggle_sneak_task",
		withToggleTooltip(commonSettings())
	);
	
	public static final Item JUMP_TASK = register("jump_task",
		withDurationTooltip(commonSettings())
	);
	
	public static final Item TOGGLE_JUMP_TASK = register("toggle_jump_task",
		withToggleTooltip(commonSettings())
	);
	
	public static final Item SWING_ITEM_TASK = register("swing_item_task",
		withDurationTooltip(commonSettings())
	);
	
	public static final Item TOGGLE_SWING_ITEM_TASK = register("toggle_swing_item_task",
		withToggleTooltip(commonSettings())
	);
	
	public static final Item USE_ITEM_TASK = register("use_item_task",
		withDurationTooltip(commonSettings())
	);
	
	public static final Item TOGGLE_USE_ITEM_TASK = register("toggle_use_item_task",
		withToggleTooltip(commonSettings())
	);
	
	public static final Item SWITCH_SLOT_TASK = register("switch_slot_task",
		withSlotTooltip(commonSettings())
	);
	
	public static final Item END_TASK = register("end_task",
		new Item(commonSettings())
	);
	
	private static Item.Settings commonSettings()
	{
		return Discarnate.commonItemSettings();
	}
	
	private static Item withAngleTooltip(final Item.Settings settings)
	{
		return withCountTooltip(settings, 64, "item.discarnate.task.angle", "Angle: %s degree(s)", I18nUtils.translate("item.discarnate.task.delay", "Delay: %s ticks", 0));
	}
	
	private static Item withDelayTooltip(final Item.Settings settings)
	{
		return withCountTooltip(settings, 64, "item.discarnate.task.delay", "Delay: %s tick(s)");
	}
	
	private static Item withDurationTooltip(final Item.Settings settings)
	{
		return withCountTooltip(settings, 64, "item.discarnate.task.duration", "Duration: %s tick(s)", I18nUtils.translate("item.discarnate.task.delay", "Delay: %s ticks", 0));
	}
	
	private static Item withItemCountTooltip(final Item.Settings settings)
	{
		return withCountTooltip(settings, 64, "item.discarnate.task.items", "Amount: %s item(s)");
	}
	
	private static Item withSlotTooltip(final Item.Settings settings)
	{
		return withCountTooltip(settings, 9, "item.discarnate.task.slot", "Slot: %s");
	}
	
	private static Item withToggleTooltip(final Item.Settings settings)
	{
		return withTooltip(settings, I18nUtils.translate("item.discarnate.task.toggle", "Toggle"));
	}
	
	private static Item withCountTooltip(final Item.Settings settings, final int limit, final String key, final String localized, final Text... tooltips)
	{
		return new Item(settings)
		{
			@Override
			public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
			{
				super.appendTooltip(stack, world, tooltip, context);
				tooltip.add(ReflectionUtils.formatted(I18nUtils.translate(key, localized, "" + (((stack.getCount() - 1) % limit) + 1)), Formatting.GRAY));
				for (final Text text : tooltips)
				{
					tooltip.add(ReflectionUtils.formatted(text, Formatting.GRAY));
				}
			}
		};
	}
	
	private static Item withTooltip(final Item.Settings settings, final Text... tooltips)
	{
		return new Item(settings)
		{
			@Override
			public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
			{
				super.appendTooltip(stack, world, tooltip, context);
				for (final Text text : tooltips)
				{
					tooltip.add(ReflectionUtils.formatted(text, Formatting.GRAY));
				}
			}
		};
	}
	
	private static Item register(String name, Item entry)
	{
		return Registry.register(Registry.ITEM, Discarnate.id(name), entry);
	}
	
	public static final ItemRegistrar INSTANCE = new ItemRegistrar();
	
	private ItemRegistrar()
	{
		
	}
}
