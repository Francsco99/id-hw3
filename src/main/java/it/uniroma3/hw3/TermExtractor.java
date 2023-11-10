package it.uniroma3.hw3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
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
import java.util.*;

public class TermExtractor {
    final static String TABLE_PATH = System.getProperty("user.dir")+"/tables.json";
final int INTERVALLO_ESTRAZIONE = 1000;
    private List<String> randomExtractor(String jsonPath) {
        PrintColored printer = new PrintColored();
        printer.printColored("Inizio estrazione valori dalle tabelle","red");
        long startTime = System.currentTimeMillis();
        printer.printColored("******************************************************************","red");
        List<String> terminiEstratti = new ArrayList<>();
        int j=0;
        try {
            /*LETTURA DELLE TABELLE*/
            BufferedReader reader = new BufferedReader(new FileReader(jsonPath));   // Apre un file json contenente tabelle
            String line;    // riga corrente del file json

            while ((line = reader.readLine()) != null) {
                /*ESTRAZIONE METADATI DELLA TABELLA CORRENTE*/
                JsonObject jsonTable = JsonParser.parseString(line).getAsJsonObject();  // Analizza la riga corrente come oggetto JSON
                int maxCols = jsonTable.getAsJsonObject("maxDimensions").get("column").getAsInt(); // Ottiene il numero massimo di colonne
                JsonArray cells = jsonTable.getAsJsonArray("cells"); // Ottiene l'array di celle della riga corrente

                /*ITERA SU TUTTE LE CELLE DELLA TABELLA CORRENTE*/
                for (int i = 0; i < cells.size(); i++) {
                    JsonObject cell = cells.get(i).getAsJsonObject();   // Estrai la cella corrente come oggetto JSON
                    JsonObject coordinates = cell.getAsJsonObject("Coordinates"); // Estrai le coordinate della cella corrente
                    int row = coordinates.get("row").getAsInt();    // Estrae la coordinata della riga
                    String cleanedText = cell.get("cleanedText").getAsString(); // Estrae il contenuto della cella

                    if (row != 0) {
                        int randomNumber = new Random().nextInt(INTERVALLO_ESTRAZIONE)+1;
                        if(randomNumber==1 && !cleanedText.equals("")){
                            terminiEstratti.add(cleanedText);
                        }
                    }

                }
                j++;
                if(j%100000==0){
                    System.out.println(j);
                }
            }
            reader.close(); // Chiudi file JSON
            printer.printColored("Fine estrazione valori dalle tabelle","red");
            long currentTime = System.currentTimeMillis();
            long totalTimeMinutes = (currentTime-startTime)/60000;
            printer.printColored("Tempo per estrarre i valori: "+totalTimeMinutes + " minuti","green");
            printer.printColored("******************************************************************","red");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return terminiEstratti;
    }

    public List<Set<String>> randomSetBuilder(){
        List<String> inputList = randomExtractor(TABLE_PATH);
        List<Set<String>> randomSets = new ArrayList<>();
        int setSize = 20;

        // Creazione di un oggetto Random per ottenere indici casuali dalla lista
        Random random = new Random();

        while (!inputList.isEmpty()) {
            Set<String> currentSet = new HashSet<>();

            // Creazione di un set con 20 elementi casuali dalla lista
            for (int i = 0; i < setSize && !inputList.isEmpty(); i++) {
                int randomIndex = random.nextInt(inputList.size());
                currentSet.add(inputList.remove(randomIndex));
            }

            // Aggiunta del set alla lista risultante
            randomSets.add(currentSet);
        }

        return randomSets;
    }
}