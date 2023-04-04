package com.sawitpro;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilTests {

    @Test
    public void checkIsAlphaNumeric(){
        boolean i = Utils.isAlphaNumeric("long ago");
        assertTrue(i);
    }

    @Test
    public void checkIsAlphaNumeric2(){
        boolean i = Utils.isAlphaNumeric("point");
        assertTrue(i);
    }

    @Test
    public void checkIsAlphaNumeric3(){
        boolean i = Utils.isAlphaNumeric("who");
        assertTrue(i);
    }

    @Test
    public void checkIsAlphaNumeric4(){
        boolean i = Utils.isAlphaNumeric("which how many");
        assertTrue(i);
    }

    @Test
    public void checkIsAlphaNumeric5(){
        boolean i = Utils.isAlphaNumeric("duō");
        assertFalse(i);
    }

    @Test
    public void checkIsAlphaNumeric6(){
        boolean i = Utils.isAlphaNumeric("jiǔ");
        assertFalse(i);
    }

    @Test
    public void checkIsAlphaNumeric7(){
        boolean i = Utils.isAlphaNumeric("多");
        assertFalse(i);
    }

    @Test
    public void checkIsAlphaNumeric8(){
        boolean i = Utils.isAlphaNumeric("钱");
        assertFalse(i);
    }

    @Test
    public void checkIsAlphaNumeric9(){
        boolean i = Utils.isAlphaNumeric("shuí, shéi");
        assertFalse(i);
    }

    @Test
    public void checkIsAlphaNumeric10(){
        boolean i = Utils.isAlphaNumeric("hello-world");
        assertTrue(i);
    }

    @Test
    public void checkIsAlphaNumeric11(){
        boolean i = Utils.isAlphaNumeric("hello_world");
        assertTrue(i);
    }

    @Test
    public void checkIsAlphaNumeric12(){
        boolean i = Utils.isAlphaNumeric("shuí-shéi");
        assertFalse(i);
    }
}
