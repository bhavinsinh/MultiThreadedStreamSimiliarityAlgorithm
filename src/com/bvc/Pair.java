package com.bvc;

/**
 * Created by bhavinchauhan on 4/19/16.
 */

public class Pair<F, S> {
    private F first;
    private S second;

    public Pair(S second, F first) {
        this.second = second;
        this.first = first;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }
}
