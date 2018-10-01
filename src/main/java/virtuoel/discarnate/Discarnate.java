package virtuoel.discarnate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import virtuoel.discarnate.init.BlockRegistrar;

@Mod(modid = Discarnate.MOD_ID, version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class Discarnate
{
	public static final String MOD_ID = "discarnate";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID)
	{
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack createIcon()
		{
			return new ItemStack(BlockRegistrar.SPIRIT_WELL);
		}
	};
	
	@Mod.EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event)
	{
		LOGGER.error("Expecting signature {}, however there is no signature matching that description. The file {} may have been tampered with. This version will NOT be supported by the author!", event.getExpectedFingerprint(), event.getSource().getName());
	}
}
