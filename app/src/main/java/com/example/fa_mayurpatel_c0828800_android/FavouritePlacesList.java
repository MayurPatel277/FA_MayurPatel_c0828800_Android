package com.example.fa_mayurpatel_c0828800_android;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class FavouritePlacesList extends AppCompatActivity {
    RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    List<DataBaseModel> allPlaces;

    Adapter adapter;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        findIds();
        getData();

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void getData() {
        dataBaseHelper = new DataBaseHelper(FavouritePlacesList.this);
        allPlaces = dataBaseHelper.getAllPlaces();

        adapter = new Adapter(FavouritePlacesList.this, allPlaces);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void findIds() {
        recyclerView = findViewById(R.id.recycler);
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

        Context context;
        List<DataBaseModel> list;


        public Adapter(Context context, List<DataBaseModel> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            DataBaseModel model = list.get(position);

            if(model.getIsVisited() == 1)
            holder.cellLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.lightgray));

            holder.name.setText("Latitude: " + model.getLat() + "\n" + "Longitude: " + model.getLng() + "\n" + model.getPlaceName());

            DataBaseModel toDisplayModel = list.get(position);


            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataBaseHelper.deletePlace(model);
                    getData();
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FavouritePlacesList.this, MapsActivity.class);
                    intent.putExtra("TYPE", "");
                    intent.putExtra("MODEL", model);
                    startActivity(intent);
                    finish();
                }
            });

            holder.displayLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FavouritePlacesList.this, MapsActivity.class);
                    String latt = toDisplayModel.getLat();
                    String longg = toDisplayModel.getLng();
                    intent.putExtra("TYPE", "");
                    intent.putExtra("MODEL", toDisplayModel);
                    startActivity(intent);
                    finish();
                }
            });

            holder.isVisited.setChecked(model.getIsVisited() == 1);
            holder.isVisited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    dataBaseHelper.updateIsVisited(model.getId(), b ? 1 : 0);
                    holder.cellLayout.setBackgroundTintList(context.getResources().getColorStateList(b? R.color.lightgray: R.color.white));
                }
            });

            holder.isFav.setChecked(model.getIsFav() == 1);
            holder.isFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    dataBaseHelper.updateIsFav(model.getId(), b ? 1 : 0);
                }
            });
        }


        @Override
        public int getItemCount() {
            return list.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout cellLayout;
            TextView name;
            Button delete, edit, displayLocation;
            CheckBox isVisited, isFav;


            public MyViewHolder(View itemView) {
                super(itemView);

                cellLayout = itemView.findViewById(R.id.cell_layout);
                name = itemView.findViewById(R.id.name);
                delete = itemView.findViewById(R.id.delete);
                edit = itemView.findViewById(R.id.edit);
                displayLocation = itemView.findViewById(R.id.displayLocation);
                isVisited = itemView.findViewById(R.id.is_visited);
                isFav = itemView.findViewById(R.id.is_fav);

            }
        }
    }
}