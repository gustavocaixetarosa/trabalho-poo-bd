package dev.gustavorosa.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Formatador {
    public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatarData(LocalDate data) {
        return data != null ? data.format(FORMATO_DATA) : "N/A";
    }

    public static String formatarMoeda(Double valor) {
        return valor != null ? String.format("R$ %.2f", valor) : "R$ 0.00";
    }

    public static String truncarTexto(String texto, int tamanhoMaximo) {
        if (texto == null) return "N/A";
        if (texto.length() <= tamanhoMaximo) return texto;
        return texto.substring(0, tamanhoMaximo - 3) + "...";
    }
}

