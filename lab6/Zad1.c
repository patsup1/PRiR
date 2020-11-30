#include <stdio.h>
#include <math.h>
#include "mpi.h"

int main(int argc, char **argv)
{
    const int TAG = 62;

    int rank;
    int size;               
    MPI_Init(&argc, &argv); 
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    float odebrana_wartosc = 0;
    if(rank != 0)
    {
        MPI_Status status;
        int nadawca = rank-1;
        MPI_Recv(&odebrana_wartosc, 1, MPI_FLOAT, nadawca, TAG, MPI_COMM_WORLD, &status);
        printf("Proces %d odebral wartosc: %f\n", rank, odebrana_wartosc);
    }

    float aktualna_wartosc = 4 * powf(-1, rank) / (2.0f * (rank+1) -1 );
    aktualna_wartosc += odebrana_wartosc;

    if(rank != size-1)
    {
        int adresat = rank + 1;
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
