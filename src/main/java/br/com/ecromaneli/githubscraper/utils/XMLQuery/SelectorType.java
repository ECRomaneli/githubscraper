package br.com.ecromaneli.githubscraper.utils.XMLQuery;

public enum SelectorType {
    TAG, CLASS, ID, ATTR;

    public static SelectorType parse(String rawSelector) {
        char firstChar = rawSelector.trim().charAt(0);
        if (firstChar == '.') { return CLASS; }
        if (firstChar == '#') { return ID; }
        if (firstChar == '[') { return ATTR; }
        return TAG;
    }
}
