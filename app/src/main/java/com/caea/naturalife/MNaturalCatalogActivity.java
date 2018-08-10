package com.caea.naturalife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.caea.naturalife.adapter.MedicamentoAdapter;
import com.caea.naturalife.database.MNaturalReference;
import com.caea.naturalife.entity.Carrito;
import com.caea.naturalife.entity.Mnaturales;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MNaturalCatalogActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private List<Mnaturales> mnaturalesList;
    private List<Carrito> carritoList;
    private MedicamentoAdapter adapter;

    private FirebaseDatabase NaturaLifeBD;
    private DatabaseReference NaturaLifeReference;

    private ImageButton btnadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnatural_catalog);

        btnadd = findViewById(R.id.ibcarrito);

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        mnaturalesList = new ArrayList<>();

        NaturaLifeBD = FirebaseDatabase.getInstance();

        adapter = new MedicamentoAdapter(mnaturalesList);
        recycler.setAdapter(adapter);

        NaturaLifeBD.getReference().child(MNaturalReference.MEDICAMENTO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mnaturalesList.removeAll(mnaturalesList);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Mnaturales mnaturales = snapshot.getValue(Mnaturales.class);
                    mnaturalesList.add(mnaturales);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MNaturalCatalogActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickcarrito(View view) {
        Intent intent = new Intent(this, MNaturalCarritoActivity.class);
        startActivity(intent);
    }

    public void onClickMinusQuantity(View view) {
        final EditText etmenor = findViewById(R.id.etcantidad);
        final ImageButton ibminus = findViewById(R.id.ibminus);
        ibminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidad = Integer.parseInt(etmenor.getText().toString());
                cantidad = cantidad - 1;
                if (cantidad <= 0) {
                    Toast.makeText(MNaturalCatalogActivity.this, "La cantidad no puede ser 0", Toast.LENGTH_SHORT).show();
                    cantidad = 1;
                }
                etmenor.setText(String.valueOf(cantidad));
            }
        });
    }

    public void onClickPlusQuantity(View view) {
        final EditText etmas = findViewById(R.id.etcantidad);
        final ImageButton ibplus = findViewById(R.id.ibplus);
        ibplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidad = Integer.parseInt(etmas.getText().toString());
                cantidad = cantidad + 1;
                etmas.setText(String.valueOf(cantidad));
            }
        });
    }

    public String generatekey(String nombre) {
        String frag1, frag2, sinespa, indicador;

        if (nombre.indexOf(' ', 0) > 0) {
            frag1 = nombre.substring(0, 1);
            int espacio = frag1.indexOf(' ', 0);
            frag2 = nombre.substring(espacio + 1, nombre.length());
            sinespa = frag1.concat(frag2);
            indicador = sinespa.replace(" ", "");
            nombre = indicador;
        }
        return nombre;
    }

    public Integer returncantidad(int quantity) {
        return quantity;
    }

    /*public void onClickAddCarrito(View v) {
        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etcantidad = v.findViewById(R.id.etcantidad);

                NaturaLifeBD = FirebaseDatabase.getInstance();
                NaturaLifeReference = NaturaLifeBD.getReference();

                int cantidad = Integer.parseInt(etcantidad.getText().toString());
                int ncarrito = 1;
                Double precio = mnaturalesList.get(recycler.getChildAdapterPosition(v)).getPrecio();
                Double total = cantidad * precio;

                Carrito carrito = new Carrito(mnaturalesList.get(recycler.getChildAdapterPosition(v)).getNombre(),
                        mnaturalesList.get(recycler.getChildAdapterPosition(v)).getDescripcion(),
                        mnaturalesList.get(recycler.getChildAdapterPosition(v)).getFormula(),
                        mnaturalesList.get(recycler.getChildAdapterPosition(v)).getFotourl(),
                        mnaturalesList.get(recycler.getChildAdapterPosition(v)).getSabor(),
                        cantidad, new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                        ncarrito, precio, total);
                String key = mnaturalesList.get(recycler.getChildAdapterPosition(v)).getNombre();
                String frag1, frag2, sinespa, indicador = "";

                if (key.indexOf(' ', 0) > 0) {
                    frag1 = key.substring(0, 1);
                    int espacio = frag1.indexOf(' ', 0);
                    frag2 = key.substring(espacio + 1, key.length());
                    sinespa = frag1.concat(frag2);
                    indicador = sinespa.replace(" ", "");
                }

                NaturaLifeReference.child(MNaturalReference.CARRITO).child(indicador).setValue(carrito);
                Toast.makeText(v.getContext(),
                        "Medicamento " + mnaturalesList.get(recycler.getChildAdapterPosition(v)).getNombre() + " añadido",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
