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
import java.util.HashMap;
import java.util.Map;

public class TableIndexer {
    final static int PRINT_INTERVAL = 100000; // costante per scegliere ogni quanto stampare il messaggio di avanzamento

    public void tableIndexer(String jsonPath, String indexPath) {
        try {
            /*VARIABILI PER STATISTICHE*/
            int tableCount = 0; // indica la tabella che sto processando
            long startIndexingTime = System.currentTimeMillis(); // istante di inizio del processamento tabelle

            /*LETTURA DELLE TABELLE*/
            BufferedReader reader = new BufferedReader(new FileReader(jsonPath));   // Apre un file json contenente tabelle
            String line;    // riga corrente del file json

            /*LUCENE SETUP*/
            Directory indexDirectory = FSDirectory.open(Paths.get(indexPath));  // Directory Lucene per indice
            IndexWriterConfig indexConfig = new IndexWriterConfig(new StandardAnalyzer());  // Writer per scrivere sull'indice
            IndexWriter writer = new IndexWriter(indexDirectory, indexConfig);
            writer.deleteAll(); // Pulisco il vecchio indice

            /*ITERA SU TUTTE LE RIGHE DEL FILE "tables.json"*/
            while ((line = reader.readLine()) != null) {
                /*ESTRAZIONE METADATI DELLA TABELLA CORRENTE*/
                JsonObject jsonTable = JsonParser.parseString(line).getAsJsonObject();  // Analizza la riga corrente come oggetto JSON
                String tableId = jsonTable.get("id").getAsString(); // Ottiene l'ID della tabella
                int maxCols = jsonTable.getAsJsonObject("maxDimensions").get("column").getAsInt(); // Ottiene il numero massimo di colonne
                JsonArray cells = jsonTable.getAsJsonArray("cells"); // Ottiene l'array di celle della riga corrente
                String[] nomiColonne = new String[maxCols + 1]; // Array con i nomi delle colonne della tabella corrente

                /*MAPPA CHE HA COME CHIAVE IL NOME DI UNA COLONNA E
                 * COME VALORE TUTTI I CONTENUTI DELLE CELLE ASSOCIATE*/
                Map<String, StringBuilder> columnData = new HashMap<>();

                /*ITERA SU TUTTE LE CELLE DELLA TABELLA CORRENTE*/
                for (int i = 0; i < cells.size(); i++) {
                    JsonObject cell = cells.get(i).getAsJsonObject();   // Estrai la cella corrente come oggetto JSON
                    JsonObject coordinates = cell.getAsJsonObject("Coordinates"); // Estrai le coordinate della cella corrente
                    int col = coordinates.get("column").getAsInt(); // Estrae la coordinata della colonna
                    int row = coordinates.get("row").getAsInt();    // Estrae la coordinata della riga
                    String cleanedText = cell.get("cleanedText").getAsString(); // Estrae il contenuto della cella

                    /*SE TI TROVI SULLA RIGA 0, ALLORA VUOL DIRE CHE IL CONTENUTO
                    DELLA CELLA CORRENTE È L'INTESTAZIONE DELLA COLONNA, QUINDI
                    SALVALO NELL'ARRAY DEI NOMI DELLE COLONNE
                     */
                    if (row == 0) {
                        nomiColonne[col] = (cleanedText);
                    }
                    /*ALTRIMENTI SALVA IL CONTENUTO DELLA COLONNA NELLA MAPPA
                    USANDO COME CHIAVE IL NOME DELLA COLONNA
                     */
                    else {
                        String columnName = nomiColonne[col];
                        // Aggiungi il valore di cleanedText alla colonna corrispondente
                        columnData.computeIfAbsent(columnName, k -> new StringBuilder()).append(cleanedText).append(",");
                    }
                }
                /*STAMPA DELLO STATO DI AVANZAMENTO*/
                if (tableCount % PRINT_INTERVAL == 0) {
                    System.out.println("Processate  " + tableCount + " tabelle");
                }

                /*STAMPA DELLA MAPPA DELLA TABELLA CORRENTE
                for (Map.Entry<String, StringBuilder> entry : columnData.entrySet()) {
                    String col = entry.getKey();
                    String aggregatedText = entry.getValue().toString().trim();
                    System.out.println("Colonna " + col + ": " + aggregatedText);
                }*/

                /*AGGIUNTA DELLA TABELLA CORRENTE ALL'INDICE LUCENE*/
                Document doc = new Document();  // Creazione di un documento
                doc.add(new TextField("id", tableId, Field.Store.YES));   // Aggiungi l'id della tabella nel campo id

                /*SCORRI LA MAPPA E AGGIUNGI TUTTE LE COLONNE*/
                for (Map.Entry<String, StringBuilder> entry : columnData.entrySet()) {
                    if (entry.getKey() != null) {
                        doc.add(new TextField(entry.getKey(), (entry.getValue().toString()), Field.Store.YES));
                    } else {
                        doc.add(new TextField(" ", (entry.getValue().toString()), Field.Store.YES));
                    }
                }
                writer.addDocument(doc);    // Aggiungi il documento

                tableCount++; // Incrementa il contatore delle tabelle
            }
            reader.close(); // Chiudi file JSON
            writer.commit(); // Commit del documento
            writer.close(); // Chiudi l'indice

            long endIndexingTime = System.currentTimeMillis();  // Istante di fine del processamento del file

            System.out.println("Finita indicizzazione in " + (endIndexingTime - startIndexingTime) / 60000 + " minuti");    // Stampa in minuti del tempo di processamento

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

