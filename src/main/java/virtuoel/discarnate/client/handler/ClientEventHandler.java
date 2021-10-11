package virtuoel.discarnate.client.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.Input;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;

public class ClientEventHandler
{
	private static int forwardTicks = 0;
	private static int backwardTicks = 0;
	private static int leftTicks = 0;
	private static int rightTicks = 0;
	private static int jumpTicks = 0;
	private static int sneakTicks = 0;
	
	public static void tryHoldKey(KeyBinding key, int millis)
	{
		new Thread(() ->
		{
			synchronized(key)
			{
				try
				{
					MinecraftClient mc = MinecraftClient.getInstance();
					key.setPressed(mc.currentScreen == null || mc.currentScreen.passEvents);
					Thread.sleep(millis);
				}
				catch(InterruptedException e)
				{
					
				}
				finally
				{
					key.setPressed(false);
				}
			}
		}).start();
	}
	
	public static void tryReleaseKey(KeyBinding key)
	{
		new Thread(() ->
		{
			synchronized(key)
			{
				key.setPressed(false);
			}
		}).start();
	}
	
	public static void onInputUpdate(ClientWorld w)
	{
		onInputUpdate(MinecraftClient.getInstance());
	}
	
	public static void onInputUpdate(MinecraftClient mc)
	{
		if (mc.player != null)
		{
			onInputUpdate(mc.player.input, mc.currentScreen);
		}
	}
	
	public static void onInputUpdate(Input input, Screen currentScreen)
	{
		synchronized(ClientEventHandler.class)
		{
			boolean allowInput = currentScreen == null || currentScreen.passEvents;
			
			if(forwardTicks > 0)
			{
				if(allowInput && !input.pressingForward)
				{
					input.movementForward++;
					input.pressingForward = true;
				}
				forwardTicks--;
			}
			
			if(backwardTicks > 0)
			{
				if(allowInput && !input.pressingBack)
				{
					input.movementForward--;
					input.pressingBack = true;
				}
				backwardTicks--;
			}
			
			if(leftTicks > 0)
			{
				if(allowInput && !input.pressingLeft)
				{
					input.movementSideways++;
					input.pressingLeft = true;
				}
				leftTicks--;
			}
			
			if(rightTicks > 0)
			{
				if(allowInput && !input.pressingRight)
				{
					input.movementSideways--;
					input.pressingRight = true;
				}
				rightTicks--;
			}
			
			if(jumpTicks > 0)
			{
				if(allowInput && !input.jumping)
				{
					input.jumping = true;
				}
				jumpTicks--;
			}
			
			if(sneakTicks > 0)
			{
				if(allowInput && !input.sneaking)
				{
					input.sneaking = true;
					input.movementSideways = (float) (input.movementSideways * 0.3D);
					input.movementForward = (float) (input.movementForward * 0.3D);
				}
				sneakTicks--;
			}
		}
	}
	
	public static synchronized int getForwardTicks()
	{
		return forwardTicks;
	}
	
	public static synchronized void setForwardTicks(int ticks)
	{
		forwardTicks = ticks <= 0 ? 0 : ticks;
	}
	
	public static synchronized void addForwardTicks(int ticks)
	{
		forwardTicks = Math.max(0, forwardTicks + ticks);
	}
	
	public static synchronized int getBackwardTicks()
	{
		return backwardTicks;
	}
	
	public static synchronized void setBackwardTicks(int ticks)
	{
		backwardTicks = ticks <= 0 ? 0 : ticks;
	}
	
	public static synchronized void addBackwardTicks(int ticks)
	{
		backwardTicks = Math.max(0, backwardTicks + ticks);
	}
	
	public static synchronized int getLeftTicks()
	{
		return leftTicks;
	}
	
	public static synchronized void setLeftTicks(int ticks)
	{
		leftTicks = ticks <= 0 ? 0 : ticks;
	}
	
	public static synchronized void addLeftTicks(int ticks)
	{
		leftTicks = Math.max(0, leftTicks + ticks);
	}
	
	public static synchronized int getRightTicks()
	{
		return rightTicks;
	}
	
	public static synchronized void setRightTicks(int ticks)
	{
		rightTicks = ticks <= 0 ? 0 : ticks;
	}
	
	public static synchronized void addRightTicks(int ticks)
	{
		rightTicks = Math.max(0, rightTicks + ticks);
	}
	
	public static synchronized int getJumpTicks()
	{
		return jumpTicks;
	}
	
	public static synchronized void setJumpTicks(int ticks)
	{
		jumpTicks = ticks <= 0 ? 0 : ticks;
	}
	
	public static synchronized void addJumpTicks(int ticks)
	{
		jumpTicks = Math.max(0, jumpTicks + ticks);
	}
	
	public static synchronized int getSneakTicks()
	{
		return sneakTicks;
	}
	
	public static synchronized void setSneakTicks(int ticks)
	{
		sneakTicks = ticks <= 0 ? 0 : ticks;
	}
	
	public static synchronized void addSneakTicks(int ticks)
	{
		sneakTicks = Math.max(0, sneakTicks + ticks);
	}
}
