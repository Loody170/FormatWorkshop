package org.example;

import org.checkerframework.checker.units.qual.A;

import java.util.Scanner;

public abstract class Converter {

    public abstract void getOptions();
    public abstract boolean checkConversion(String s);
    public abstract void convert();

    public abstract void init();
    public abstract void passID(String id);

    public final void conversion(){
        init();
        while(true) {
            getOptions();
            System.out.println("*** Please type in the name of the format you like to convert to: ***");
            Main.answer = Main.in.nextLine();
            if (checkConversion(Main.answer) == true) {
                convert();
                break;
            } else {
                System.err.println("*** This format is incorrect or not compatiable. Please choose another one ***");
            }
        }//while loop
        System.out.println("*** Your file has been coverted succesfuly, thank you for using Factory WorkShop ***");
    }
}



