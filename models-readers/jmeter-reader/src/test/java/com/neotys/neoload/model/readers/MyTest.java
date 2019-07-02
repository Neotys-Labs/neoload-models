package com.neotys.neoload.model.readers;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class MyTest {


    @Test
    public void testFunction(){
        AtomicInteger test2 = new AtomicInteger(0);
        System.out.println(test2);
        myFunc(test2);
        System.out.println(test2);
    }

    public void myFunc(AtomicInteger test2){
        test2.set(5);
    }
}
