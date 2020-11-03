package br.com.ecromaneli.githubscraper.util.XMLQuery;

import br.com.ecromaneli.githubscraper.util.XMLQuery.Exception.MalformedDocumentException;
import br.com.ecromaneli.githubscraper.util.XMLQuery.Exception.SelectorFormatException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MLQuery {
    private static final QueryElements EMPTY = new QueryElements();
    private QueryElements elements;
    private MLQuery prevObject;

    public MLQuery(String xml) {
        elements = new QueryElements();
        try {
            elements.put(0, QueryElement.parse(xml));
        } catch (MalformedDocumentException e) {
            e.printStackTrace();
            elements = EMPTY;
        }
    }

    public MLQuery(QueryElement element) {
        this.prevObject = null;
        this.elements = new QueryElements();
        this.elements.put(0, element);
    }

    private MLQuery(QueryElements elements, MLQuery prevObject) {
        this.prevObject = prevObject;
        this.elements = elements;
    }

    /**
     * Return tag content of the first element.
     * @return Return tag content.
     */
    public String inner() {
        if (elements == null || elements.isEmpty()) { return ""; }
        return get(0).inner();

    }

    /**
     * Remove all tags of the first element and return. Is not script and style safe.
     * @return Return only the text between tags.
     */
    public String text() {
        if (elements == null || elements.isEmpty()) { return ""; }
        return get(0).text();
    }

    public String attr(String attr) {
        if (elements == null || elements.isEmpty()) { return ""; }
        return get(0).attr(attr);
    }

    public int size() {
        return elements.size();
    }

    public void each(BiConsumer<QueryElement, Integer> biConsumer) {
        int index = 0;
        Collection<QueryElement> elements = this.elements.values();
        for (QueryElement element : elements) {
            biConsumer.accept(element, index);
        }
    }

    public QueryElement get(int index) {
        if (Math.abs(index) >= elements.size()) { throw new IndexOutOfBoundsException(); }
        Iterator<QueryElement> it = elements.values().iterator();

        if (index < 0) { index = elements.size() + index; }
        while (it.hasNext()) {
            if (index-- == 0) { return it.next(); }
        }

        throw new IndexOutOfBoundsException();
    }

    public MLQuery filter(String rawSelector) {
        try {
            MLQuery query = this;
            List<Selector> selectors = Selector.parse(rawSelector);
            for (Selector selector : selectors) { query = query.filter(selector); }
            return query;
        } catch (SelectorFormatException e) {
            e.printStackTrace();
            return new MLQuery(EMPTY, this);
        }
    }

    public MLQuery filter(final Selector selector) {
        final QueryElements newElements = new QueryElements();
        elements.forEach((index, element) -> {
            if (selector.getType().equals(SelectorType.TAG)) {
                if (element.getTag().equalsIgnoreCase(selector.getName())) {
                    newElements.put(index, element);
                }
                return;
            }

            if (!element.hasAttr(selector.getName())) { return; }

            if (selector.getValue() == null) {
                newElements.put(index, element);
                return;
            }

            String attrValue = element.attr(selector.getName());
            if (Pattern
                    .compile("(^|\\s)" + selector.getValue() + "(\\s|$)", Pattern.CASE_INSENSITIVE)
                    .matcher(attrValue)
                    .find()) {
                newElements.put(index, element);
            }

        });
        return new MLQuery(newElements, this);
    }

    public MLQuery find(String rawSelector) {
        try {
            List<Selector> selectors = Selector.parse(rawSelector);
            MLQuery query = find(selectors.get(0));
            for (int i = 1; i < selectors.size(); i++) { query = query.filter(selectors.get(i)); }
            return query;
        } catch (SelectorFormatException e) {
            e.printStackTrace();
            return new MLQuery(EMPTY, this);
        }
    }

    private MLQuery find(final Selector selector) {
        final QueryElements foundElements = new QueryElements();
        elements.values().stream().forEach((data) -> findInto(data.inner(), selector, foundElements));
        return new MLQuery(foundElements, this);
    }

    private void findInto(String data, Selector selector, QueryElements elements) {
        Matcher matcher = QueryPattern.getPattern(selector).matcher(data);

        while (matcher.find()) {
            int begin = matcher.start();

            // Verify if this index already exists into the elements and if this match are inside script or style
            if (elements.containsKey(begin) || QueryElement.isScriptOrStyle(data, begin)) { continue; }

            try {
                elements.put(begin, QueryElement.parse(data.substring(begin)));
            } catch (MalformedDocumentException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static MLQuery from(String xml) {
        return new MLQuery(xml);
    }
}
