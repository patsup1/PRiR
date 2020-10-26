/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package watki;
import java.util.Scanner;


class Simpson extends Thread
{
    double a, b, wynik;

    public Simpson(double a, double b) 
    {
        this.a = a;
        this.b = b;
    }

    double f(double x) 
    {
        return Math.sqrt((Math.pow(x,2)+0.4)/(1.2+Math.sqrt(0.7*Math.pow(x,2)+1.3)));
    }

    public void run()
    {

        double tmp1 = (b - a) / 10;
        double h = (b - (b - tmp1)) / 2;
        double t = 0, a1 = a, a2 = a, tmp, tmp2;
        for(int i = 0; i < 10; i++)
        {
            tmp = a2 + h;
            t += 4 * f(tmp);
            a2 += tmp1;
        }
        for(int i = 1; i < 10; i++)
        {
            tmp2 = a1 + tmp1;
            wynik += 2 * f(tmp2);
            a1 += tmp1;    
        }
        wynik = (h / 3) * (f(a) + f(b) + t + wynik);
    }
}

class Prostokaty extends Thread
{
    double a, b, wynik;
    
    public Prostokaty(double a, double b)
    {
        this.a = a;
        this.b = b;
    }

    double f(double x) 
    {
        return Math.sqrt((Math.pow(x,2)+0.4)/(1.2+Math.sqrt(0.7*Math.pow(x,2)+1.3)));
    }

    public void run()
    {
        double h = (b - a) / 10;
        for(int i = 1; i <= 10; i++)
        {
            wynik += f(a + i * h);
        }
        wynik *= h;
    }
}

class Trapezy extends Thread
{
   double a, b, wynik;
   
   public Trapezy(double a, double b)
   {
        this.a = a;
        this.b = b;
   }
    double f(double x) 
    {
        return Math.sqrt((Math.pow(x,2)+0.4)/(1.2+Math.sqrt(0.7*Math.pow(x,2)+1.3)));
    }
    public void run()
    {
        double h = (b - a) / 10.0;
        for(int i = 1; i < 10; i++) 
        {
            wynik += f(a+(i*h))*h;
        }
        
        wynik += f(a)/2*h + f(b)/2*h;
    } 
}


public class Watki {

   
    public static void main(String[] args) throws InterruptedException 
    {
     double a = 0.3, b = 1.5, wynik = 0, xp, xk;
        Scanner wybor = new Scanner(System.in);
        double roz = b - a;

        System.out.println("1. Metoda trapezów 2. Metoda Simpsona 3. Metoda prostokątów");
        int numer_metody = wybor.nextInt();

        System.out.println("Podaj n (ilosc watkow)");
        double watki = wybor.nextInt();
        
        if(numer_metody==1){
            for(int i = 0; i < watki; i++){
                xp = a + roz/watki * i;
                xk = a + roz/watki * (i+1);
                Trapezy watek = new Trapezy(xp, xk);
                watek.start();
                watek.join();
                wynik += watek.wynik;
                }
        }
        if(numer_metody==2){
            for(int i = 0; i < watki; i++){
                xp = a + roz/watki * i;
                xk = a + roz/watki * (i+1);
                Simpson watek = new Simpson(xp, xk);
                watek.start();
                watek.join();
                wynik += watek.wynik;
                }
        }
        if(numer_metody==3){
            for(int i = 0; i < watki; i++){
                xp = a + roz/watki * i;
                xk = a + roz/watki * (i+1);
                Prostokaty watek = new Prostokaty(xp, xk);
                watek.start();
                watek.join();
                wynik += watek.wynik;
                }
            }
        System.out.println(wynik);   
    }
    
}
