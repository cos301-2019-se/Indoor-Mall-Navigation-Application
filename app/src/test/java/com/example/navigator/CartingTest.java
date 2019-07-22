package com.example.navigator;

import org.junit.Test;

import java.util.ArrayList;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;


import static org.junit.Assert.*;

public class CartingTest {
    private static final Carting item = new Carting("12345", "A Product", 65, 3);
    @Test
    public void getPid_GivenTestObject_returnsPid()
    {
        assertWithMessage("getPid").that(item.getPid()).isEqualTo("12345");
    }
    @Test
    public void getPname_GivenTestObject_returnsPname()
    {
        assertWithMessage("getPname").that(item.getPname()).isEqualTo("A Product");
    }
    @Test
    public void getPprice_GivenTestObject_returnsPprice()
    {
        assertWithMessage("getPprice").that(item.getPprice()).isEqualTo(65);
    }
    @Test
    public void getPquantity_GivenTestObject_returnsPquantity()
    {
        assertWithMessage("getPquantity").that(item.getPquantity()).isEqualTo(3);
    }
    @Test
    public void getTotal_GivenTestObject_returnsProductOfQuantityAndPrice()
    {
        assertWithMessage("getTotal").that(item.getTotal()).isEqualTo(195);
    }
}
