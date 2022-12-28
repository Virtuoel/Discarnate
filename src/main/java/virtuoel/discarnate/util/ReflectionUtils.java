package virtuoel.discarnate.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import virtuoel.discarnate.Discarnate;

public final class ReflectionUtils
{
	public static final MethodHandle FORMATTED, GROUP, BUTTON_WIDGET, RENDER_TOOLTIP, BUILD, REGISTER, GET, GET_ID, GET_OR_EMPTY;
	public static final Registry<Block> BLOCK_REGISTRY;
	public static final Registry<Item> ITEM_REGISTRY;
	public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTRY;
	
	static
	{
		final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
		final Int2ObjectMap<MethodHandle> h = new Int2ObjectArrayMap<MethodHandle>();
		Object rB, rI, rBe = rI = rB = null;
		
		final Lookup lookup = MethodHandles.lookup();
		String mapped = "unset";
		Method m;
		Class<?> clazz;
		Field f;
		Constructor<?> c;
		
		try
		{
			final boolean is115Minus = VersionUtils.MINOR <= 15;
			final boolean is1192Minus = VersionUtils.MINOR < 19 || (VersionUtils.MINOR == 19 && VersionUtils.PATCH <= 2);
			
			if (is115Minus)
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2561", "method_10856", "([Lnet/minecraft/class_124;)Lnet/minecraft/class_2561;");
				m = Text.class.getMethod(mapped, Formatting[].class);
			}
			else
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_5250", "method_27695", "([Lnet/minecraft/class_124;)Lnet/minecraft/class_5250;");
				m = MutableText.class.getMethod(mapped, Formatting[].class);
			}
			h.put(0, lookup.unreflect(m));
			
			final String registrar = "net.minecraft.class_" + (is1192Minus ? "2378" : "7923");
			
			mapped = mappingResolver.mapClassName("intermediary", registrar);
			clazz = Class.forName(mapped);
			
			mapped = mappingResolver.mapFieldName("intermediary", registrar, "field_" + (is1192Minus ? "11146" : "41175"), "Lnet/minecraft/class_" + (is1192Minus ? "2348;" : "7922;"));
			f = clazz.getField(mapped);
			rB = f.get(null);
			
			mapped = mappingResolver.mapFieldName("intermediary", registrar, "field_" + (is1192Minus ? "11142" : "41178"), "Lnet/minecraft/class_" + (is1192Minus ? "2348;" : "7922;"));
			f = clazz.getField(mapped);
			rI = f.get(null);
			
			mapped = mappingResolver.mapFieldName("intermediary", registrar, "field_" + (is1192Minus ? "11137" : "41181"), "Lnet/minecraft/class_2378;");
			f = clazz.getField(mapped);
			rBe = f.get(null);
			
			if (is1192Minus)
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_1792$class_1793", "method_7892", "(Lnet/minecraft/class_1761;)Lnet/minecraft/class_1792$class_1793;");
				m = Item.Settings.class.getMethod(mapped, ItemGroup.class);
				h.put(1, lookup.unreflect(m));
				
				if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
				{
					mapped = "<init>";
					c = ButtonWidget.class.getConstructor(int.class, int.class, int.class, int.class, Text.class, ButtonWidget.PressAction.class);
					h.put(2, lookup.unreflectConstructor(c));
					
					mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_339", "method_25352", "(Lnet/minecraft/class_4587;II)V");
					m = ClickableWidget.class.getMethod(mapped, MatrixStack.class, int.class, int.class);
					h.put(3, lookup.unreflect(m));
				}
			}
			
			if (ModLoaderUtils.isModLoaded("fabric-item-groups-v0"))
			{
				mapped = "net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder";
				clazz = Class.forName(mapped);
				
				mapped = "build";
				m = clazz.getMethod(mapped, Identifier.class, Supplier.class);
				h.put(4, lookup.unreflect(m));
			}
			
			mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2378", "method_10230", "(Lnet/minecraft/class_2378;Lnet/minecraft/class_2960;Ljava/lang/Object;)Ljava/lang/Object;");
			m = Registry.class.getMethod(mapped, Registry.class, Identifier.class, Object.class);
			h.put(5, lookup.unreflect(m));
			
			mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2378", "method_10223", "(Lnet/minecraft/class_2960;)Ljava/lang/Object;");
			m = Registry.class.getMethod(mapped, Identifier.class);
			h.put(6, lookup.unreflect(m));
			
			mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2378", "method_10221", "(Ljava/lang/Object;)Lnet/minecraft/class_2960;");
			m = Registry.class.getMethod(mapped, Object.class);
			h.put(7, lookup.unreflect(m));
			
			mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2378", "method_17966", "(Lnet/minecraft/class_2960;)Ljava/util/Optional;");
			m = Registry.class.getMethod(mapped, Identifier.class);
			h.put(8, lookup.unreflect(m));
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e)
		{
			Discarnate.LOGGER.error("Current name lookup: {}", mapped);
			Discarnate.LOGGER.catching(e);
		}
		
		FORMATTED = h.get(0);
		GROUP = h.get(1);
		BUTTON_WIDGET = h.get(2);
		RENDER_TOOLTIP = h.get(3);
		BUILD = h.get(4);
		REGISTER = h.get(5);
		GET = h.get(6);
		GET_ID = h.get(7);
		GET_OR_EMPTY = h.get(8);
		BLOCK_REGISTRY = castRegistry(rB);
		ITEM_REGISTRY = castRegistry(rI);
		BLOCK_ENTITY_TYPE_REGISTRY = castRegistry(rBe);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> Registry<T> castRegistry(Object obj)
	{
		return (Registry<T>) obj;
	}
	
	public static Text formatted(Object input, Formatting... formatting)
	{
		if (FORMATTED != null)
		{
			try
			{
				if (VersionUtils.MINOR <= 15)
				{
					return (Text) FORMATTED.invokeExact((Text) input, formatting);
				}
				else
				{
					return (MutableText) FORMATTED.invokeExact((MutableText) input, formatting);
				}
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return (Text) input;
	}
	
	public static void setItemSettingsGroup(Item.Settings settings, ItemGroup group)
	{
		if (GROUP != null)
		{
			try
			{
				GROUP.invoke(settings, group);
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
	public static ItemGroup buildItemGroup(Identifier id, Supplier<ItemStack> icon, Supplier<Stream<ItemConvertible>> items)
	{
		if (BUILD != null)
		{
			try
			{
				return (ItemGroup) BUILD.invoke(id, icon);
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return FabricItemGroup.builder(id)
			.icon(icon)
			.entries((enabledFeatures, entries, operatorEnabled) ->
			{
				items.get().forEach(entries::add);
			})
			.build();
	}
	
	public static <V, T extends V> T register(Registry<V> registry, Identifier id, T entry)
	{
		try
		{
			return (T) REGISTER.invoke(registry, id, entry);
		}
		catch (Throwable e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static <V> V get(Registry<V> registry, Identifier id)
	{
		try
		{
			return (V) GET.invoke(registry, id);
		}
		catch (Throwable e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static <V> Identifier getId(Registry<V> registry, V entry)
	{
		try
		{
			return (Identifier) GET_ID.invoke(registry, entry);
		}
		catch (Throwable e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static <V> Optional<V> getOrEmpty(Registry<V> registry, Identifier id)
	{
		try
		{
			return (Optional<V>) GET_OR_EMPTY.invoke(registry, id);
		}
		catch (Throwable e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static final class Client
	{
		public static ButtonWidget buildButtonWidget(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress)
		{
			if (BUTTON_WIDGET != null)
			{
				try
				{
					return (ButtonWidget) BUTTON_WIDGET.invoke(x, y, width, height, message, onPress);
				}
				catch (Throwable e)
				{
					throw new RuntimeException(e);
				}
			}
			
			return ButtonWidget.builder(message, onPress)
				.dimensions(x, y, width, height)
				.build();
		}
		
		public static void renderClickableWidgetTooltip(ClickableWidget widget, MatrixStack matrices, int x, int y)
		{
			if (RENDER_TOOLTIP != null)
			{
				if (widget.isHovered() && !widget.isFocused())
				{
					try
					{
						RENDER_TOOLTIP.invoke(widget, matrices, x, y);
					}
					catch (Throwable e)
					{
						throw new RuntimeException(e);
					}
				}
			}
		}
		
		private Client()
		{
			
		}
	}
	
	private ReflectionUtils()
	{
		
	}
}
