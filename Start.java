/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package start;

import java.util.Random;

class Pociag extends Thread 
{
    static int DWORZEC=1;
    static int START=2;
    static int PRZEJAZD=3;
    static int KONIEC_PRZEJAZDU=4;
    static int KATASTROFA=5;
    static int TANKUJ=0;
    static int REZERWA=500;
    
    int numer;
    int paliwo;
    int stan;
    
    Dworzec d;
    Random rand;
    
    public Pociag(int numer, int paliwo, Dworzec d)
    {
        this.numer=numer;
        this.paliwo=paliwo;
        this.stan=DWORZEC;
        this.d=d;
        rand=new Random();
    }
    
    public void run()
    {
        while(true)
        {
            if(stan==DWORZEC)
            {
                if(rand.nextInt(2)==1)
                {
                    stan=START;
                    paliwo=TANKUJ;
                    System.out.println("Czekam na odjazd, pociąg "+numer);
                    stan=d.start(numer);
                }
                else
                {
                    System.out.println("Postoje sobie jeszcze troche");
                }
            }
            else if(stan==START)
            {
                System.out.println("Wyruszyłem, pociąg "+numer);
                stan=PRZEJAZD;
            }
            else if(stan==PRZEJAZD)
            {
                paliwo-=rand.nextInt(500);
                System.out.println("Pociąg "+numer+" w trasie");
                if(paliwo<=REZERWA){
                stan=KONIEC_PRZEJAZDU;
                }
                else try{
                sleep(rand.nextInt(1000));
                }
                catch (Exception e){}
            }
            else if(stan==KONIEC_PRZEJAZDU)
            {
                System.out.println("Zbliżam się do dworca "+numer+" ilosc paliwa "+paliwo);
                stan=d.laduj();
                if(stan==KONIEC_PRZEJAZDU)
                {
                    paliwo-=rand.nextInt(500);
                    System.out.println("REZERWA "+paliwo);
                    if(paliwo<=0) {stan=KATASTROFA;}
                }
            }
            else if(stan==KATASTROFA)
            {
                System.out.println("BRAK PALIWA pociąg "+numer);
                d.zmniejsz();
            }
        }
    } 
}

 class Dworzec 
{
    static int DWORZEC=1;
    static int START=2;
    static int PRZEJAZD=3;
    static int KONIEC_PRZEJAZDU=4;
    static int KATASTROFA=5;
    
    int ilosc_peronow;
    int ilosc_zajetych;
    int ilosc_pociagow;
    
    public Dworzec(int ilosc_peronow,int ilosc_pociagow)
    {
        this.ilosc_peronow=ilosc_peronow;
        this.ilosc_pociagow=ilosc_pociagow;
        this.ilosc_zajetych=0;
    }
    
    synchronized int start(int numer)
    {
        ilosc_zajetych--;
        System.out.println("Pozwolenie na wyjazd "+numer);
        return START;
    }
    
    synchronized int laduj()
    {
        try
        {
            Thread.currentThread().sleep(1000);
        }
        catch(Exception ie)
        {
        }
        if(ilosc_zajetych<ilosc_peronow)
        {
            ilosc_zajetych++;
            System.out.println("Pozwolenie na przyjazd "+ilosc_zajetych);
            return DWORZEC;
        }
        else
        {return KONIEC_PRZEJAZDU;}
    }
    synchronized void zmniejsz()
    {
        ilosc_pociagow--;
        System.out.println("ZABILEM");
        if(ilosc_pociagow==ilosc_peronow) System.out.println("Ilosc pociągów taka jak ilość peronów");
    }
}

public class Start {
    static int ilosc_pociagow=10;
    static int ilosc_peronow=5;
    static Dworzec dworzec;
   
    public static void main(String[] args) {
        dworzec = new Dworzec(ilosc_peronow, ilosc_pociagow);
        for(int i=0;i<ilosc_pociagow;i++)
        new Pociag(i+1,2000,dworzec).start();

    }
    
}
