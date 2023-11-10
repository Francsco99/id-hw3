package it.uniroma3.hw3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ProbabilitaCattura {
    Random random = new Random();
    public void probabilita(FileWriter writer, String columnName) throws IOException {
        int number = random.nextInt(1000000) + 1;
        // Se il numero casuale è uguale a 1, chiama la funzione per scrivere nel file CSV
        if (number == 1) {
            writer.append(columnName + "\n");
            System.out.println("Catturata cella");
        }
    }
}
