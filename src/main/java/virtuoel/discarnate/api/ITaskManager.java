package virtuoel.discarnate.api;

import java.util.Optional;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface ITaskManager
{
	public default boolean addTask(ResourceLocation name, @Nonnull BiConsumer<ItemStack, EntityPlayer> task)
	{
		return false;
	}
	
	public default boolean addTask(String name, @Nonnull BiConsumer<ItemStack, EntityPlayer> task)
	{
		return addTask(new ResourceLocation(name), task);
	}
	
	public default boolean addTask(Item item, @Nonnull BiConsumer<ItemStack, EntityPlayer> task)
	{
		return addTask(item.getRegistryName(), task);
	}
	
	public default boolean addTask(ItemStack stack, @Nonnull BiConsumer<ItemStack, EntityPlayer> task)
	{
		return addTask(stack.getItem(), task);
	}
	
	public default Optional<BiConsumer<ItemStack, EntityPlayer>> removeTask(ResourceLocation name)
	{
		return Optional.empty();
	}
	
	public default Optional<BiConsumer<ItemStack, EntityPlayer>> removeTask(String name)
	{
		return removeTask(new ResourceLocation(name));
	}
	
	public default Optional<BiConsumer<ItemStack, EntityPlayer>> getTask(ResourceLocation name)
	{
		return Optional.empty();
	}
	
	public default Optional<BiConsumer<ItemStack, EntityPlayer>> getTask(String name)
	{
		return getTask(new ResourceLocation(name));
	}
	
	public default Optional<BiConsumer<ItemStack, EntityPlayer>> getTask(Item item)
	{
		return getTask(item.getRegistryName());
	}
	
	public default Optional<BiConsumer<ItemStack, EntityPlayer>> getTask(ItemStack stack)
	{
		return getTask(stack.getItem());
	}
}
