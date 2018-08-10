package com.caea.naturalife.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caea.naturalife.R;
import com.caea.naturalife.entity.Carrito;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>{

    List<Carrito> carritos;

    public CarritoAdapter(List<Carrito> carritos) {
        this.carritos = carritos;
    }

    @Override
    public CarritoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_carrito, parent, false);
        CarritoViewHolder carritoViewHolder = new CarritoViewHolder(view);
        return carritoViewHolder;
    }

    @Override
    public void onBindViewHolder(CarritoViewHolder holder, int position) {
        Carrito carrito = carritos.get(position);
        holder.nombrecarrito.setText(carrito.getNombre());
        holder.descripcioncarrito.setText("Descripcion: " + carrito.getDescripcion());
        holder.formulacarrito.setText("Formula: " + carrito.getFormula());
        holder.saborcarrito.setText("Sabor: " + carrito.getSabor());
        holder.preciocarrito.setText(carrito.getPrecio().toString());
        holder.totalcarrito.setText(carrito.getTotal().toString());
        Glide.with(holder.itemView.getContext()).load(carrito.getImageurl()).into(holder.imedicamentocarrito);
    }

    @Override
    public int getItemCount() {
        return carritos.size();
    }

    public static class CarritoViewHolder extends RecyclerView.ViewHolder{

        TextView nombrecarrito, descripcioncarrito, formulacarrito, saborcarrito, preciocarrito, totalcarrito;
        ImageView imedicamentocarrito;


        public CarritoViewHolder(View view) {
            super(view);
            imedicamentocarrito = view.findViewById(R.id.imedicamentocarrito);
            nombrecarrito = view.findViewById(R.id.cardtvnombrecarrito);
            descripcioncarrito = view.findViewById(R.id.cardtvdescripcioncarrito);
            formulacarrito = view.findViewById(R.id.cardtvformulacarrito);
            saborcarrito = view.findViewById(R.id.cardtvsaborcarrito);
            preciocarrito = view.findViewById(R.id.cardtvpreciocarrito);
            totalcarrito = view.findViewById(R.id.cardtvtotalcarrito);
        }
    }
}
