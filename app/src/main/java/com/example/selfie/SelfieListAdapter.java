package com.example.selfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.selfie.model.mediator.webdata.Selfie;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 3/17/15.
 */
public class SelfieListAdapter extends BaseAdapter {

    private static final String TAG = SelfieListAdapter.class.getSimpleName();
    private Context context;
    private List<Selfie> items;
    private LayoutInflater inflater;

    public SelfieListAdapter(Context context, List<Selfie> items) {
        this.context = context;
        this.items = items;

        inflater = LayoutInflater.from(context);
    }

    public SelfieListAdapter(Context context) {
        items = new ArrayList<Selfie>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public Selfie getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Selfie singleItem = getItem(position);
        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.selfie_item, parent, false);
            holder = new ViewHolder();

            holder.preview = (ImageView)convertView.findViewById(R.id.preview_img);
            holder.name = (TextView)convertView.findViewById(R.id.name_txt);
            holder.checkbox = (CheckBox)convertView.findViewById(R.id.select_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        byte [] outImage = singleItem.getPictureBlob();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap imageBm = BitmapFactory.decodeStream(imageStream);
        //holder.preview.setImageBitmap(decodeBitmap(singleItem.getPath(), 40, 40));
        holder.preview.setImageBitmap(imageBm);
        holder.name.setText(singleItem.getTitle());
        //holder.checkbox.setVisibility(visibility);
        convertView.setBackground(context.getResources().getDrawable(getBackgrnd(position)));

        return convertView;
    }

    private int getBackgrnd(int position){
        int[] items = {
                R.drawable.wood,
                R.drawable.blum,
                R.drawable.grey_flowers,
                R.drawable.flowers,
                R.drawable.field,
                R.drawable.houses,
                R.drawable.ocean};
        if (position<=6) {
            return items[position];
        } else if (position <= 12){
            int pos = Math.abs(6 - position);
            return items[pos];
        } else {
            return items[0];
        }
    }

    private int visibility;
    public void setVisible(boolean visible) {
        if (visible) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.GONE;
        }

        notifyDataSetChanged();
    }

    public void swap(List<Selfie> list) {
        if(list == null){
            items = new ArrayList<>();
        } else {
            items = new ArrayList<>(list);
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder{
        ImageView preview;
        TextView name;
        CheckBox checkbox;
    }

    public void addItem(Selfie model) {
        items.add(model);
        notifyDataSetChanged();
    }

    public static Bitmap decodeBitmap(String path, int reqWidth, int reqHeight) {
        File image = new File(path);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(image.getAbsolutePath(), options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
