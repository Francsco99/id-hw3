package it.uniroma3.hw3;



import java.util.*;

public class Merge {
    PrintColored printer = new PrintColored();
    public void mergeList(Map<String, Set<String[]>> mappaSearcher) {
        Map<String, Object[]> set2Count = new HashMap<>(); // crea una nuova mappa per il conteggio(chiave: nome colonna e valore:Set tupla occorrenza, idtable
        /* ITERA SU OGNI CHIAVE DELLA MAPPA */
        for (String chiave : mappaSearcher.keySet()) {
            Set<String[]> setValori = mappaSearcher.get(chiave);

            for (String[] valore : setValori) {
                String colonna = valore[0];
                String id = valore[1];

                if (set2Count.containsKey(colonna)) {
                    int conteggio = (int) set2Count.get(colonna)[0]; // Conteggio della colonna
                    Object[] tupla = new Object[]{conteggio + 1, id};
                    set2Count.put(colonna, tupla);

                } else {
                    Object[] tupla = new Object[]{1, id};
                    set2Count.put(colonna, tupla);
                }

            }

        }
        /* ORDINAMENTO PER VALORI DECRESCENTI DI CONTEGGIO */

        // Creo una LinkedHashMap vuota per mantenere le voci ordinate secondo la voce conteggio
        LinkedHashMap<String, Object[]> sortedMap = new LinkedHashMap<>();

        // Creare una lista temporanea di voci della mappa
        List<Map.Entry<String, Object[]>> entryList = new ArrayList<>(set2Count.entrySet());

        // Ordina la lista in base al conteggio in ordine decrescente
        Collections.sort(entryList, (entry1, entry2) -> {
            int conteggio1 = (int) entry1.getValue()[0];
            int conteggio2 = (int) entry2.getValue()[0];
            return Integer.compare(conteggio2, conteggio1);
        });

        // Riempio la LinkedHashMap ordinata con le voci
        for (Map.Entry<String, Object[]> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        // stampa della mappa ordinata
        System.out.println("\n");
        printer.printColored("Mappa set2count","green");
        for (Map.Entry<String, Object[]> entry : sortedMap.entrySet()) {
            String colonna = entry.getKey();
            Object[] tupla = entry.getValue();
            int conteggio = (int) tupla[0];
            String id = (String) tupla[1];
            System.out.println("Colonna: " + colonna + ", Conteggio: " + conteggio);
        }
    }
}
