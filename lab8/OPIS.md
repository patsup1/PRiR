W main zaczynamy od deklaracji naszego pola do gry oraz potrzebnych zmiennych

        int wzor_siatki[256] =
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    int suma_procesow;
    int suma_iteracji;
    int ID; 
    int j;
    int iteracje = 0;
    
W zależności od parametru argc przypisujemy wartość zmiennej suma_iteracji

        if (argc == 1)
        {
                suma_iteracji = DOMYSLNE_ITERACJE;
        }
        else if (argc == 2)
        {
                suma_iteracji = atoi(argv[1]);
        }
        else
        {
                exit(1);
        }

Podstawowe funckje MPI

    MPI_Init(&argc, &argv);
    MPI_Status status;
    MPI_Comm_size(MPI_COMM_WORLD, &suma_procesow);
    MPI_Comm_rank(MPI_COMM_WORLD, &ID);
    
Cały program działa w pętli, która wukonuje się tyle ile wynosi suma_iteracji

        for (iteracje = 0; iteracje < suma_iteracji; iteracje++)
        
W następnej pętli do stworzonej przez nas tablicy wpisujemy wartość z wcześniej zadeklarowanej tablicy wzor_siatki

        j = WYMIAR;
        for (int i=ID*(SIATKA/suma_procesow); i<(ID+1)*(SIATKA/suma_procesow); i++)
        {
            tablica[j]=wzor_siatki[i];
            j++;
        }

Jeśli suma_procesow jest różna od 1 za pomocą MPI wysyłamy i odbieramy wartości z wcześniej utworzonej tablicy

         if (suma_procesow != 1)
        {
            int odbior1[WYMIAR];
            int odbior2[WYMIAR];
            int wyslij1[WYMIAR];
            int wyslij2[WYMIAR];
            if (ID%2 == 0)
            {
                for (int i=0; i<WYMIAR; i++)
                {
                    wyslij1[i] = tablica[i + WYMIAR];
                }
                MPI_Send(&wyslij1, WYMIAR, MPI_INT, modulo(ID - 1, suma_procesow), 1, MPI_COMM_WORLD);

                for (int i = 0; i < WYMIAR; i++)
                {
                    wyslij2[i] = tablica[(WYMIAR * (WYMIAR / suma_procesow)) + i];
                }
                MPI_Send(&wyslij2, WYMIAR, MPI_INT, modulo(ID + 1, suma_procesow), 1, MPI_COMM_WORLD);
            }
            else
            {
                MPI_Recv(&odbior2, WYMIAR, MPI_INT, modulo(ID + 1, suma_procesow), 1, MPI_COMM_WORLD, &status);
                MPI_Recv(&odbior1, WYMIAR, MPI_INT, modulo(ID - 1, suma_procesow), 1, MPI_COMM_WORLD, &status);
            }
            if (ID%2==0)
            {
                MPI_Recv(&odbior2, WYMIAR, MPI_INT, modulo(ID + 1, suma_procesow), 1, MPI_COMM_WORLD, &status);
                MPI_Recv(&odbior1, WYMIAR, MPI_INT, modulo(ID - 1, suma_procesow), 1, MPI_COMM_WORLD, &status);
            }
            
W przeciwnym wypadku do tablicy wpisujemy wartość z cześniej utworzonego przez nas wzoru z tablicy wzor_siatki

        else
        {
            for (int i=0; i<WYMIAR; i++)
            {
                tablica[i + SIATKA + WYMIAR] = wzor_siatki[i];
                //printf(" %d %d \n",i + SIATKA+WYMIAR,i);
            }
            for (int i = SIATKA; i < SIATKA + WYMIAR; i++)
            {
                tablica[i - SIATKA] = wzor_siatki[i - WYMIAR];
                //printf(" %d %d \n",i - SIATKA,i-WYMIAR);
            }
        }

W następnej pętli uzupełniamy tablicę wynik w zależności jaką wartośc napotkamy w zmiennej policz

        for (int k=WYMIAR; k<WYMIAR*((WYMIAR/suma_procesow)+1); k++)
        {
            int wszystkie_wiersze = WYMIAR * (WYMIAR / suma_procesow) + 2;
            int wiersz = k / WYMIAR;
            int kolumna = k % WYMIAR;
            int poprzedni_wiersz = modulo(wiersz - 1, wszystkie_wiersze);
            int poprzednia_kolumna = modulo(kolumna - 1, WYMIAR);
            int nastepny_wiersz = modulo(wiersz + 1, wszystkie_wiersze);
            int nastepna_kolumna = modulo(kolumna + 1, WYMIAR);

            int policz = tablica[poprzedni_wiersz * WYMIAR + poprzednia_kolumna] + tablica[poprzedni_wiersz * WYMIAR + kolumna] + tablica[poprzedni_wiersz * WYMIAR +                         nastepna_kolumna] + tablica[wiersz * WYMIAR + poprzednia_kolumna] + tablica[wiersz * WYMIAR + nastepna_kolumna] + tablica[nastepny_wiersz * WYMIAR +                             poprzednia_kolumna] + tablica[nastepny_wiersz * WYMIAR + kolumna] + tablica[nastepny_wiersz * WYMIAR + nastepna_kolumna];
            if (tablica[k] == 1)
            {
                if (policz < 2)
                    wynik[k - WYMIAR] = 0;
                else if (policz > 3)
                    wynik[k - WYMIAR] = 0;
                else
                    wynik[k - WYMIAR] = 1;
            }
            else
            {
                if (policz == 3)
                    wynik[k - WYMIAR] = 1;
                else
                    wynik[k - WYMIAR] = 0;
            }
        }

Na koniec do naszej tablicy wzor_siatki przypisujemy odpowiednią wartość z tablicy wynik

         for (int i=ID*(SIATKA/suma_procesow); i<(ID+1)*(SIATKA/suma_procesow); i++)
        {
            wzor_siatki[i] = wynik[j];
            j++;
        }
        
Z każdą iteracją wypisujemy siatkę gry na ekran

        if (ID == 0)
        {
            printf("\nIteracja %d: Siatka:\n", iteracje);
            for (j = 0; j < SIATKA; j++)
            {
                if (j % WYMIAR == 0)
                {
                    printf("\n");
                }
                printf("%d  ", wzor_siatki[j]);
            }
            printf("\n");
        }


