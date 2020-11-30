#include <stdio.h>
#include <math.h>
#include "mpi.h"

float f(float x)
{
    return x*x;
}

float Trapez(int a, int b, int rank, int size)
{
    float dx = (b-a) / (float)size;
    if(rank == size-1)
    {
        return f(b) / 2 * dx;        
    }
    else if (rank == 0)
    {
        return f(a) / 2 * dx;
    }
    else
    {
        return f(a + dx * rank) * dx;
    }
    
}

int main(int argc, char **argv)
{
    const int TAG = 62;
    const int a = 0;
    const int b = 10;

    int rank;
    int size;               
    MPI_Init(&argc, &argv); 
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    float odebrana_wartosc = 0;
    if(rank != size-1)
    {
        MPI_Status status;
        int nadawca = rank+1;
        MPI_Recv(&odebrana_wartosc, 1, MPI_FLOAT, nadawca, TAG, MPI_COMM_WORLD, &status);
        printf("Proces %d odebral wartosc: %f\n", rank, odebrana_wartosc);
    }

    float aktualna_wartosc = Trapez(a, b, rank, size);
    aktualna_wartosc += odebrana_wartosc;

    if(rank != 0)
    {
        int adresat = rank - 1;
        MPI_Send(&aktualna_wartosc, 1, MPI_FLOAT, adresat, TAG, MPI_COMM_WORLD);
        printf("Proces %d wyslal wartosc: %f\n", rank, aktualna_wartosc);
    }
    else
    {
        printf("Wyliczona wartosc to: %f\n", aktualna_wartosc);
    }


    MPI_Finalize();
    return 0;
}
