package com.paucampos.tareasapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paucampos.tareasapp.adapter.TareasAdapter;
import com.paucampos.tareasapp.database.TareaRepository;
import com.paucampos.tareasapp.model.Tarea;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etTitulo;
    private EditText etDescripcion;
    private EditText etFecha;
    private Button btnGuardar;
    private TextView tvSinTareas;
    private RecyclerView rvTareas;

    private TareaRepository repository;
    private TareasAdapter adapter;
    private List<Tarea> listaTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarVistas();
        configurarRecyclerView();

        repository = new TareaRepository(this);

        cargarTareas();
        etFecha.setOnClickListener(v -> mostrarDatePicker());

        btnGuardar.setOnClickListener(v -> guardarTarea());
    }

    private void inicializarVistas() {
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFecha = findViewById(R.id.etFecha);
        btnGuardar = findViewById(R.id.btnGuardar);
        tvSinTareas = findViewById(R.id.tvSinTareas);
        rvTareas = findViewById(R.id.rvTareas);
    }

    private void configurarRecyclerView() {
        listaTareas = new ArrayList<>();

        adapter = new TareasAdapter(listaTareas, new TareasAdapter.OnTareaClickListener() {
            @Override
            public void onEliminarClick(Tarea tarea) {
                mostrarConfirmacionEliminar(tarea);
            }

            @Override
            public void onCompletarClick(Tarea tarea) {
                int filas = repository.completarTarea(tarea.getId());

                if (filas > 0) {
                    Toast.makeText(MainActivity.this, "Tarea completada", Toast.LENGTH_SHORT).show();
                    cargarTareas();
                }
            }
        });

        rvTareas.setLayoutManager(new LinearLayoutManager(this));
        rvTareas.setAdapter(adapter);
    }

    private void guardarTarea() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();
        String estado = "Pendiente";

        if (titulo.isEmpty()) {
            etTitulo.setError("Ingresa un título");
            return;
        }

        if (fecha.isEmpty()) {
            etFecha.setError("Ingresa una fecha");
            return;
        }

        Tarea tarea = new Tarea(titulo, descripcion, fecha, estado);

        long resultado = repository.insertarTarea(tarea);

        if (resultado > 0) {
            Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
            limpiarFormulario();
            cargarTareas();
        } else {
            Toast.makeText(this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarTareas() {
        List<Tarea> tareas = repository.obtenerTareas();
        adapter.actualizarLista(tareas);

        if (tareas.isEmpty()) {
            tvSinTareas.setVisibility(View.VISIBLE);
            rvTareas.setVisibility(View.GONE);
        } else {
            tvSinTareas.setVisibility(View.GONE);
            rvTareas.setVisibility(View.VISIBLE);
        }
    }

    private void limpiarFormulario() {
        etTitulo.setText("");
        etDescripcion.setText("");
        etFecha.setText("");
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

    private void mostrarConfirmacionEliminar(Tarea tarea) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar tarea")
                .setMessage("¿Deseas eliminar esta tarea?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    int filas = repository.eliminarTarea(tarea.getId());

                    if (filas > 0) {
                        Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                        cargarTareas();
                    } else {
                        Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}