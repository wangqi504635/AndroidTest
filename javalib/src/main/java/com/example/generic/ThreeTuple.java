package com.example.generic;

/**
 * Created by wangqi on 15/11/16.
 */
public class ThreeTuple<A, B, C> extends TwoTuple<A, B> {

    public final C c;

    public ThreeTuple(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }
}