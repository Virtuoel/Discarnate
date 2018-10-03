package virtuoel.discarnate.api;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public interface ITaskManager
{
	public default boolean addTask(@Nonnull ResourceLocation name, @Nonnull TriConsumer<ItemStack, EntityPlayer, TileEntity> task)
	{
		return false;
	}
	
	public default boolean addTask(@Nonnull String name, @Nonnull TriConsumer<ItemStack, EntityPlayer, TileEntity> task)
	{
		return addTask(new ResourceLocation(name), task);
	}
	
	public default boolean addTask(@Nonnull Item item, @Nonnull TriConsumer<ItemStack, EntityPlayer, TileEntity> task)
	{
		return addTask(item.getRegistryName(), task);
	}
	
	public default boolean addTask(@Nonnull Block block, @Nonnull TriConsumer<ItemStack, EntityPlayer, TileEntity> task)
	{
		return addTask(block.getRegistryName(), task);
	}
	
	public default boolean addTask(@Nonnull ItemStack stack, @Nonnull TriConsumer<ItemStack, EntityPlayer, TileEntity> task)
	{
		return addTask(stack.getItem(), task);
	}
	
	public default Optional<TriConsumer<ItemStack, EntityPlayer, TileEntity>> removeTask(@Nonnull ResourceLocation name)
	{
		return Optional.empty();
	}
	
	public default Optional<TriConsumer<ItemStack, EntityPlayer, TileEntity>> removeTask(@Nonnull String name)
	{
		return removeTask(new ResourceLocation(name));
	}
	
	public default Optional<TriConsumer<ItemStack, EntityPlayer, TileEntity>> getTask(@Nonnull ResourceLocation name)
	{
		return Optional.empty();
	}
	
	public default Optional<TriConsumer<ItemStack, EntityPlayer, TileEntity>> getTask(@Nonnull String name)
	{
		return getTask(new ResourceLocation(name));
	}
	
	public default Optional<TriConsumer<ItemStack, EntityPlayer, TileEntity>> getTask(@Nonnull Item item)
	{
		return getTask(item.getRegistryName());
	}
	
	public default Optional<TriConsumer<ItemStack, EntityPlayer, TileEntity>> getTask(@Nonnull Block block)
	{
		return getTask(block.getRegistryName());
	}
	
	public default Optional<TriConsumer<ItemStack, EntityPlayer, TileEntity>> getTask(@Nonnull ItemStack stack)
	{
		return getTask(stack.getItem());
	}
}
