package it.uniroma3.hw3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex {
    public Map<String, List<String>> inverted(String jsonPath, String indexPath) {
        TableIndexer table = new TableIndexer();
        Map<String, StringBuilder> columnData = table.tableIndexer(jsonPath, indexPath);
        Map<String, List<String>> invertedIndex = new HashMap<>();

        /* ITERA SU TUTTE LE COLONNE NELLA MAPPA columnData */
        for (Map.Entry<String, StringBuilder> entry : columnData.entrySet()) {
            String col = entry.getKey();
            String aggregatedText = entry.getValue().toString().trim();
            String[] values = aggregatedText.split(",");

            for (String value : values) {
                if (!value.isEmpty()) {
                    /* Verifica se l'elemento è già presente nell'indice invertito */
                    if (!invertedIndex.containsKey(value)) {
                        invertedIndex.put(value, new ArrayList<>());
                    }

                    /* Aggiungi il nome della colonna all'elenco delle colonne che contengono l'elemento */
                    invertedIndex.get(value).add(col);
                }
            }
        }
        return invertedIndex;
    }
}
