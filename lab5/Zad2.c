#include <stdio.h>
#include <math.h>
#include <stdlib.h>

float Pi(int n)
{
    float wynik = 0;
    for(int i=1; i<=n; i++)
    {
        wynik += powf(-1, i-1) / (2*i-1);
    }
    wynik *= 4;
    return wynik;
}

int main()
{
    printf("Podaj liczbę procesów: ");
    int liczba;
    scanf("%d", &liczba);

    for(int i=0; i<liczba; i++)
    {
        if(fork() == 0)
        {
            srand(time(NULL) ^ getpid()<<16);
            int n = 100 + rand()%4901;
            float wynik = Pi(n);
            printf("Obliczono %f, dla %d wyrazow\n", wynik, n);
            exit(0);
        }
    }
}