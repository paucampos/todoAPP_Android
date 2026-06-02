package com.paucampos.tareasapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paucampos.tareasapp.adapter.TareasAdapter;
import com.paucampos.tareasapp.database.TareaRepository;
import com.paucampos.tareasapp.model.Tarea;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnNuevaTarea;
    private LinearLayout contenedorSinTareas;
    private RecyclerView rvTareas;

    private TareaRepository repository;
    private TareasAdapter adapter;
    private List<Tarea> listaTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNuevaTarea = findViewById(R.id.btnNuevaTarea);
        contenedorSinTareas = findViewById(R.id.contenedorSinTareas);
        rvTareas = findViewById(R.id.rvTareas);

        repository = new TareaRepository(this);

        configurarRecyclerView();

        btnNuevaTarea.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormularioTareaActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTareas();
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

    private void cargarTareas() {
        List<Tarea> tareas = repository.obtenerTareas();
        adapter.actualizarLista(tareas);

        if (tareas.isEmpty()) {
            contenedorSinTareas.setVisibility(View.VISIBLE);
            rvTareas.setVisibility(View.GONE);
        } else {
            contenedorSinTareas.setVisibility(View.GONE);
            rvTareas.setVisibility(View.VISIBLE);
        }
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
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}