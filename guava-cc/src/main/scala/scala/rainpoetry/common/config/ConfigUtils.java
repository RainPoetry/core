package scala.rainpoetry.common.config;

/*
 * User: chenchong
 * Date: 2019/4/19
 * description:
 */

import com.google.common.collect.ImmutableMap;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtils {

	private static final ImmutableMap<String, ByteUnit> byteSuffixes =
			ImmutableMap.<String, ByteUnit>builder()
					.put("b", ByteUnit.BYTE)
					.put("k", ByteUnit.KiB)
					.put("kb", ByteUnit.KiB)
					.put("m", ByteUnit.MiB)
					.put("mb", ByteUnit.MiB)
					.put("g", ByteUnit.GiB)
					.put("gb", ByteUnit.GiB)
					.put("t", ByteUnit.TiB)
					.put("tb", ByteUnit.TiB)
					.put("p", ByteUnit.PiB)
					.put("pb", ByteUnit.PiB)
					.build();

	public static long byteStringAs(String str, ByteUnit unit) {
		String lower = str.toLowerCase(Locale.ROOT).trim();

		try {
			Matcher m = Pattern.compile("([0-9]+)([a-z]+)?").matcher(lower);
			Matcher fractionMatcher = Pattern.compile("([0-9]+\\.[0-9]+)([a-z]+)?").matcher(lower);

			if (m.matches()) {
				long val = Long.parseLong(m.group(1));
				String suffix = m.group(2);

				// Check for invalid suffixes
				if (suffix != null && !byteSuffixes.containsKey(suffix)) {
					throw new NumberFormatException("Invalid suffix: \"" + suffix + "\"");
				}

				// If suffix is valid use that, otherwise none was provided and use the default passed
				return unit.convertFrom(val, suffix != null ? byteSuffixes.get(suffix) : unit);
			} else if (fractionMatcher.matches()) {
				throw new NumberFormatException("Fractional values are not supported. Input was: "
						+ fractionMatcher.group(1));
			} else {
				throw new NumberFormatException("Failed to parse byte string: " + str);
			}

		} catch (NumberFormatException e) {
			String byteError = "Size must be specified as bytes (b), " +
					"kibibytes (k), mebibytes (m), gibibytes (g), tebibytes (t), or pebibytes(p). " +
					"E.g. 50b, 100k, or 250m.";

			throw new NumberFormatException(byteError + "\n" + e.getMessage());
		}
	}

}
