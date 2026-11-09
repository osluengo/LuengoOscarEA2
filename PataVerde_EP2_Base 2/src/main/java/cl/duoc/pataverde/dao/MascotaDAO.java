package cl.duoc.pataverde.dao;

import cl.duoc.pataverde.model.Gato;
import cl.duoc.pataverde.model.Perro;

import java.io.IOException;
import java.util.List;

public interface MascotaDAO {
    List<Perro> leerPerros() throws IOException;
    List<Gato> leerGatos() throws IOException;
    void guardarPerros(List<Perro> perros) throws IOException;
    void guardarGatos(List<Gato> gatos) throws IOException;
}
