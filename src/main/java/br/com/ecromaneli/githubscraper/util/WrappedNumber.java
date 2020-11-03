package br.com.ecromaneli.githubscraper.util;

public final class WrappedNumber {
    Double value = null;

    public WrappedNumber () {}

    public WrappedNumber (Number value) { this.value = value.doubleValue(); }

    public void set(Number value) {
        this.value = value.doubleValue();
    }

    public synchronized WrappedNumber plus(Number value) {
        this.value += value.doubleValue();
        return this;
    }

    public synchronized WrappedNumber minus(Number value) {
        this.value -= value.doubleValue();
        return this;
    }

    public synchronized WrappedNumber times(Number value) {
        this.value *= value.doubleValue();
        return this;
    }

    public synchronized WrappedNumber divide(Number value) {
        this.value /= value.doubleValue();
        return this;
    }

    private boolean isInvalid(Number rawValue) {
        if (rawValue == null) { return true; }

        Double value = rawValue.doubleValue();
        return value.isNaN() || value.isInfinite();
    }

    public WrappedNumber plus(Number value, boolean ignInvalid) {
        return ignInvalid && isInvalid(value) ? this : plus(value);
    }

    public WrappedNumber minus(Number value, boolean ignInvalid) {
        return ignInvalid && isInvalid(value) ? this : minus(value);
    }

    public WrappedNumber times(Number value, boolean ignInvalid) {
        return ignInvalid && isInvalid(value) ? this : times(value);
    }

    public WrappedNumber divide(Number value, boolean ignInvalid) {
        return ignInvalid && isInvalid(value) ? this : divide(value);
    }

    public Number get() {
        return value;
    }

    public Number abs() {
        return Math.abs(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}