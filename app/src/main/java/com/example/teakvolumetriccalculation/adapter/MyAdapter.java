package com.example.teakvolumetriccalculation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teakvolumetriccalculation.R;
import com.example.teakvolumetriccalculation.modelo.User;
import com.example.teakvolumetriccalculation.modelo.VolumM;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context1;
    private List<User> userArrayList;

    public MyAdapter(Context context1, List<User> userArrayList) {
        this.context1 = context1;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Optimiza la inflación al no adjuntar inmediatamente al ViewGroup (parent)
        View v = LayoutInflater.from(context1).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.imageUrl.setText(String.format("imageUrl = %s", user.getImageUrl()));
        holder.volumen.setText(String.format("%s", user.getVolumen()));
        holder.volumenAprox.setText(String.format("%s", user.getVolumenAprox()));

        // Cargar imagen usando Picasso o similar
        /*Picasso.get().load(user.getImagenUrl()).into(holder.imageView);

        holder.volumen.setText(String.format("%.2f", user.getVolumen())); // Asumiendo que volumen es double
        holder.volumenAprox.setText(String.format("%.2f", user.getVolumenAprox())); // Formato para decimales*/
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView imageUrl, volumen, volumenAprox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUrl = itemView.findViewById(R.id.textViewImaUrl);
            volumen = itemView.findViewById(R.id.textViewVolum);
            volumenAprox = itemView.findViewById(R.id.textViewVolumAprox);
        }
    }

    /*@NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        User user = userArrayList.get(position);

        holder.imagenUrl.setText(user.imagenUrl);
        holder.volumen.setText(String.valueOf(user.volumen));
        holder.volumenAprox.setText(String.valueOf(user.volumenAprox));

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView imagenUrl, volumen, volumenAprox;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenUrl = itemView.findViewById(R.id.tvimagneUri);
            volumen = itemView.findViewById(R.id.tvvolumen);
            volumenAprox = itemView.findViewById(R.id.tvvolumenapro);
        }
    }*/
}
