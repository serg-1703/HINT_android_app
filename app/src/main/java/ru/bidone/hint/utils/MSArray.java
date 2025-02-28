package ru.bidone.hint.utils;

import java.util.ArrayList;
import java.util.List;

public class MSArray {
    public List<String> values = new ArrayList<>();

    public List<String> getValues() {
        return values;
    }

    public String get(int position) {
        return values.size() <= position ? "" : values.get(position);
    }

    public void add(String str) {
        values.add(str);
    }

    public int size() {
        return values.size();
    }
}
