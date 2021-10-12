package virtuoel.discarnate.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;

@Mixin(MobEntity.class)
public interface MobEntityAccessor
{
	@Accessor
	GoalSelector getGoalSelector();
	
	@Accessor
	GoalSelector getTargetSelector();
	
	@Accessor
	void setExperiencePoints(int experience);
}
