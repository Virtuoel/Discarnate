package virtuoel.discarnate.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.LocalDifficulty;

@Mixin(MobEntity.class)
public interface MobEntityAccessor
{
	@Accessor
	GoalSelector getGoalSelector();
	
	@Accessor
	GoalSelector getTargetSelector();
	
	@Accessor
	void setExperiencePoints(int experience);
	
	@Invoker
	void callInitEquipment(LocalDifficulty difficulty);
}
