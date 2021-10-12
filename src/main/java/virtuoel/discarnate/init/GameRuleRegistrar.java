package virtuoel.discarnate.init;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import virtuoel.discarnate.Discarnate;
import virtuoel.discarnate.reference.DiscarnateConfig;

public class GameRuleRegistrar
{
	public static final GameRules.Key<GameRules.BooleanRule> PUMPKIN_TO_START = GameRuleRegistry.register(Discarnate.MOD_ID + ".pumpkinStart", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) -> DiscarnateConfig.requirePumpkinToStart = rule.get()));
	public static final GameRules.Key<GameRules.BooleanRule> PUMPKIN_TO_CONTINUE = GameRuleRegistry.register(Discarnate.MOD_ID + ".pumpkinContinue", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false, (server, rule) -> DiscarnateConfig.requirePumpkinToContinue = rule.get()));
	public static final GameRules.Key<GameRules.IntRule> MIN_LEVEL = GameRuleRegistry.register(Discarnate.MOD_ID + ".minLevel", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(1, 0, (server, rule) -> DiscarnateConfig.minExpLevel = rule.get()));
	public static final GameRules.Key<GameRules.IntRule> LEVEL_COST = GameRuleRegistry.register(Discarnate.MOD_ID + ".levelCost", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(1, 0, (server, rule) -> DiscarnateConfig.expLevelCost = rule.get()));
	
	public static final GameRuleRegistrar INSTANCE = new GameRuleRegistrar();
	
	private GameRuleRegistrar()
	{
		
	}
}
