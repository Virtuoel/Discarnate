package virtuoel.discarnate.init;

import java.util.Optional;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.ITask;
import virtuoel.discarnate.api.Task;
import virtuoel.discarnate.reference.DiscarnateConfig;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

@EventBusSubscriber(modid = Discarnate.MOD_ID)
public class TaskRegistrar
{
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
				if(p.onGround)
				{
					p.jump();
					p.velocityChanged = true;
				}
			}, ItemRegistrar.JUMP_TASK),
			
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
									Optional.ofNullable(Task.REGISTRY.getValue(stack.getItem().getRegistryName())).ifPresent(task -> task.accept(stack, p, t));
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
		
		for(EnumDyeColor color : EnumDyeColor.values())
		{
			event.getRegistry().register(createTask(shulkerTask, BlockShulkerBox.getBlockByColor(color)));
		}
	}
	
	@SubscribeEvent
	public static void registerTaskRegistry(RegistryEvent.NewRegistry event)
	{
		new RegistryBuilder<Task>()
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
		return new Task(task).setRegistryName(name.getRegistryName());
	}
}
