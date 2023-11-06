package it.uniroma3.hw3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableReader {

    public void tableIndexer(String jsonPath,String indexPath) {
        try {
            // Apre un file JSON contenente tabelle
            BufferedReader reader = new BufferedReader(new FileReader(jsonPath));
            String line;
            //int j = 0; // Contatore per le tabelle

            // Directory Lucene per indice
            Directory indexDirectory = FSDirectory.open(Paths.get(indexPath));
            // Writer per scrivere sull'indice
            IndexWriterConfig indexConfig = new IndexWriterConfig(new StandardAnalyzer());
            IndexWriter writer = new IndexWriter(indexDirectory,indexConfig);
            // Pulisco il vecchio indice
            writer.deleteAll();

            while ((line = reader.readLine()) != null) {
                // Analizza la riga corrente come oggetto JSON
                JsonObject jsonTable = JsonParser.parseString(line).getAsJsonObject();
                String tableId = jsonTable.get("id").getAsString(); // Ottiene l'ID della tabella
                JsonObject maxDimensions = jsonTable.getAsJsonObject("maxDimensions");
                int maxRows = maxDimensions.get("row").getAsInt(); // Ottiene il numero massimo di righe
                int maxCols = maxDimensions.get("column").getAsInt(); // Ottiene il numero massimo di colonne
                JsonArray cells = jsonTable.getAsJsonArray("cells"); // Ottiene l'array di celle

                //Stampa l'id della tabella corrente
                //System.out.println("Tabella " + j + " " + tableId + " " + maxRows + " " + maxCols);
                //j++;

                // Crea una mappa per memorizzare i valori "cleanedText" per ciascuna colonna
                Map<Integer, List<String>> columnValues = new HashMap<>();

                // Riempie la mappa con i valori "cleanedText" per ciascuna colonna
                for (int i = 0; i < cells.size(); i++) {
                    JsonObject cell = cells.get(i).getAsJsonObject();
                    JsonObject coordinates = cell.getAsJsonObject("Coordinates");
                    int col = coordinates.get("column").getAsInt();
                    String cleanedText = cell.get("cleanedText").getAsString();
                    // Aggiunge il valore alla colonna corrispondente
                    if (!columnValues.containsKey(col)) {
                        columnValues.put(col, new ArrayList<>());
                    }
                    columnValues.get(col).add(cleanedText);
                }
                // Nuovo documento Lucene per l'indice
                Document doc = new Document();
                // Aggiunge il campo id
                doc.add(new TextField("id",tableId,Field.Store.YES));
                // Itera sulle colonne della mappa per salvare il contenuto sull'indice
                for (int c = 0; c <= maxCols; c++) {
                    if (columnValues.containsKey(c)) {
                        List<String> columnData = columnValues.get(c);
                        String columnDataString = String.join(", ",columnData);
                        // Aggiunge il campo colonna con il relativo contenuto della colonna
                        doc.add(new TextField("column"+c,columnDataString,Field.Store.YES));
                        //System.out.println("Column " + c + ": " + String.join(", ", columnData));
                    }
                }
                // Scrivi sull'indice
                writer.addDocument(doc);
                writer.commit();
            }
            // Chiudi l'indice
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



