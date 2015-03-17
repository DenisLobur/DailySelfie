package com.example.selfie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 3/17/15.
 */
public class SelfieListAdapter extends BaseAdapter {
    Context context;
    List<SelfieModel> items;
    LayoutInflater inflater;

    public SelfieListAdapter(Context context, List<SelfieModel> items) {
        this.context = context;
        this.items = items;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public SelfieModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelfieModel singleItem = getItem(position);
        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.selfie_item, parent, false);
            holder = new ViewHolder();

            holder.preview = (ImageView)convertView.findViewById(R.id.preview_img);
            holder.name = (TextView)convertView.findViewById(R.id.name_txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

//        holder.preview.setImageBitmap(Uri.parse(""));
        holder.name.setText(singleItem.getName());

        return convertView;
    }

    public void swap(List<SelfieModel> list){
        if(list == null){
            items = new ArrayList<SelfieModel>();
        } else {
            items = new ArrayList<>(list);
        }
    }

    private static class ViewHolder{
        ImageView preview;
        TextView name;
    }
}
