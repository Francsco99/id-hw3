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

public class TableIndexer {

    public void tableIndexer(String jsonPath, String indexPath) {
        try {
            // Apre un file JSON contenente tabelle
            BufferedReader reader = new BufferedReader(new FileReader(jsonPath));
            String line; // riga corrente del file json
            //int j = 0; // Contatore per le tabelle

            // Directory Lucene per indice
            Directory indexDirectory = FSDirectory.open(Paths.get(indexPath));
            // Writer per scrivere sull'indice
            IndexWriterConfig indexConfig = new IndexWriterConfig(new StandardAnalyzer());
            IndexWriter writer = new IndexWriter(indexDirectory, indexConfig);
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

                for (int col = 0; col <= maxCols; col++) { // per ogni colonna
                    StringBuilder columnDataStringBuilder = new StringBuilder();
                    String columnName = null;

                    for (int i = 0; i < cells.size(); i++) {
                        JsonObject cell = cells.get(i).getAsJsonObject();
                        JsonObject coordinates = cell.getAsJsonObject("Coordinates");
                        int cellCol = coordinates.get("column").getAsInt();

                        if (cellCol == col) {
                            String cleanedText = cell.get("cleanedText").getAsString();
                            if (i == 0) {
                                // Se è la riga 0, conserva il nome della colonna
                                columnName = cleanedText;
                            } else {
                                if (columnDataStringBuilder.length() > 0) {
                                    columnDataStringBuilder.append(", ");
                                }
                                columnDataStringBuilder.append(cleanedText);
                            }
                        }
                    }

                    if (columnDataStringBuilder.length() > 0) {
                        // Nuovo documento Lucene per l'indice
                        Document doc = new Document();
                        doc.add(new TextField("id", tableId, Field.Store.YES));
                        // Aggiunge il campo colonna con il relativo contenuto della colonna
                        doc.add(new TextField("column" + col, columnDataStringBuilder.toString(), Field.Store.YES));
                        // Aggiunge il nome della colonna
                        if (columnName != null) {
                            doc.add(new TextField("column" + col + "_name", columnName, Field.Store.YES));
                        }
                        System.out.println(tableId + "\n");
                        System.out.println(columnName + "\n");
                        System.out.println(columnDataStringBuilder.toString() + "\n\n\n");

                        // Scrivi sull'indice
                        writer.addDocument(doc);
                        writer.commit();
                    }
                }
            }

            // Chiudi file JSON
            reader.close();
            // Chiudi l'indice
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




