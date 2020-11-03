package br.com.ecromaneli.githubscraper.utils.XMLQuery;

import br.com.ecromaneli.githubscraper.utils.XMLQuery.Exception.SelectorFormatException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Selector {
    private static final Pattern SELECTOR_VALIDATOR = Pattern.compile("^([\\w-]+|[.#][\\w-]+|\\[[\\w-]+(=(['\"])?[\\w-]+\\3?)?])$");
    private static final Pattern SELECTOR_TOKENIZER = Pattern.compile("([\\w-]+)");

    private SelectorType type;
    private String name;
    private String value;

    public static List<Selector> parse(String rawSelectorsStr) throws SelectorFormatException {
        List<Selector> selectors = new ArrayList<>();

        String[] rawSelectors = rawSelectorsStr.split("(?=#|\\.|\\[)");
        for (String rawSelector : rawSelectors) {
            selectors.add(parseSelector(rawSelector));
        }

        return selectors;
    }

    private static Selector parseSelector(String rawSelector) throws SelectorFormatException {
        rawSelector = rawSelector.trim();
        Selector.validateSelector(rawSelector);

        Selector obj = new Selector();
        obj.type = SelectorType.parse(rawSelector);

        List<String> tokens = getSelectorTokens(rawSelector);

        if (obj.type == SelectorType.TAG) {
            obj.name = tokens.get(0);

        } else if (obj.type == SelectorType.ID || obj.type == SelectorType.CLASS) {
            obj.name = obj.type.name();
            obj.value = tokens.get(0);

        } else if (obj.type == SelectorType.ATTR) {
            obj.name = tokens.get(0);
            if (tokens.size() > 1) { obj.value = tokens.get(1); }
        }

        return obj;
    }

    private static List<String> getSelectorTokens(String rawSelector) {
        List<String> tokens = new ArrayList<>(2);
        Matcher matcher = SELECTOR_TOKENIZER.matcher(rawSelector);
        while (matcher.find()) { tokens.add(matcher.group()); }
        return tokens;
    }

    private static void validateSelector(String rawSelector) throws SelectorFormatException {
        if (!SELECTOR_VALIDATOR.matcher(rawSelector).matches()) {
            throw new SelectorFormatException();
        }
    }

    @Override
    public String toString() {
        if (getName() == null) { return super.toString(); }

        if (getType().equals(SelectorType.TAG)) { return getName(); }
        if (getType().equals(SelectorType.CLASS)) { return '.' + getValue(); }
        if (getType().equals(SelectorType.ID)) { return '#' + getValue(); }

        // ATTR
        String suffix = "]";
        if (getValue() != null) { suffix = "=\"" + getValue() + "\"" + suffix; }
        return "[" + getName() + suffix;
    }
}
