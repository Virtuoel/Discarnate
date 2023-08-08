package virtuoel.discarnate.mixin.client.compat1182plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.MinecraftClient;
import virtuoel.discarnate.util.DiscarnateMinecraftClientExtensions;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements DiscarnateMinecraftClientExtensions
{
	@Shadow
	abstract boolean doAttack();
	
	@Override
	public void discarnate_doAttack()
	{
		doAttack();
	}
}
