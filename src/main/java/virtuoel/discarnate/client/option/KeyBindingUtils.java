package virtuoel.discarnate.client.option;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class KeyBindingUtils
{
	private static final Map<KeyBinding, Thread> KEY_THREAD_MAP = new HashMap<>();
	
	public static void tryHoldKey(KeyBinding key, int millis)
	{
		Thread t = new Thread(() ->
		{
			synchronized (key)
			{
				try
				{
					Thread.sleep(millis);
				}
				catch (InterruptedException e)
				{
					
				}
				finally
				{
					key.setPressed(false);
				}
			}
		});
		
		tryHoldKey(key);
		
		KEY_THREAD_MAP.put(key, t);
		
		t.start();
	}
	
	public static void tryHoldKey(KeyBinding key)
	{
		final MinecraftClient mc = MinecraftClient.getInstance();
		tryPressKey(key, mc.currentScreen == null);
	}
	
	public static void tryToggleKey(KeyBinding key)
	{
		final MinecraftClient mc = MinecraftClient.getInstance();
		tryPressKey(key, !key.isPressed() && (mc.currentScreen == null));
	}
	
	public static void tryReleaseKey(KeyBinding key)
	{
		tryPressKey(key, false);
	}
	
	public static void tryPressKey(KeyBinding key, boolean pressed)
	{
		final Thread old = KEY_THREAD_MAP.remove(key);
		if (old != null)
		{
			old.interrupt();
		}
		
		key.setPressed(pressed);
	}
}
