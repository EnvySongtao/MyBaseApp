package com.gst.javatest;

import com.gst.javatest.test.SynchronizedTest;

import java.text.NumberFormat;

public class MyClass {

    public  static void main(String[] args){

//        yearMoney(20,0.0256,100000,2000);
        new SynchronizedTest().test();
    }



    private static void yearMoney(int years,double yearlyRate,int startMoney,int monthMoney){
        int months=years*12;
        double monthRate=yearlyRate/12;
        double money= startMoney;
        for (int i=0;i<months;i++){
            money=money+money*monthRate+monthMoney;
        }
        NumberFormat nf=NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        System.out.print("years = "+years+" , yearlyRate= "+nf.format(yearlyRate)+", month income = "+monthMoney+", balance="+nf.format(money));
    }
}
