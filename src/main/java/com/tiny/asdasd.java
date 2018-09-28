package com.tiny;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: PeterPuff
 * @date : 2018-09-27 下午8:03
 */
public class asdasd {

    static void m(List<String> ...stringLists) {


        Object[] array = stringLists;

        List<Integer> tmpList = Arrays.asList(42);

        array[0] = tmpList; // Semantically invalid, but compiles without warnings

        // s=abc
        String s = ObjectUtils.identityToString(stringLists[0].get(0)); // Oh no, ClassCastException at runtime!

        List<String> b = stringLists[0];

        System.out.println(" " + 123);
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("abc");
        m(list);
    }

}
