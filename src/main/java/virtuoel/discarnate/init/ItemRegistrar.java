package virtuoel.discarnate.init;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.api.DiscarnateAPI;
import virtuoel.discarnate.api.TriConsumer;
import virtuoel.discarnate.tileentity.TileEntitySpiritChanneler;

@EventBusSubscriber(modid = Discarnate.MOD_ID)
@ObjectHolder(Discarnate.MOD_ID)
public class ItemRegistrar
{
	public static final Item BLANK_TASK = Items.AIR;
	public static final Item INFO_TASK = Items.AIR;
	public static final Item WAIT_TASK = Items.AIR;
	public static final Item JUMP_TASK = Items.AIR;
	public static final Item DROP_TASK = Items.AIR;
	public static final Item END_TASK = Items.AIR;
	
	public static void init()
	{
		DiscarnateAPI.instance().addTask(BLANK_TASK, (i, p, t) ->
		{});
		
		DiscarnateAPI.instance().addTask(INFO_TASK, (i, p, t) ->
		{
			p.sendMessage(new TextComponentString("" + i.getCount()));
		});
		
		DiscarnateAPI.instance().addTask(WAIT_TASK, (i, p, t) ->
		{
			try
			{
				Thread.sleep(i.getCount() * 50);
			}
			catch(InterruptedException e)
			{}
		});
		
		DiscarnateAPI.instance().addTask(JUMP_TASK, (i, p, t) ->
		{
			if(p.onGround)
			{
				p.jump();
				p.velocityChanged = true;
			}
		});
		
		DiscarnateAPI.instance().addTask(DROP_TASK, (i, p, t) ->
		{
			ItemStack stack = p.inventory.getCurrentItem();
			
			if(!stack.isEmpty() && stack.getItem().onDroppedByPlayer(stack, p))
			{
				ForgeHooks.onPlayerTossEvent(p, p.inventory.decrStackSize(p.inventory.currentItem, i.getCount()), true);
			}
		});
		
		DiscarnateAPI.instance().addTask(END_TASK, (i, p, t) ->
		{
			if(t instanceof TileEntitySpiritChanneler)
			{
				((TileEntitySpiritChanneler) t).deactivate();
			}
		});
		
		TriConsumer<ItemStack, EntityPlayer, TileEntity> shulkerTask = (i, p, t) ->
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
							if(!stack.isEmpty())
							{
								DiscarnateAPI.instance().getTask(stack).ifPresent(task -> task.accept(stack, p, t));
							}
						}
					}
				}
			}
		};
		
		for(EnumDyeColor color : EnumDyeColor.values())
		{
			DiscarnateAPI.instance().addTask(BlockShulkerBox.getBlockByColor(color), shulkerTask);
		}
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		Stream.of(
			setRegistryNameAndTranslationKey(
				new Item()
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"blank_task"),
			setRegistryNameAndTranslationKey(
				new Item()
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"info_task"),
			setRegistryNameAndTranslationKey(
				new Item()
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"wait_task"),
			setRegistryNameAndTranslationKey(
				new Item()
				.setMaxStackSize(1)
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"jump_task"),
			setRegistryNameAndTranslationKey(
				new Item()
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"drop_task"),
			setRegistryNameAndTranslationKey(
				new Item()
				.setMaxStackSize(1)
				.setCreativeTab(Discarnate.CREATIVE_TAB),
				"end_task"),
		null).filter(Objects::nonNull)
		.forEach(event.getRegistry()::register);
	}
	
	@EventBusSubscriber(modid = Discarnate.MOD_ID, value = Side.CLIENT)
	public static class Client
	{
		@SubscribeEvent
		public static void registerItemModels(ModelRegistryEvent event)
		{
			final Consumer<Item> setItemModel = item ->
			{
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			};
			
			Stream.of(
				BLANK_TASK,
				INFO_TASK,
				WAIT_TASK,
				JUMP_TASK,
				DROP_TASK,
				END_TASK,
			null).filter(i -> i != null && i != Items.AIR)
			.forEach(setItemModel);
		}
	}
	
	public static <T extends Item> T setRegistryNameAndTranslationKey(T entry, String name, String key)
	{
		entry.setRegistryName(name).setTranslationKey(entry.getRegistryName().getNamespace() + "." + key);
		return entry;
	}
	
	public static <T extends Item> T setRegistryNameAndTranslationKey(T entry, String name)
	{
		return setRegistryNameAndTranslationKey(entry, name, name);
	}
}
