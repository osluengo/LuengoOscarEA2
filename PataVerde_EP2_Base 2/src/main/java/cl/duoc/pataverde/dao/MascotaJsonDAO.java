package cl.duoc.pataverde.dao;

import cl.duoc.pataverde.model.Gato;
import cl.duoc.pataverde.model.Perro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MascotaJsonDAO implements MascotaDAO {

    private static final String ARCHIVO_PERROS = "perros.json";
    private static final String ARCHIVO_GATOS = "gatos.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public List<Perro> leerPerros() throws IOException {
        Path ruta = Path.of(ARCHIVO_PERROS);
        if (!Files.exists(ruta)) {
            return new ArrayList<>();
        }
        try (Reader lector = Files.newBufferedReader(ruta)) {
            Type tipo = new TypeToken<List<Perro>>() {}.getType();
            List<Perro> perros = gson.fromJson(lector, tipo);
            return perros != null ? perros : new ArrayList<>();
        } catch (JsonParseException e) {
            throw new IOException("El archivo " + ARCHIVO_PERROS + " está dañado.", e);
        }
    }

    @Override
    public List<Gato> leerGatos() throws IOException {
        Path ruta = Path.of(ARCHIVO_GATOS);
        if (!Files.exists(ruta)) {
            return new ArrayList<>();
        }
        try (Reader lector = Files.newBufferedReader(ruta)) {
            Type tipo = new TypeToken<List<Gato>>() {}.getType();
            List<Gato> gatos = gson.fromJson(lector, tipo);
            return gatos != null ? gatos : new ArrayList<>();
        } catch (JsonParseException e) {
            throw new IOException("El archivo " + ARCHIVO_GATOS + " está dañado.", e);
        }
    }

    @Override
    public void guardarGatos(List<Gato> gatos) throws IOException {
        try (Writer escritor = Files.newBufferedWriter(Path.of(ARCHIVO_GATOS))) {
            gson.toJson(gatos, escritor);
        }
    }

    @Override
    public void guardarPerros(List<Perro> perros) throws IOException {
        try (Writer escritor = Files.newBufferedWriter(Path.of(ARCHIVO_PERROS))) {
            gson.toJson(perros, escritor);
        }
    }
}