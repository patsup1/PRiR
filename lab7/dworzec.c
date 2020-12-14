#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "mpi.h"
#define REZERWA 500
#define DWORZEC 1
#define START 2
#define PRZEJAZD 3
#define KONIEC_PRZEJAZDU 4
#define KATASTROFA 5
#define TANKUJ 5000
int paliwo = 5000;
int POSTOJ=1, BRAK_POSTOJU=0;
int liczba_procesow;
int nr_procesu;
int ilosc_pociagow;
int ilosc_peronow=6;
int ilosc_zajetych_peronow=0;
int tag=1123;
int wyslij[2];
int odbierz[2];
MPI_Status mpi_status;

void Wyslij(int nr_pociagu, int stan) 
{
    wyslij[0]=nr_pociagu;
    wyslij[1]=stan;
    MPI_Send(&wyslij, 2, MPI_INT, 0, tag, MPI_COMM_WORLD);
    sleep(1);
}

void Dworzec(int liczba_procesow)
{
    int nr_pociagu,status;
    ilosc_pociagow = liczba_procesow - 1;
    printf("Witamy w PKP Intercity \n");
    if(rand()%2==1)
    {
        printf("Mamy piekna pogode, Pociagi nie beda mialy opoznien\n");
    }
    else
    {
        printf("Niestety pogoda nie sprzyja. Moga byc opoznienia w kursach\n");
    }
    printf("Zyczymy Panstwu, przyjemnej podrozy \n \n \n");
    printf("Dysponujemy %d peronami\n", ilosc_peronow);
    sleep(2);
    
    while(ilosc_peronow<=ilosc_pociagow)
    {
        MPI_Recv(&odbierz,2,MPI_INT,MPI_ANY_SOURCE,tag,MPI_COMM_WORLD, &mpi_status); 
        nr_pociagu=odbierz[0];
        status=odbierz[1];
        if(status==1)
        {
            printf("Pociag %d stoi na dworcu.\n", nr_pociagu);
        }
        if(status==2)
        {
            printf("Pociag %d pozwolenie na przejazd z peronu nr %d\n", nr_pociagu, ilosc_zajetych_peronow);
            ilosc_zajetych_peronow--;
        }
        if(status==3)
        {
            printf("Pociag %d jedzie do nastepnego dwroca\n", nr_pociagu);
        }
        if(status==4)
        {
            if(ilosc_zajetych_peronow<ilosc_peronow)
            {
                ilosc_zajetych_peronow++;
                MPI_Send(&POSTOJ, 1, MPI_INT, nr_pociagu, tag, MPI_COMM_WORLD);
            }
            else
            {
                MPI_Send(&BRAK_POSTOJU, 1, MPI_INT, nr_pociagu, tag, MPI_COMM_WORLD);
            }
        }
        if(status==5)
        {
            ilosc_pociagow--;
            printf("Ilosc pociagow %d\n", ilosc_pociagow);
        }
    } 
    printf("Koniec programu.)\n");
}

void Pociag()
{
    int stan,suma,i;
    stan=PRZEJAZD; 
    while(1)
    {
        if(stan==1)
        {
            if(rand()%2==1)
            {
                stan=START;
                paliwo=TANKUJ;
                printf("Prosze o pozwolenie na przejazd, pociag %d\n",nr_procesu);
                Wyslij(nr_procesu,stan);
            }
            else
            {
                Wyslij(nr_procesu,stan);
            }
        }
        else if(stan==2)
        {
            printf("Wyruszylem w podroz, pociag %d\n",nr_procesu);
            stan=PRZEJAZD;
            Wyslij(nr_procesu,stan);
        }
        else if(stan==3)
        {
            paliwo-=rand()%500; 
            if(paliwo<=REZERWA)
            {
                stan=KONIEC_PRZEJAZDU;
                printf("Prosze o pozwolenie na wjazd na dworzec\n");
                Wyslij(nr_procesu,stan);
            }
            else
            {
                for(i=0; rand()%10000;i++);
            }
        }
        else if(stan==4)
        {
            int temp;
            MPI_Recv(&temp, 1, MPI_INT, 0, tag, MPI_COMM_WORLD, &mpi_status);
            if(temp==POSTOJ)
            {
                stan=DWORZEC;
                printf("Wjechalem na dworzec, pociag %d\n", nr_procesu);
            }
            else
            {
                paliwo-=rand()%500;
                if(paliwo>0)
                {
                    Wyslij(nr_procesu,stan);
                }
                else
                {
                    stan=KATASTROFA;
                    printf("Pociag sie wykoleil.\n");
                    Wyslij(nr_procesu,stan);
                    return;
                }
            }
        }
    }
}

int main(int argc, char *argv[])
{
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD,&nr_procesu);
    MPI_Comm_size(MPI_COMM_WORLD,&liczba_procesow);
    srand(time(NULL));
    
    if(nr_procesu == 0) 
        Dworzec(liczba_procesow);
    else 
        Pociag();
    MPI_Finalize();
    return 0;
}
