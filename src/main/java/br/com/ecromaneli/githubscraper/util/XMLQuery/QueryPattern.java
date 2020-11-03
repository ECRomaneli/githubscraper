package br.com.ecromaneli.githubscraper.util.XMLQuery;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.regex.Pattern;

@AllArgsConstructor
public enum QueryPattern {
    TAG("<{{1}}[\\s>]"),
    ATTR("<[^>]+\\s{{1}}[\\s=>]"),
    ATTR_VALUE("<[^>]+\\s{{1}}\\s*=\\s*.([^'\"]*\\s)?{{2}}[\\s'\"]");

    @Getter
    private String pattern;

    public Pattern getPattern(String... tokens) {
        String regex = pattern.replace("{{1}}", tokens[0]);

        if (tokens.length > 1 && tokens[1] != null) {
            regex = regex.replace("{{2}}", tokens[1]);
        }

        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    public static Pattern getPattern(Selector selector) {
        QueryPattern qp = TAG;

        if (!selector.getType().equals(SelectorType.TAG)) {
            qp = selector.getValue() != null ? ATTR_VALUE : ATTR;
        }

        return qp.getPattern(selector.getName(), selector.getValue());
    }
}
