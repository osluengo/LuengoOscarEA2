package cl.duoc.pataverde.service;

import cl.duoc.pataverde.model.Perro;

public class MascotaValidador {

    public static String validar(String tipo, String nombre, String edadTexto,
                                 String pesoTexto, String raza) {

        if (tipo == null) {
            return "Tipo: debe seleccionar Perro o Gato.";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return "Nombre: no puede estar vacío.";
        }

        int edad;
        try {
            edad = Integer.parseInt(edadTexto.trim());
        } catch (NumberFormatException e) {
            return "Edad: debe ser un número entero.";
        }
        if (edad < 0 || edad > 30) {
            return "Edad: debe estar entre 0 y 30 años.";
        }

        double peso;
        try {
            peso = Double.parseDouble(pesoTexto.trim());
        }  catch (NumberFormatException e) {
            return "Peso: debe ser un número entero.";
        }
        if (peso <=0){
            return "Peso: debe ser mayor a cero.";
        }

        if ("Perro".equals(tipo)) {
            if (raza == null || raza.trim().isEmpty()) {
                return "Raza: no puede estar vacía.";
            }
        }
        return null;
    }
}