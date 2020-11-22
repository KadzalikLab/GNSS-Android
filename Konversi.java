package com.kadzalik.kalkulator;

public class Konversi {

    public static int [] DesKeDms(double desimal){
        int derajat=(int)desimal;
        double menit=(desimal%1)*60;
        double detik=Math.round((menit%1)*60);

        return new int[]{0,derajat,(int)menit,(int)detik};
    }

}
