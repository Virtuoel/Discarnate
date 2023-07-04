package virtuoel.discarnate.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class I18nUtils
{
	public static final Object[] EMPTY_VARARGS = new Object[0];
	
	public static MutableText translate(final String unlocalized, final String defaultLocalized)
	{
		return translate(unlocalized, defaultLocalized, EMPTY_VARARGS);
	}
	
	public static MutableText translate(final String unlocalized, final String defaultLocalized, final Object... args)
	{
		return Text.translatable(unlocalized, args);
	}
	
	public static MutableText literal(final String text, final Object... args)
	{
		return Text.literal(String.format(text, args));
	}
}
