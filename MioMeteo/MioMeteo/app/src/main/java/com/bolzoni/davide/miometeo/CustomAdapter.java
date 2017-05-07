package com.bolzoni.davide.miometeo;

/**
 * Created by Davide on 23/05/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class CustomAdapter extends BaseAdapter{
    String [] forecastAdapter;

    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomAdapter(MainActivity mainActivity, String[] forecastFM, int[] iconFM) {
        // TODO Auto-generated constructor stub
        forecastAdapter=forecastFM;
        context=mainActivity;
        imageId=iconFM;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return forecastAdapter.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.forecast, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(forecastAdapter[position]);

        int resourceId = context.getResources().getIdentifier("drawable/icon" + imageId[position], null, context.getPackageName());
        holder.img.setImageResource(resourceId);


        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //qua sta cliccando la row
                // TODO Auto-generated method stub
            }
        });
        return rowView;
    }

}
