package virtuoel.discarnate.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import virtuoel.discarnate.Discarnate;

@EventBusSubscriber(modid = Discarnate.MOD_ID, value = Side.CLIENT)
public class ClientEventHandler
{
	private static int forwardTicks = 0;
	private static int backwardTicks = 0;
	private static int leftTicks = 0;
	private static int rightTicks = 0;
	private static int jumpTicks = 0;
	private static int sneakTicks = 0;
	private static int digTicks = 0;
	private static int useItemTicks = 0;
	
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event)
	{
		if(event.phase == Phase.END)
			return;
		
		boolean allowInput = Minecraft.getMinecraft().currentScreen == null || Minecraft.getMinecraft().currentScreen.allowUserInput;
		
		synchronized(ClientEventHandler.class)
		{
			if(digTicks > 0)
			{
				if(allowInput)
				{
					int key = Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode();
					KeyBinding.onTick(key);
				}
				digTicks--;
			}
			
			if(useItemTicks > 0)
			{
				if(allowInput)
				{
					int key = Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode();
					KeyBinding.onTick(key);
				}
				useItemTicks--;
			}
		}
	}
	
	public static synchronized int getDigTicks()
	{
		return digTicks;
	}
	
	public static synchronized void setDigTicks(int ticks)
	{
		digTicks = ticks <= 0 ? 0 : ticks;
	}
	
	public static synchronized void addDigTicks(int ticks)
	{
		digTicks = Math.max(0, digTicks + ticks);
	}
	
	public static synchronized int getUseItemTicks()
	{
		return useItemTicks;
	}
	
	public static synchronized void setUseItemTicks(int ticks)
	{
		useItemTicks = ticks <= 0 ? 0 : ticks;
	}
	
	public static synchronized void addUseItemTicks(int ticks)
	{
		useItemTicks = Math.max(0, useItemTicks + ticks);
	}
	
	@SubscribeEvent
	public static void onInputUpdate(InputUpdateEvent event)
	{
		boolean allowInput = Minecraft.getMinecraft().currentScreen == null || Minecraft.getMinecraft().currentScreen.allowUserInput;
		
		MovementInput input = event.getMovementInput();
		
		synchronized(ClientEventHandler.class)
		{
			if(forwardTicks > 0)
			{
				if(allowInput && !input.forwardKeyDown)
				{
					input.moveForward++;
					input.forwardKeyDown = true;
				}
				forwardTicks--;
			}
			
			if(backwardTicks > 0)
			{
				if(allowInput && !input.backKeyDown)
				{
					input.moveForward--;
					input.backKeyDown = true;
				}
				backwardTicks--;
			}
			
			if(leftTicks > 0)
			{
				if(allowInput && !input.leftKeyDown)
				{
					input.moveStrafe++;
					input.leftKeyDown = true;
				}
				leftTicks--;
			}
			
			if(rightTicks > 0)
			{
				if(allowInput && !input.rightKeyDown)
				{
					input.moveStrafe--;
					input.rightKeyDown = true;
				}
				rightTicks--;
			}
			
			if(jumpTicks > 0)
			{
				if(allowInput && !input.jump)
				{
					input.jump = true;
				}
				jumpTicks--;
			}
			
			if(sneakTicks > 0)
			{
				if(allowInput && !input.sneak)
				{
					input.sneak = true;
					input.moveStrafe = (float) (input.moveStrafe * 0.3D);
					input.moveForward = (float) (input.moveForward * 0.3D);
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
