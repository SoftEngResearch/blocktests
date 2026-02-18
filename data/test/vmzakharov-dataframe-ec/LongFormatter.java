package io.github.vmzakharov.ecdataframe.dataset;

import io.github.vmzakharov.ecdataframe.util.ExceptionFactory;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.impl.utility.StringIterate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

import static io.github.vmzakharov.ecdataframe.util.ExceptionFactory.exceptionByKey;

public class LongFormatter
{
    private DecimalFormat decimalFormat;
    private LongFunction<String> longParser;
    private Pattern nonNumericPattern;

    public LongFormatter(String pattern)
    {
        if (StringIterate.isEmpty(pattern))
        {
            this.longParser = Long::parseLong;
        }
        else if (pattern.length() == 1)
        {
            this.nonNumericPattern = Pattern.compile("[^0-9" + pattern + "]");
            this.longParser = s -> Long.parseLong(this.stripNonNumericCharacters(s));
        }
        else
        {
            this.decimalFormat = new DecimalFormat(pattern);
            this.longParser = s -> {
                // BLOCKTEST EVAL: https://github.com/vmzakharov/dataframe-ec/blob/b5cef516c1f02207399f751ba922f9e5347f8f3c/src/main/java/io/github/vmzakharov/ecdataframe/dataset/LongFormatter.java#L32-L44
                blocktest().given(this.decimalFormat, new DecimalFormat("#000.000##E0")).given(s, "12.0000E0")
                        .checkReturnEq(12, 0.01);
                blocktest().given(this.decimalFormat, new DecimalFormat("#000.000##E0")).given(s, "  1.23456789E8  ")
                        .checkReturnEq(123456789, 0.01);
                blocktest().given(this.decimalFormat, new DecimalFormat("#000.000##E0")).given(s, "funny")
                        .checkReturnEq(null).expect(RuntimeException.class);
                try
                {
                    return this.decimalFormat.parse(s.trim()).longValue();
                }
                catch (ParseException e)
                {
                    throw exceptionByKey("CSV_PARSE_ERR")
                            .with("type", "integer")
                            .with("inputString", s)
                            .get(e);
                }
            };
        }
    }

    public long parseAsLong(String aString)
    {
        if (aString == null || aString.isEmpty())
        {
            return 0L;
        }

        return this.longParser.applyAsLong(aString);
    }

    public String format(long longValue)
    {
        return this.decimalFormat == null ? Long.toString(longValue) : this.decimalFormat.format(longValue);
    }

    private String stripNonNumericCharacters(String s)
    {
        return this.nonNumericPattern.matcher(s).replaceAll("");
    }
}
