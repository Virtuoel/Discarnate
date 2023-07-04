package virtuoel.discarnate.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.RegistryObject;

public final class ReflectionUtils
{
	public static MutableText formatted(MutableText input, Formatting... formatting)
	{
		return input.formatted(formatting);
	}
	
	public static void setItemSettingsGroup(Item.Settings settings, ItemGroup group)
	{
		settings.group(group);
	}
	
	public static ItemGroup buildItemGroup(Identifier id, Supplier<ItemStack> icon, Supplier<Stream<RegistryObject<? extends ItemConvertible>>> items)
	{
		return new ItemGroup(id.getNamespace() + "." + id.getPath())
		{
			@Override
			public ItemStack createIcon()
			{
				return icon.get();
			}
		};
	}
	
	public static <V> V get(Registry<V> registry, Identifier id)
	{
		return registry.get(id);
	}
	
	public static <V> Identifier getId(Registry<V> registry, V entry)
	{
		return registry.getId(entry);
	}
	
	public static <V> Optional<V> getOrEmpty(Registry<V> registry, Identifier id)
	{
		return registry.getOrEmpty(id);
	}
	
	public static final class Client
	{
		public static ButtonWidget buildButtonWidget(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress)
		{
			return new ButtonWidget(x, y, width, height, message, onPress);
		}
		
		public static void renderClickableWidgetTooltip(ClickableWidget widget, MatrixStack matrices, int x, int y)
		{
			if (widget.isHovered() && !widget.isFocused())
			{
				widget.renderTooltip(matrices, x, y);
			}
		}
		
		private Client()
		{
			
		}
	}
	
	public static Optional<Field> getField(final Optional<Class<?>> classObj, final String fieldName)
	{
		return classObj.map(c ->
		{
			try
			{
				final Field f = c.getDeclaredField(fieldName);
				f.setAccessible(true);
				return f;
			}
			catch (SecurityException | NoSuchFieldException e)
			{
				
			}
			return null;
		});
	}
	
	public static void setField(final Optional<Class<?>> classObj, final String fieldName, Object object, Object value)
	{
		ReflectionUtils.getField(classObj, fieldName).ifPresent(f ->
		{
			try
			{
				f.set(object, value);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				
			}
		});
	}
	
	public static Optional<Method> getMethod(final Optional<Class<?>> classObj, final String methodName, Class<?>... args)
	{
		return classObj.map(c ->
		{
			try
			{
				final Method m = c.getMethod(methodName, args);
				m.setAccessible(true);
				return m;
			}
			catch (SecurityException | NoSuchMethodException e)
			{
				
			}
			return null;
		});
	}
	
	public static <T> Optional<Constructor<T>> getConstructor(final Optional<Class<T>> clazz, final Class<?>... params)
	{
		return clazz.map(c ->
		{
			try
			{
				return c.getConstructor(params);
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				return null;
			}
		});
	}
	
	public static Optional<Class<?>> getClass(final String className, final String... classNames)
	{
		Optional<Class<?>> ret = getClass(className);
		
		for (final String name : classNames)
		{
			if (ret.isPresent())
			{
				return ret;
			}
			
			ret = getClass(name);
		}
		
		return ret;
	}
	
	public static Optional<Class<?>> getClass(final String className)
	{
		try
		{
			return Optional.of(Class.forName(className));
		}
		catch (ClassNotFoundException e)
		{
			
		}
		
		return Optional.empty();
	}
	
	private ReflectionUtils()
	{
		
	}
}
