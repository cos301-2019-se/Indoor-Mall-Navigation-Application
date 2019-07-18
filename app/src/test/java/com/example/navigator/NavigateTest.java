package com.example.navigator;

import org.junit.Test;

import java.util.ArrayList;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;


import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NavigateTest {
    @Test
    public void searchList_NeedleExistsInHaystack_ReturnsTrue() {
        Navigate search = new Navigate();
        ArrayList<String> list = new ArrayList<String>();
        list.add("Apple");
        list.add("Pear");
        list.add("Cherry");
        assertWithMessage("NeedleExistInHaystack").that(search.searchList("Pear", list)).isTrue();
    }
    @Test
    public void searchList_NeedleFuzzyExistsInHaystack_ReturnsTrue() {
        Navigate search = new Navigate();
        ArrayList<String> list = new ArrayList<String>();
        list.add("Apple");
        list.add("Pear");
        list.add("Cherry");
        assertWithMessage("NeedleFuzzyExistInHaystack").that(search.searchList("a", list)).isTrue();
    }
    @Test
    public void searchList_NeedleNotExistsInHaystack_ReturnsFalse() {
        Navigate search = new Navigate();
        ArrayList<String> list = new ArrayList<String>();
        list.add("Apple");
        list.add("Pear");
        list.add("Cherry");
        assertWithMessage("NeedleNotExistInHaystack").that(search.searchList("Banana", list)).isFalse();
    }
    @Test
    public void searchList_NeedleNotFuzzyExistsInHaystack_ReturnsFalse() {
        Navigate search = new Navigate();
        ArrayList<String> list = new ArrayList<String>();
        list.add("Apple");
        list.add("Pear");
        list.add("Cherry");
        assertWithMessage("NeedleNotFuzzyExistInHaystack").that(search.searchList("z", list)).isFalse();
    }
}