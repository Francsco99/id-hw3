package it.uniroma3.hw3;

import java.util.*;

public class Merge {
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
                    set2Count.put(valore, conteggio+1);
                } else {
                    // la stringa non è presente, quindi aggiungila con valore 1
                    set2Count.put(valore, 1);
                }
            }

        }

        // Estrai i valori dalla mappa e memorizzali in una lista in modo da ordinare la mappa
        List<Map.Entry<String, Integer>> listaValoriDisordinata = new ArrayList<>(set2Count.entrySet());

        // Ordina la lista dei valori in ordine decrescente
        listaValoriDisordinata.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Crea una nuova mappa ordinata
        Map<String, Integer> set2CountOrdinata = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : listaValoriDisordinata) {
            set2CountOrdinata.put(entry.getKey(), entry.getValue());
        }

        // Stampa la mappa set2CountOrdinata
        System.out.println("\n\nMappa del conteggio set2Count ordinata in modo decrescente:");
        System.out.println("\n");
        for (Map.Entry<String, Integer> entry : set2CountOrdinata.entrySet()) {
            System.out.println("Colonna: " + entry.getKey() + ", Occorrenza: " + entry.getValue());
        }
    }
}
