package com.example.burak.bluetoothkontroltest;

/**
 * Created by user on 05/05/2018.
 */

public class MyThread extends Thread
{
    @Override
    public  void run()
    {
        try {
            for (int i = 0; i < 10; ++i) {
                System.out.println(i);
                Thread.sleep(15000);
            }


        }
        catch (InterruptedException ex) {
            //...
        }
    }
}
