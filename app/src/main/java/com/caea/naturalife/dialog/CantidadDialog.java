package com.caea.naturalife.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.caea.naturalife.MNaturalCatalogActivity;
import com.caea.naturalife.R;

public class CantidadDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CharSequence[] cantidades = new CharSequence[10];
        cantidades[0] = "1";
        cantidades[1] = "2";
        cantidades[2] = "3";
        cantidades[3] = "4";
        cantidades[4] = "5";
        cantidades[5] = "6";
        cantidades[6] = "7";
        cantidades[7] = "8";
        cantidades[8] = "9";
        cantidades[9] = "10";

        builder.setTitle(R.string.ingresecantidad)
        .setItems(cantidades, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MNaturalCatalogActivity catalogo = new MNaturalCatalogActivity();
                catalogo.returncantidad(Integer.parseInt(String.valueOf(cantidades[which])));
            }
        });
        return builder.create();
    }
}
