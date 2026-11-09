package cl.duoc.pataverde.controller;

import cl.duoc.pataverde.dao.MascotaJsonDAO;
import cl.duoc.pataverde.model.Gato;
import cl.duoc.pataverde.model.Mascota;
import cl.duoc.pataverde.model.Perro;
import cl.duoc.pataverde.repository.MascotaRepository;
import cl.duoc.pataverde.service.MascotaValidador;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class MainController {

    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private TextField txtPeso;
    @FXML private TextField txtRaza;
    @FXML private TextField txtBuscar;
    @FXML private CheckBox chkVacunado;
    @FXML private CheckBox chkAdopcion;
    @FXML private CheckBox chkExterior;
    @FXML private Button btnRegistrar;
    @FXML private Button btnEliminar;
    @FXML private Label lblMensaje;
    @FXML private TableView<Mascota> tblMascotas;
    @FXML private TableColumn<Mascota, String> colTipo;
    @FXML private TableColumn<Mascota, String> colNombre;
    @FXML private TableColumn<Mascota, Integer> colEdad;
    @FXML private TableColumn<Mascota, Double> colPeso;
    @FXML private TableColumn<Mascota, Double> colCosto;
    private MascotaRepository repository;

    @FXML public void initialize() {
        repository = new MascotaRepository(new MascotaJsonDAO());
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        cbTipo.getItems().addAll("Perro", "Gato");
        txtRaza.setDisable(true);
        chkVacunado.setDisable(true);
        chkAdopcion.setDisable(true);
        chkExterior.setDisable(true);
        cbTipo.setOnAction(e -> cambiarTipo());
        btnRegistrar.setOnAction(e -> registrar());
        btnEliminar.setOnAction(e -> eliminar());
        colCosto.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().calcularCostoConsulta()));
        FilteredList<Mascota> filtradas = new FilteredList<>(repository.getMascotas(), m -> true);
        tblMascotas.setItems(filtradas);
        txtBuscar.textProperty().addListener((obs, anterior, texto) ->
                filtradas.setPredicate(m -> coincide(m, texto)));
        try {
            repository.cargar();
        } catch (IOException e) {
            lblMensaje.setText("Error al leer los archivos: " + e.getMessage());
        }
    }

    private void cambiarTipo() {
        String tipo = cbTipo.getValue();
        boolean esPerro = "Perro".equals(tipo);
        boolean esGato = "Gato".equals(tipo);
        txtRaza.setDisable(!esPerro);
        chkVacunado.setDisable(!esPerro);
        chkAdopcion.setDisable(!esPerro);
        chkExterior.setDisable(!esGato);

    }

    private void registrar() {
        String error = MascotaValidador.validar(
                cbTipo.getValue(),
                txtNombre.getText(),
                txtEdad.getText(),
                txtPeso.getText(),
                txtRaza.getText());

        if (error != null) {
            lblMensaje.setText(error);
            return;
        }

        lblMensaje.setText("Datos válidos");   // temporal, para probar
        String nombre = txtNombre.getText().trim();
        int edad = Integer.parseInt(txtEdad.getText().trim());
        double peso = Double.parseDouble(txtPeso.getText().trim().replace(',', '.'));
        Mascota mascota;

        if ("Perro".equals(cbTipo.getValue())) {
            Perro perro = new Perro(nombre, edad, peso, txtRaza.getText().trim(), chkVacunado.isSelected());
            if (chkAdopcion.isSelected()) {
                perro.marcarDisponibleParaAdopcion();
            }
            mascota = perro;
        } else {
            Gato gato = new Gato(nombre, edad, peso, chkExterior.isSelected());
            mascota = gato;
        }
        try {
            repository.agregar(mascota);
            lblMensaje.setText(mascota.getNombre() + " (" + mascota.getTipo() + ") registrado correctamente.");
            limpiarFormulario();
        } catch (IOException e) {
            lblMensaje.setText("Error al guardar los archivos: " + e.getMessage());
        }
        }

    private void limpiarFormulario() {
        cbTipo.setValue(null);
        txtNombre.clear();
        txtEdad.clear();
        txtPeso.clear();
        txtRaza.clear();
        chkVacunado.setSelected(false);
        chkAdopcion.setSelected(false);
        chkExterior.setSelected(false);
        cambiarTipo();
    }

    private void eliminar() {
        Mascota seleccionada = tblMascotas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            lblMensaje.setText("Debe seleccionar una mascota de la tabla para eliminarla.");
            return;
        }

        try {
            repository.eliminar(seleccionada);
            lblMensaje.setText(seleccionada.getNombre() + " (" + seleccionada.getTipo() + ") eliminado correctamente.");
        } catch (IOException e) {
            lblMensaje.setText("Error al guardar los archivos: " + e.getMessage());
        }
    }

    private boolean coincide(Mascota m, String texto) {
        if (texto == null || texto.isBlank()) {
            return true;
        }
        String buscado = texto.trim().toLowerCase();
        return m.getNombre().toLowerCase().contains(buscado)
                || m.getTipo().toLowerCase().contains(buscado);
    }

}


