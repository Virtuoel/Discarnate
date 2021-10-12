package virtuoel.discarnate.init;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import virtuoel.discarnate.Discarnate;

public class GameRuleRegistrar
{
	public static final GameRules.Key<GameRules.BooleanRule> PUMPKIN_TO_START = GameRuleRegistry.register(Discarnate.MOD_ID + ".pumpkinStart", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
	public static final GameRules.Key<GameRules.BooleanRule> PUMPKIN_TO_CONTINUE = GameRuleRegistry.register(Discarnate.MOD_ID + ".pumpkinContinue", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
	public static final GameRules.Key<GameRules.IntRule> MIN_LEVEL = GameRuleRegistry.register(Discarnate.MOD_ID + ".minLevel", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(1, 0));
	public static final GameRules.Key<GameRules.IntRule> LEVEL_COST = GameRuleRegistry.register(Discarnate.MOD_ID + ".levelCost", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(1, 0));
	
	public static final GameRuleRegistrar INSTANCE = new GameRuleRegistrar();
	
	private GameRuleRegistrar()
	{
		
	}
}
