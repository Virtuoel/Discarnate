package virtuoel.discarnate.init;

import java.util.function.Supplier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import virtuoel.discarnate.Discarnate;

public class ItemRegistrar
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Discarnate.MOD_ID);
	
	public static final RegistryObject<Item> BLANK_TASK = register("blank_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> INFO_TASK = register("info_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> WAIT_TASK = register("wait_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> DROP_TASK = register("drop_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> SWAP_TASK = register("swap_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP).maxCount(1))
	);
	
	public static final RegistryObject<Item> MOVE_FORWARD_TASK = register("move_forward_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> MOVE_BACKWARD_TASK = register("move_backward_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> STRAFE_LEFT_TASK = register("strafe_left_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> STRAFE_RIGHT_TASK = register("strafe_right_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> CANCEL_MOVEMENT_TASK = register("cancel_movement_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP).maxCount(1))
	);
	
	public static final RegistryObject<Item> LOOK_UP_TASK = register("look_up_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> LOOK_DOWN_TASK = register("look_down_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> LOOK_LEFT_TASK = register("look_left_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> LOOK_RIGHT_TASK = register("look_right_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> FACE_HORIZON_TASK = register("face_horizon_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP).maxCount(1))
		{
			@Override
			public ItemStack getContainerItem(ItemStack itemStack)
			{
				return new ItemStack(LOOK_UP_TASK.get());
			}
			
			@Override
			public boolean hasContainerItem(ItemStack stack)
			{
				return true;
			}
		}
	);
	
	public static final RegistryObject<Item> FACE_CARDINAL_TASK = register("face_cardinal_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP).maxCount(1))
		{
			@Override
			public ItemStack getContainerItem(ItemStack itemStack)
			{
				return new ItemStack(LOOK_LEFT_TASK.get());
			}
			
			@Override
			public boolean hasContainerItem(ItemStack stack)
			{
				return true;
			}
		}
	);
	
	public static final RegistryObject<Item> SNEAK_TASK = register("sneak_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> JUMP_TASK = register("jump_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> SWING_ITEM_TASK = register("swing_item_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> USE_ITEM_TASK = register("use_item_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP))
	);
	
	public static final RegistryObject<Item> SWITCH_SLOT_TASK = register("switch_slot_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP).maxCount(9))
	);
	
	public static final RegistryObject<Item> END_TASK = register("end_task",
		() -> new Item(new Item.Settings().group(Discarnate.ITEM_GROUP).maxCount(1))
	);
	
	private static RegistryObject<Item> register(String name, Supplier<Item> entry)
	{
		return ITEMS.register(name, entry);
	}
	
	public static final ItemRegistrar INSTANCE = new ItemRegistrar();
	
	private ItemRegistrar()
	{
		
	}
}
