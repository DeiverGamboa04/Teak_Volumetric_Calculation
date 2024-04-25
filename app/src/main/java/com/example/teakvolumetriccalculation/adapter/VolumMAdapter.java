package com.example.teakvolumetriccalculation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teakvolumetriccalculation.R;
import com.example.teakvolumetriccalculation.modelo.VolumM;

import java.util.List;

public class VolumMAdapter extends RecyclerView.Adapter<VolumMAdapter.ViewHolder>{

     private Context context;
     private List<VolumM> listaVolumen;

    public VolumMAdapter(Context context, List<VolumM> listaVolumen) {
        this.context = context;
        this.listaVolumen = listaVolumen;
    }


    @NonNull
    @Override
    public VolumMAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_volumm_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolumMAdapter.ViewHolder holder, int position) {
        VolumM volum = listaVolumen.get(position);
        holder.diametro.setText(String.format("Diámetro: %s", volum.getDiametro()));
        holder.alturaComercial.setText(String.format("Altura comercial: %s", volum.getAlturaComercial()));
        holder.factorForma.setText(String.format("Factor de forma: %s", volum.getFactorForma()));
        holder.constante.setText(String.format("Constante: %s", volum.getConstante()));
        holder.volumen.setText(String.format("Volumen: %s", volum.getVolumen()));
        holder.volumenAproximado.setText(String.format("Volumen aproximado de 1 año: %s", volum.getVolumenAproximado()));
    }

    @Override
    public int getItemCount() {
        return listaVolumen.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView diametro, alturaComercial, factorForma, constante, volumen, volumenAproximado;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            diametro = itemView.findViewById(R.id.textView5D);
            alturaComercial = itemView.findViewById(R.id.textView6A);
            factorForma = itemView.findViewById(R.id.textView7F);
            constante = itemView.findViewById(R.id.textView8C);
            volumen = itemView.findViewById(R.id.textView9V);
            volumenAproximado = itemView.findViewById(R.id.textView10VA);
        }
    }
}
