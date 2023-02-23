package com.cliProject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        //Inisialisasi variabel
        String logFilePath = "";
        String outputType = "text";
        String outputFilePath = "";

        //Cek user inputan flags
        if (args.length > 0 && args[0].equals("-h")) {
            showHelp();
            return;
        } else {
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    logFilePath = args[0];
                } else if (args[i].equals("-t") && i < args.length - 1) {
                    outputType = args[i + 1];
                } else if (args[i].equals("-o") && i < args.length - 1) {
                    outputFilePath = args[i + 1];
                }
            }
        }

        List<String> lines = readLog(logFilePath);
        if (outputType.equals("json")) {
            lines = convertToJson(lines);
        }

        if (!outputFilePath.isEmpty()) {
            writeOutputToFile(outputFilePath, lines);
        } else {
            printOutputToConsole(lines);
        }
    }

    //Daftar list perintah penggunaan
    public static void showHelp() {
        System.out.println("Cara menggunakan CLI:");
        System.out.println("-t : Memilih output type [text, json], defaultnya text.");
        System.out.println("-o : Memilih lokasi direktori penyimpanan output file,");
        System.out.println("-h : Bantuan.");
    }

    //Membaca data file log (String/plaintext)
    public static List<String> readLog(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    //Convert data log ke json
    public static List<String> convertToJson(List<String> lines) {
        String output = "[";

        //Looping data log setiap baris
        for (String line : lines) {
            String escapedLine = line.replace("\\", "\\\\").replace("\"", "\\\"");
            output += "{\"message\":\"" + escapedLine + "\"},";
        }

        //Hapus koma di akhir data log
        if (output.endsWith(",")) {
            output = output.substring(0, output.length() - 1);
        }
        output += "]";

        List<String> convertedLines = new ArrayList<>();
        convertedLines.add(output);
        return convertedLines;
    }

    public static void writeOutputToFile(String filePath, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);

        //Cek path direktori
        if (!Files.exists(path.getParent())) {
            System.err.println("Direktori output tidak ditemukan.");
            return;
        }

        //Menulis data output file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Gagal menulis data output " + filePath);
            e.printStackTrace();
        }

        System.out.println("Log file telah tersimpan di " + filePath + ".");
    }

    //Cetak Data ke Konsol
    public static void printOutputToConsole(List<String> lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }
}


