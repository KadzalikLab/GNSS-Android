package com.kadzalik.kalkulator;

public class Nama {

      static String hari (int nomorhari){

        String namahari="";
        if (nomorhari==1)namahari="Ahad";
        else if (nomorhari==2)namahari="Senin";
        else if (nomorhari==3)namahari="Selasa";
        else if (nomorhari==4)namahari="Rabu";
        else if (nomorhari==5)namahari="Kamis";
        else if (nomorhari==6)namahari="Jum'at";
        else if (nomorhari==7||nomorhari==0)namahari="Sabtu";
        return namahari;
    }

      static String pasaran (int nomorpasaran){
        String namapasaran="";
        if (nomorpasaran==1)namapasaran="Legi";
        else if (nomorpasaran==2)namapasaran="Pahing";
        else if (nomorpasaran==3)namapasaran="Pon";
        else if (nomorpasaran==4)namapasaran="Wage";
        else if (nomorpasaran==5||nomorpasaran==0)namapasaran="Kliwon";
        return namapasaran;






    }
}
