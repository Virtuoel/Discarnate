package virtuoel.discarnate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import virtuoel.discarnate.api.ITaskManager;
import virtuoel.discarnate.api.TriConsumer;
import virtuoel.discarnate.init.BlockRegistrar;
import virtuoel.discarnate.init.ItemRegistrar;
import virtuoel.discarnate.network.CPacketActivateChanneler;
import virtuoel.discarnate.proxy.GuiProxy;

@Mod(modid = Discarnate.MOD_ID, version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class Discarnate implements ITaskManager
{
	public static final String MOD_ID = "discarnate";
	
	@Mod.Instance(MOD_ID)
	public static Discarnate instance;
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
	
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID)
	{
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack createIcon()
		{
			return new ItemStack(BlockRegistrar.SPIRIT_CHANNELER);
		}
	};
	
	static
	{
		NETWORK.registerMessage(CPacketActivateChanneler.Handler.class, CPacketActivateChanneler.class, 0, Side.SERVER);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
		
		ItemRegistrar.init();
	}
	
	@Mod.EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event)
	{
		LOGGER.error("Expecting signature {}, however there is no signature matching that description. The file {} may have been tampered with. This version will NOT be supported by the author!", event.getExpectedFingerprint(), event.getSource().getName());
	}
	
	private Map<ResourceLocation, TriConsumer<ItemStack, EntityPlayer, TileEntity>> tasks = new HashMap<>();
	private static final ResourceLocation AIR_ID = new ResourceLocation("air");
	
	@Override
	public boolean addTask(@Nonnull ResourceLocation name, @Nonnull TriConsumer<ItemStack, EntityPlayer, TileEntity> task)
	{
		return !AIR_ID.equals(name) && tasks.putIfAbsent(name, task) == null;
	}
	
	@Override
	public Optional<TriConsumer<ItemStack, EntityPlayer, TileEntity>> removeTask(@Nonnull ResourceLocation name)
	{
		return Optional.ofNullable(tasks.remove(name));
	}
	
	@Override
	public Optional<TriConsumer<ItemStack, EntityPlayer, TileEntity>> getTask(@Nonnull ResourceLocation name)
	{
		return Optional.ofNullable(tasks.get(name));
	}
}
