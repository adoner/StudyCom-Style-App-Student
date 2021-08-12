package com.sanaltebesir.stb_student;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomQboxAdapter extends BaseAdapter {

    private Context context; //context
    private ArrayList<DataModel> itemsArray = new ArrayList<>();

    public CustomQboxAdapter(Activity context, ArrayList<DataModel> items){
        this.context = context;
        this.itemsArray = items;
    }

    @Override
    public int getCount() {
        return itemsArray.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_qbox, parent, false);

        }
        // get current item to be displayed
        DataModel currentItem = (DataModel) getItem(position);

        // get the TextView for item name and item description
        ImageView questionimage = convertView.findViewById(R.id.questionimage);
        ImageView qbox_durum_image = convertView.findViewById(R.id.qbox_durum_image);
        TextView title = convertView.findViewById(R.id.title);
        TextView message = convertView.findViewById(R.id.message);
        TextView asking_date = convertView.findViewById(R.id.asking_date);
        TextView viewed = convertView.findViewById(R.id.viewed);
        TextView tvsolved = convertView.findViewById(R.id.tvSolved);

        //sets the text for item name and item description from the current item object
        questionimage.setImageResource(currentItem.getImageid());
        qbox_durum_image.setImageResource(currentItem.getImageid2());
        title.setText(currentItem.getTitle());
        message.setText(currentItem.getMessageDate());
        asking_date.setText(currentItem.getAskingDate());
        viewed.setText(currentItem.getViewed());
        tvsolved.setText(currentItem.getTvsolved());

        return convertView;
    }
}
