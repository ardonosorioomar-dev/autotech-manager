package com.autotech.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formateador {
    private static final DecimalFormat FORMATO_MONEDA;
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMATO_FECHA_CORTA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "HN"));
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        FORMATO_MONEDA = new DecimalFormat("L #,##0.00", symbols);
    }

    public static String lempiras(double monto) {
        return FORMATO_MONEDA.format(monto);
    }

    public static String fechaHora(LocalDateTime fecha) {
        return fecha != null ? fecha.format(FORMATO_FECHA_HORA) : "N/A";
    }

    public static String fechaCorta(LocalDateTime fecha) {
        return fecha != null ? fecha.format(FORMATO_FECHA_CORTA) : "N/A";
    }
}
