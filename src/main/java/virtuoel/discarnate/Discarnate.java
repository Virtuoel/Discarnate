package virtuoel.discarnate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import virtuoel.discarnate.api.DiscarnateAPI;
import virtuoel.discarnate.api.ITaskManager;
import virtuoel.discarnate.init.BlockRegistrar;
import virtuoel.discarnate.init.ItemRegistrar;

@Mod(modid = Discarnate.MOD_ID, version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class Discarnate implements ITaskManager
{
	public static final String MOD_ID = "discarnate";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@Mod.Instance(MOD_ID)
	public static Discarnate instance;
	
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID)
	{
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack createIcon()
		{
			return new ItemStack(BlockRegistrar.SPIRIT_CHANNELER);
		}
	};
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		DiscarnateAPI.instance().addTask(ItemRegistrar.TEMPLATE_TASK, (s, p) ->
		{});
	}
	
	@Mod.EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event)
	{
		LOGGER.error("Expecting signature {}, however there is no signature matching that description. The file {} may have been tampered with. This version will NOT be supported by the author!", event.getExpectedFingerprint(), event.getSource().getName());
	}
	
	private Map<ResourceLocation, BiConsumer<ItemStack, EntityPlayer>> tasks = new HashMap<>();
	
	@Override
	public boolean addTask(ResourceLocation name, @Nonnull BiConsumer<ItemStack, EntityPlayer> task)
	{
		return tasks.putIfAbsent(name, task) == null;
	}
	
	@Override
	public Optional<BiConsumer<ItemStack, EntityPlayer>> removeTask(ResourceLocation name)
	{
		return Optional.ofNullable(tasks.remove(name));
	}
	
	@Override
	public Optional<BiConsumer<ItemStack, EntityPlayer>> getTask(ResourceLocation name)
	{
		return Optional.ofNullable(tasks.get(name));
	}
}
