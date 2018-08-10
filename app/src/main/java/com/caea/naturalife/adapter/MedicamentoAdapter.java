package com.caea.naturalife.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caea.naturalife.R;
import com.caea.naturalife.database.MNaturalReference;
import com.caea.naturalife.entity.CabFactura;
import com.caea.naturalife.entity.Carrito;
import com.caea.naturalife.entity.Mnaturales;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.MedicamentoViewHolder> {

    List<Mnaturales> mnaturales;

    public MedicamentoAdapter(List<Mnaturales> mnaturales) {
        this.mnaturales = mnaturales;
    }

    @Override
    public MedicamentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_medicameto, parent, false);
        MedicamentoViewHolder medicamentoviewholder = new MedicamentoViewHolder(view);
        return medicamentoviewholder;
    }

    @Override
    public void onBindViewHolder(final MedicamentoViewHolder holder, int position) {
        Mnaturales mnatural = mnaturales.get(position);

        holder.tvnombre.setText(mnatural.getNombre());
        holder.tvdescripcion.setText(mnatural.getDescripcion());
        holder.tvformula.setText(mnatural.getFormula());
        holder.tvsabor.setText(mnatural.getSabor());
        holder.tvprecio.setText(mnatural.getPrecio().toString());
        holder.tvurl.setText(mnatural.getFotourl());
        holder.setOnclickListener();

        Glide.with(holder.itemView.getContext()).load(mnatural.getFotourl()).into(holder.imedicamento);
    }

    @Override
    public int getItemCount() {
        return mnaturales.size();
    }

    public static class MedicamentoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvnombre, tvdescripcion, tvformula, tvsabor, tvprecio, tvurl, tvusuario;
        ImageView imedicamento;
        EditText etcantidad;
        ImageButton ibcarrito, sumar, restar, ibmodificar;
        FirebaseDatabase NaturaLifeBD;
        DatabaseReference NaturaLifeReference;
        private int cantidad, ncarrito;
        private Double precio, total;
        Context context;

        public MedicamentoViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            context = view.getContext();
            ibmodificar = view.findViewById(R.id.ibmodificar);
            imedicamento = view.findViewById(R.id.imedicamento);
            tvusuario = view.findViewById(R.id.tvuser);
            tvurl = view.findViewById(R.id.tvurl);
            tvnombre = view.findViewById(R.id.cardtvnombre);
            tvdescripcion = view.findViewById(R.id.cardtvdescripcion);
            tvformula = view.findViewById(R.id.cardtvformula);
            tvsabor = view.findViewById(R.id.cardtvsabor);
            tvprecio = view.findViewById(R.id.cardtvprecio);
            etcantidad = view.findViewById(R.id.etcantidad);
            ibcarrito = view.findViewById(R.id.ibcarrito);
            sumar = view.findViewById(R.id.ibplus);
            restar = view.findViewById(R.id.ibminus);
        }

        public void setOnclickListener() {
            ibcarrito.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ibcarrito:
                    FirebaseDatabase natura = FirebaseDatabase.getInstance();
                    DatabaseReference naturaref = natura.getReference();

                    int cantidad = Integer.parseInt(etcantidad.getText().toString());
                    int ncarrito = 1;
                    Double precio = Double.valueOf(tvprecio.getText().toString());
                    Double total = cantidad * precio;

                    Carrito carrito = new Carrito(tvnombre.getText().toString(),
                            tvdescripcion.getText().toString(),
                            tvformula.getText().toString(),
                            tvurl.getText().toString(),
                            tvsabor.getText().toString(),
                            cantidad, new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                            ncarrito, precio, total);
                    //guardando en el carrito
                    String key = tvnombre.getText().toString();
                    naturaref.child(MNaturalReference.CARRITO).child(generatekey(key)).setValue(carrito);

                    //guardando el detalle
                    String usuario = tvusuario.getText().toString();
                    naturaref.child(MNaturalReference.DETALLE).child(generatekey(usuario)).setValue(carrito);

                    //guardando la cabecera de la facutra
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    String emision = calendar.getTime().toString();
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
                    String vencimiento = calendar.getTime().toString();

                    CabFactura cabFactura = new CabFactura(usuario,emision,
                            vencimiento);
                    naturaref.child(MNaturalReference.FACTURA).child(generatekey(usuario)).setValue(cabFactura);
                    Toast.makeText(v.getContext(),
                            "Medicamento '" + tvnombre.getText().toString() + "' añadido",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        private String generatekey(String key) {
            String frag1, frag2, sinespa, indicador = "";
            frag1 = key.substring(0, 1);
            int espacio = key.indexOf(' ', 0);
            frag2 = key.substring(espacio + 1, key.length());
            sinespa = frag1.concat(frag2);
            indicador = sinespa.replace(" ", "");
            key = indicador;

            return key;
        }
    }
}

