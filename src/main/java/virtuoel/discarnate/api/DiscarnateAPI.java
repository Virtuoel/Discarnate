package virtuoel.discarnate.api;

import net.minecraftforge.fml.common.Mod;

public class DiscarnateAPI
{
	@Mod.Instance("discarnate")
	private static ITaskManager instance = new ITaskManager()
	{};
	
	public static ITaskManager instance()
	{
		return instance;
	}
}
