package com.point.sale.util;

import java.text.DecimalFormat;
import java.text.ParseException;

public class Util {


    // Método para convertir un double en formato de moneda
    public static String doubleAMoneda(double numero) {
        DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00");

        String formato =  formatoMoneda.format(numero).replaceAll("\\$","").replace(",",".");
        System.out.println(formato);
        return formato;
    }

    // Método para convertir una cadena de texto en formato de moneda a double
    public static double monedaADouble(String cantidad) {
        try {
            cantidad.replace(".",",");
            cantidad = "$"+cantidad;
            DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00");
            return formatoMoneda.parse(cantidad).doubleValue();
        } catch (ParseException e) {
            // Manejar cualquier error de formato aquí
            e.printStackTrace();
            return 0.0;
        }
    }
}
