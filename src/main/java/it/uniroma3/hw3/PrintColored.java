package it.uniroma3.hw3;

public class PrintColored {
    // Funzione di utilità per stampare con il colore specificato
    public  void printColored(String message, String color) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";

        String colorCode;
        switch (color.toLowerCase()) {
            case "green":
                colorCode = ANSI_GREEN;
                break;
            default:
                colorCode = "";
        }

        System.out.println(colorCode + message + ANSI_RESET);
    }

}
