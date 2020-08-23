package de.alb654.AlbyCloud.utils;

import java.util.ArrayList;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 14.10.2019.
 */
public class LimitedArray<T> {

    private ArrayList<T> array = new ArrayList<>();

    private int startPointer = 0;
    private int endPointer = 0;

    private int maxSize;
    private int size = 0;

    public LimitedArray(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized void add(T element) {
        if(size < maxSize) {
            array.add(element);
            size++;
        } else
            array.set(endPointer, element);

        if(endPointer + 1 >= maxSize) {
            endPointer = 0;
            startPointer++;
        } else {
            if(startPointer > endPointer)
                if(startPointer + 1 >= maxSize)
                    startPointer = 0;
                else
                    startPointer++;
            endPointer++;
        }
    }

    public synchronized T get(int index) {
        int sp = size >= maxSize ? startPointer - 1  :  startPointer;
        int i = sp + index;
        if(i > size - 1) {
            i -= size;
        }
        if(i < 0)
            i = 0;
        return array.get(i);
    }

    public synchronized boolean contains(T element) {
        return array.contains(element);
    }

    public int size() {
        return array.size();
    }
}
