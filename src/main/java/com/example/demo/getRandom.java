package com.example.demo;


import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class getRandom {


    int min=1000;




    int max=9999;

    Random random= new Random();
    public long getrandom()
    {
        return random.nextInt(max-min+1)+min;
    }

}
