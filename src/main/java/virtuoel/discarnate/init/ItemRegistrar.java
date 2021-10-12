package virtuoel.discarnate.init;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import virtuoel.discarnate.Discarnate;

public class ItemRegistrar
{
	public static final Item BLANK_TASK = register("blank_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item INFO_TASK = register("info_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item WAIT_TASK = register("wait_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item DROP_TASK = register("drop_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item SWAP_TASK = register("swap_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP).maxCount(1))
	);
	
	public static final Item MOVE_FORWARD_TASK = register("move_forward_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item MOVE_BACKWARD_TASK = register("move_backward_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item STRAFE_LEFT_TASK = register("strafe_left_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item STRAFE_RIGHT_TASK = register("strafe_right_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item CANCEL_MOVEMENT_TASK = register("cancel_movement_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP).maxCount(1))
	);
	
	public static final Item LOOK_UP_TASK = register("look_up_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item LOOK_DOWN_TASK = register("look_down_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item LOOK_LEFT_TASK = register("look_left_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item LOOK_RIGHT_TASK = register("look_right_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item FACE_HORIZON_TASK = register("face_horizon_task",
		new Item(new Item.Settings().recipeRemainder(LOOK_UP_TASK).group(Discarnate.ITEM_GROUP).maxCount(1))
	);
	
	public static final Item FACE_CARDINAL_TASK = register("face_cardinal_task",
		new Item(new Item.Settings().recipeRemainder(LOOK_LEFT_TASK).group(Discarnate.ITEM_GROUP).maxCount(1))
	);
	
	public static final Item SNEAK_TASK = register("sneak_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item JUMP_TASK = register("jump_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item SWING_ITEM_TASK = register("swing_item_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
		{
			@Override
			@Environment(EnvType.CLIENT)
			public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
			{
				tooltip.add(new LiteralText("WIP"));
			};
		}
	);
	
	public static final Item USE_ITEM_TASK = register("use_item_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final Item SWITCH_SLOT_TASK = register("switch_slot_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP).maxCount(9))
	);
	
	public static final Item END_TASK = register("end_task",
		new Item(new Item.Settings().group(Discarnate.ITEM_GROUP).maxCount(1))
	);
	
	private static Item register(String name, Item entry)
	{
		return Registry.register(Registry.ITEM, Discarnate.id(name), entry);
	}
	
	public static final ItemRegistrar INSTANCE = new ItemRegistrar();
	
	private ItemRegistrar()
	{
		
	}
}
