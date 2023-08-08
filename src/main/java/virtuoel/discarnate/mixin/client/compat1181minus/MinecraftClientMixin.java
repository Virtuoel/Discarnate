package virtuoel.discarnate.mixin.client.compat1181minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.MinecraftClient;
import virtuoel.discarnate.util.DiscarnateMinecraftClientExtensions;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements DiscarnateMinecraftClientExtensions
{
	@Shadow
	abstract void doAttack();
	
	@Override
	public void discarnate_doAttack()
	{
		doAttack();
	}
}
