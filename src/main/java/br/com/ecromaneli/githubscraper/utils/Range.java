package br.com.ecromaneli.githubscraper.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Range<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<T> interval;

    public Range() {
        interval = new ArrayList<>(2);
        interval.add(null);
        interval.add(null);
    }

    public Range(T min, T max) {
        this ();
        min(min).max(max);
    }

    public T min() { return interval.get(0); }
    public Range<T> min(T min) {
        interval.set(0, min);
        return this;
    }
    public Range<T> min(Function<T, T> minFn) {
        interval.set(0, minFn.apply(interval.get(0)));
        return this;
    }

    public T max() { return interval.get(1); }
    public Range<T> max(T max) {
        interval.set(1, max);
        return this;
    }
    public Range<T> max(Function<T, T> maxFn) {
        interval.set(1, maxFn.apply(interval.get(1)));
        return this;
    }

    private boolean hasNull() {
        if (min() == null || max() == null) {
            System.err.println("Range has null value.");
            return true;
        }
        return false;
    }

    public boolean between(Date date) {
        return !hasNull() && date.after((Date) min()) && date.before((Date) max());
    }

    public boolean betweenOrEqual(Date date) {
        if (between(date)) { return true; }
        if (hasNull()) { return false; }

        long dateMillis = date.getTime();

        return ((Date) min()).getTime() == dateMillis
                || ((Date) max()).getTime() == dateMillis;
    }

    public boolean between(Number number) {
        if (hasNull()) { return false; }
        return number.doubleValue() > (Double) min()
                && number.doubleValue() < (Double) max();
    }

    public boolean betweenOrEqual(Number number) {
        if (hasNull()) { return false; }
        return number.doubleValue() >= (Double) min()
                && number.doubleValue() <= (Double) max();
    }

    public void each(Consumer<T> consumer) {
        interval.stream().forEach(consumer);
    }

    public void map(Function<T, T> function) {
        interval = interval.stream().map(function).collect(Collectors.toList());
    }

    public boolean any(Predicate<T> function) {
        return interval.stream().anyMatch(function);
    }

    @Override
    public Range<T> clone() {
        return new Range<>(min(), max());
    }

    @Override
    public String toString() {
        return "{ " + min() + ", " + max() + " }";
    }
}

