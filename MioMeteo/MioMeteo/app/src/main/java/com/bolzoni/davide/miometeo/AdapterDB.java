package com.bolzoni.davide.miometeo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Davide on 28/06/2016.
 */
public class AdapterDB extends BaseAdapter {
    Context context;
    String[] cittaAdapter;
    private static LayoutInflater inflater = null;

    public AdapterDB(Insert_City insert_city, String[] cittaAdapter) {
        this.cittaAdapter = cittaAdapter;
        context=insert_city;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cittaAdapter.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder
    {
        TextView tv;
        ImageView img;
        ImageView img1;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.citta, null);
        holder.tv=(TextView) rowView.findViewById(R.id.cittaAdapterDB);
        holder.tv.setText(cittaAdapter[position]);
        holder.img=(ImageView) rowView.findViewById(R.id.imageButtonAdapterDB);
        holder.img1=(ImageView) rowView.findViewById(R.id.arrowAdapterDB);

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("città", cittaAdapter[position]);
                intent.putExtra("flag", "noGps");
                context.startActivity(intent);
            }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("città", cittaAdapter[position]);
                intent.putExtra("flag", "noGps");
                context.startActivity(intent);
            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Insert_City.class);
                intent.putExtra("flag", "b");
                intent.putExtra("città", cittaAdapter[position]);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        return rowView;
    }
}
