package virtuoel.discarnate.util;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

public class I18nUtils
{
	public static final Object[] EMPTY_VARARGS = new Object[0];
	
	public static MutableText translate(final String unlocalized, final String defaultLocalized)
	{
		return translate(unlocalized, defaultLocalized, EMPTY_VARARGS);
	}
	
	public static MutableText translate(final String unlocalized, final String defaultLocalized, final Object... args)
	{
		return new TranslatableText(unlocalized, args);
	}
	
	public static MutableText literal(final String text, final Object... args)
	{
		return new LiteralText(String.format(text, args));
	}
}
