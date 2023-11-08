package it.uniroma3.hw3;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        // Stampa la mappa set2Count
        System.out.println("Mappa del conteggio set2Count");
        for (Map.Entry<String, Integer> entry : set2Count.entrySet()) {
            System.out.println("Chiave: " + entry.getKey() + ", Valore: " + entry.getValue());
        }
    }
}
