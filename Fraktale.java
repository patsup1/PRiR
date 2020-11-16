/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fraktale;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;


public class Fraktale extends Thread {

    final static int N = 4096;
    final static int CUTOFF = 100;
    static int[][] set = new int[N][N];
    
    int me;
    public Fraktale(int me) 
    {
        this.me = me;
    }
    
    public void run() 
    {
        int begin = 0, end = 0;
        if (me == 0)
        {
            begin = 0;
            end = (N / 4) * 1;
        }   
        else if (me == 1) 
        {
            begin = (N / 4) * 1;
            end = (N / 4) * 2;
        }
        else if (me == 2) 
        {
            begin = (N / 4) * 2;
            end = (N / 4) * 3;
        }
        else if (me == 3)
        {
            begin = (N / 4) * 3;
            end = N;
        }
        for (int i = begin; i < end; i++)
        {
            for (int j = 0; j < N; j++)
            {
                double cr = 1.5 * (i - N / 2) / (0.5 * N);
                double ci = (j - N / 2) / (0.5 * N);
                double zr = cr, zi = ci;
                int k = 0;
                while (k < CUTOFF && zr * zr + zi * zi < 4.0) 
                {
                    double newr = cr + zr * zr - zi * zi;
                    double newi = ci + 2 * zr * zi;
                    zr = newr;
                    zi = newi;
                    k++;
                }
                set[i][j] = k;
            }
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        long startTime = System.currentTimeMillis();
        
        Fraktale thread0 = new Fraktale(0);
        Fraktale thread1 = new Fraktale(1);
        Fraktale thread2 = new Fraktale(2);
        Fraktale thread3 = new Fraktale(3);
        
        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
        
        thread0.join();
        thread1.join();
        thread2.join();
        thread3.join();

        long endTime = System.currentTimeMillis();
        System.out.println("Obliczenia zakończone w czasie " + (endTime - startTime) + " millisekund");

        BufferedImage img = new BufferedImage(N, N, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < N; i++) 
        {
            for (int j = 0; j < N; j++) 
            {
                int k = set[i][j];
                float level;
                if (k < CUTOFF) {
                level = (float) k / CUTOFF;
                } else {
                level = 0;
                }
                
                Color c = new Color(0, level, 0); 
                img.setRGB(i, j, c.getRGB());
            }
        }

        ImageIO.write(img, "PNG", new File("FraktaleJulii.png"));
        
    }
    
    
    
}

  
