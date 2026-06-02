package com.paucampos.tareasapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.paucampos.tareasapp.database.TareaRepository;
import com.paucampos.tareasapp.model.Tarea;

import java.util.Calendar;

public class FormularioTareaActivity extends AppCompatActivity {

    private EditText etTitulo;
    private EditText etDescripcion;
    private EditText etFecha;
    private Button btnGuardar;
    private Button btnCancelar;

    private TareaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_tarea);

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFecha = findViewById(R.id.etFecha);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        repository = new TareaRepository(this);

        etFecha.setOnClickListener(v -> mostrarDatePicker());
        btnGuardar.setOnClickListener(v -> guardarTarea());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void mostrarDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String fecha = year + "-" +
                            String.format("%02d", month + 1) + "-" +
                            String.format("%02d", dayOfMonth);

                    etFecha.setText(fecha);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void guardarTarea() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();

        if (titulo.isEmpty()) {
            etTitulo.setError("Ingresa un título");
            return;
        }

        if (fecha.isEmpty()) {
            etFecha.setError("Selecciona una fecha");
            return;
        }

        Tarea tarea = new Tarea(titulo, descripcion, fecha, "Pendiente");

        long resultado = repository.insertarTarea(tarea);

        if (resultado > 0) {
            Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }
}