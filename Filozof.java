/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filozof;

import java.util.concurrent.Semaphore;
import java.util.*;



public class Filozof extends Thread 
{
    static final int MAX=5;
    static Semaphore [] widelec = new Semaphore [MAX] ;
    int mojNum;
    static int wybor;
    Random losuj;
    
    public Filozof ( int nr ) 
    {
        mojNum=nr ;
        losuj = new Random(mojNum) ;
    }
    public void run ( ) 
    {
        
        if(wybor==1)
        {
            while ( true ) 
            {
            
                System.out.println ( "Myślę " + mojNum) ;
                try {
                Thread.sleep ( ( long ) (7000 * Math.random( ) ) );
                } catch ( InterruptedException e ) {
                }
                widelec [mojNum].acquireUninterruptibly ( ) ; 
                widelec [ (mojNum+1)%MAX].acquireUninterruptibly ( ); 

                System.out.println ( "Zaczyna jeść "+mojNum) ;
                try {
                Thread.sleep ( ( long ) (5000 * Math.random( ) ) );
                } catch ( InterruptedException e ) {
                }
                System.out.println ( "Kończy jeść "+mojNum);
                widelec [mojNum].release ( ) ; 
                widelec [ (mojNum+1)%MAX].release ( ); 
            }
        }
        
        else if(wybor==2)
        {
            while ( true ) 
            {
                
                System.out.println ( "Mysle ¦ " + mojNum) ;
                try {
                Thread.sleep ( ( long ) (5000 * Math.random( ) ) ) ;
                } catch ( InterruptedException e ) {
                }
                if (mojNum == 0) {
                widelec [ (mojNum+1)%MAX].acquireUninterruptibly ( ) ;
                widelec [mojNum].acquireUninterruptibly ( ) ;
                } else {
                widelec [mojNum].acquireUninterruptibly ( ) ;
                widelec [ (mojNum+1)%MAX].acquireUninterruptibly ( ) ;
                }
                
                System.out.println ( "Zaczyna jesc "+mojNum) ;
                try {
                Thread.sleep ( ( long ) (3000 * Math.random( ) ) ) ;
                } catch ( InterruptedException e ) {
                }
                System.out.println ( "Konczy jesc "+mojNum) ;
                widelec [mojNum].release ( ) ;
                widelec [ (mojNum+1)%MAX].release ( ) ;
            }
        }
        
        else if(wybor==3)
        {
            while ( true )
            {
                // myslenie
                System.out.println ( "Mysle ¦ " + mojNum) ;
                try {
                Thread.sleep ( ( long ) (5000 * Math.random( ) ) ) ;
                } catch ( InterruptedException e ) {
                }
                int strona = losuj.nextInt ( 2 ) ;
                boolean podnioslDwaWidelce = false ;
                do {
                if ( strona == 0) {
                widelec [mojNum].acquireUninterruptibly ( ) ;
                if( ! ( widelec [ (mojNum+1)%MAX].tryAcquire ( ) ) ) {
                widelec[mojNum].release ( ) ;
                } else {
                podnioslDwaWidelce = true ;
                }
                } else {
                widelec[(mojNum+1)%MAX].acquireUninterruptibly ( ) ;
                if ( ! (widelec[mojNum].tryAcquire ( ) ) ) {
                widelec[(mojNum+1)%MAX].release ( ) ;
                } else {
                podnioslDwaWidelce = true ;
                }
                }
                } while ( podnioslDwaWidelce == false ) ;
                System.out.println ( "Zaczyna jesc "+mojNum) ;
                try {
                Thread.sleep ( ( long ) (3000 * Math.random( ) ) ) ;
                } catch ( InterruptedException e ) {
                }
                System.out.println ( "Konczy jesc "+mojNum) ;
                widelec [mojNum].release ( ) ;
                widelec [ (mojNum+1)%MAX].release ( ) ;
            }
        }
        
    }

    public static void main(String[] args) {
        
        Scanner s =  new Scanner(System.in);
        System.out.println("Wybierz wariant. 1, 2 lub 3");
        wybor = s.nextInt();
        
        for ( int i =0; i<MAX; i++) 
        {
            widelec [ i ]=new Semaphore ( 1 ) ;
        }
        for ( int i =0; i<MAX; i++)
        {
            new Filozof(i).start();
        }
    }
    
}

