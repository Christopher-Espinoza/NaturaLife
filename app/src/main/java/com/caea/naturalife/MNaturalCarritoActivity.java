package com.caea.naturalife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.caea.naturalife.adapter.CarritoAdapter;
import com.caea.naturalife.database.MNaturalReference;
import com.caea.naturalife.entity.Carrito;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MNaturalCarritoActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private List<Carrito> carritoList;
    private CarritoAdapter adapter;

    private FirebaseDatabase natura;
    private DatabaseReference naturaref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnatural_carrito);

        recycler = findViewById(R.id.recyclercarrito);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        carritoList = new ArrayList<>();

        natura = FirebaseDatabase.getInstance();

        adapter = new CarritoAdapter(carritoList);
        recycler.setAdapter(adapter);

        natura.getReference().child(MNaturalReference.CARRITO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                carritoList.removeAll(carritoList);
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Carrito carrito = snapshot.getValue(Carrito.class);
                    carritoList.add(carrito);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MNaturalCarritoActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
