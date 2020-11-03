package br.com.ecromaneli.githubscraper.utils.XMLQuery;

import br.com.ecromaneli.githubscraper.utils.WrappedNumber;
import br.com.ecromaneli.githubscraper.utils.XMLQuery.Exception.MalformedDocumentException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryElement {
    private static final Pattern ML_TOKENIZER = Pattern.compile("<([-:\\w]+)\\s*([^>]*)>");

    private String tag;
    private Map<String, String> attributes;
    private String inner;

    public boolean hasAttr(String key) {
        return attributes.containsKey(normalize(key));
    }

    public String attr(String key) {
        return attributes.get(normalize(key));
    }

    public String text() {
        return inner.replaceAll("<[^>]*>", "").trim();
    }

    public String inner() {
        return getInner();
    }

    public String getTag() {
        return tag;
    }

    public String getInner() {
        return inner;
    }


    public static QueryElement parse(String markupText) throws MalformedDocumentException {
        QueryElement qe = new QueryElement();
        Matcher matcher = ML_TOKENIZER.matcher(markupText);
        if (!matcher.find()) { throw new MalformedDocumentException(); }

        qe.tag = matcher.group(1);
        qe.attributes = parseAttributes(matcher.group(2));

        int end = findTagEnding(qe.tag, markupText, matcher.start());
        qe.inner = markupText.substring(matcher.end(), end);
        return qe;
    }

    private static String normalize(String str) {
        return str.toLowerCase(Locale.US);
    }

    private static Map<String, String> parseAttributes(String rawAttributes) {
        Map<String, String> attrs = new HashMap<>();
        char[] rawAttrChars = normalize(rawAttributes).toCharArray();
        StringBuilder attr = new StringBuilder();
        StringBuilder value = new StringBuilder();
        Character cachedDelimiter = null;

        for (char c : rawAttrChars) {
            if (cachedDelimiter != null) {
                if (c == cachedDelimiter) {
                    cachedDelimiter = null;
                } else {
                    value.append(c);
                }
            } else {
                if (c == '\'' || c == '"') {
                    cachedDelimiter = c;
                } else if (c == ' ') {
                    if (attr.length() != 0) {
                        attrs.put(attr.toString(), value.toString());
                    }
                    attr = new StringBuilder();
                    value = new StringBuilder();
                } else if (c != '=') {
                    attr.append(c);
                }
            }
        }
        if (attr.length() != 0) { attrs.put(attr.toString(), value.toString()); }
        return attrs;
    }

    private static int findTagEnding(String tag, String data, int begin) throws MalformedDocumentException {
        final WrappedNumber endIndex = new WrappedNumber();
        final WrappedNumber scripts = new WrappedNumber(0);

        final String subData = data.substring(begin);
        final String tagEnding = "</" + tag;

        findOccurrences(subData, tagEnding, (count, index) -> {
            int actualIndex = index + tagEnding.length();

            char divider;
            do { divider = subData.charAt(actualIndex++); } while (divider == ' ');
            if (divider != '>') { return true;  }

            if (isScriptOrStyle(subData, index)) {
                scripts.plus(1);
                return true;
            }

            String candidateData = subData.substring(0, actualIndex);
            if (foundMoreThen(tag, candidateData, count - scripts.get().intValue())) {
                return true;
            }

            endIndex.set(begin + index);
            return false;
        });

        if (endIndex.get() == null) { throw new MalformedDocumentException(); }

        return endIndex.get().intValue();
    }

    private static boolean foundMoreThen(String tag, String data, final int max) {
        final String tagOpening = "<" + tag;
        final String subData = data.substring(tagOpening.length());
        return !findOccurrences(subData, tagOpening, (count, index) -> {
            char divider = subData.charAt(index + tagOpening.length());
            return (divider != ' ' && divider != '>') || count != max;
        });
    }

    /**
     * Find all occurrences of an substring. Case-insensitive.
     *
     * @param str    Initial string.
     * @param subStr Substring to search for.
     * @param fn     Handler. Receive occurrence number and index.
     * @return If handler returns false, return false. Otherwise, true.
     */
    private static boolean findOccurrences(String str, String subStr, BiFunction<Integer, Integer, Boolean> fn) {
        int index = 0;
        int count = 0;
        str = str.toLowerCase(Locale.US);
        subStr = subStr.toLowerCase(Locale.US);
        while ((index = str.indexOf(subStr, index)) != -1) {
            if (!fn.apply(++count, index)) { return false; }
            index += subStr.length();
        }
        return true;
    }

    protected static boolean isScriptOrStyle(String data, int index) {
        String prefix = data.substring(index + 1).toLowerCase(Locale.US);

        int nextScriptEnding = prefix.indexOf("</script");
        if (nextScriptEnding != -1) {
            String scriptData = prefix.substring(0, nextScriptEnding);
            if (!scriptData.contains("<script")) { return true; }
        }

        int nextStyleEnding = prefix.indexOf("</style");
        if (nextStyleEnding != -1) {
            String styleData = prefix.substring(0, nextStyleEnding);
            return !styleData.contains("<style");
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("<" + tag);
        if (attributes != null) {
            attributes.forEach((attr, value) ->
                    builder.append(" ").append(attr).append("=\"")
                            .append(value).append("\""));
        }
        builder.append("/>");
        return builder.toString();
    }
}
