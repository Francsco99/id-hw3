package it.uniroma3.hw3;

import java.util.*;

public class Merge {
    PrintColored printer = new PrintColored(); // Per stampare i messaggi verdi
    public void mergeList(Map<String, Set<String>> mappaSearcher) {
        Map<String, Integer> set2Count = new HashMap<>();
        /* ITERA SU OGNI CHIAVE DELLA MAPPA */
        for (String chiave : mappaSearcher.keySet()) {

            Set<String> setValori = mappaSearcher.get(chiave); // set di stringhe della chiave corrente
            /* ITERA SU OGNI STRINGA DI setValori */
            for (String valore : setValori) {
                // verifica se la stringa è già presente nella mappa set2Count
                if (set2Count.containsKey(valore)) {
                    // la stringa è già presente perciò incrementa il valore
                    int conteggio = set2Count.get(valore);
                    set2Count.put(valore, conteggio + 1);
                } else {
                    // la stringa non è presente, quindi aggiungila con valore 1
                    set2Count.put(valore, 1);
                }
            }
        }

        /*STAMPA DELLA MAPPA set2count ORDINATA PER VALORI DECRESCENTI*/
        printer.printColored("Mappa del conteggio set2Count (Ordinata per valori crescenti)", "green");
        List<Map.Entry<String, Integer>> sortedSet2count = new ArrayList<>(set2Count.entrySet()); // Converto le entry della mappa in una lista per poterle ordinare
        sortedSet2count.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Ordino la lista in base ai valori decrescenti
        /*ITERO SUGLI ELEMENTI ORDINATI*/
        for (Map.Entry<String, Integer> entry : sortedSet2count) {
            System.out.println("Colonna: " + entry.getKey() + ", Conteggio: " + entry.getValue());
        }
    }
}
