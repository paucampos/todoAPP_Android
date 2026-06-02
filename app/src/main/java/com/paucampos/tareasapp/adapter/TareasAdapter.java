package com.paucampos.tareasapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paucampos.tareasapp.R;
import com.paucampos.tareasapp.model.Tarea;

import java.util.List;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareaViewHolder> {

    private List<Tarea> listaTareas;
    private OnTareaClickListener listener;

    public interface OnTareaClickListener {
        void onEliminarClick(Tarea tarea);
        void onCompletarClick(Tarea tarea);
    }

    public TareasAdapter(List<Tarea> listaTareas, OnTareaClickListener listener) {
        this.listaTareas = listaTareas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarea, parent, false);

        return new TareaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);

        holder.tvTitulo.setText(tarea.getTitulo());
        holder.tvFechaEstado.setText(tarea.getFecha() + " · " + tarea.getEstado());
        holder.tvDescripcion.setText(tarea.getDescripcion());
        holder.btnCompletar.setVisibility(
                tarea.getEstado().equals("Completada") ? View.GONE : View.VISIBLE
        );

        holder.btnCompletar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCompletarClick(tarea);
            }
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminarClick(tarea);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public void actualizarLista(List<Tarea> nuevaLista) {
        this.listaTareas = nuevaLista;
        notifyDataSetChanged();
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo;
        TextView tvFechaEstado;
        TextView tvDescripcion;
        Button btnEliminar;
        Button btnCompletar;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvFechaEstado = itemView.findViewById(R.id.tvFechaEstado);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnCompletar = itemView.findViewById(R.id.btnCompletar);
        }
    }
}