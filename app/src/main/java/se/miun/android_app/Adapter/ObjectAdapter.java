package se.miun.android_app.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import se.miun.android_app.R;

import java.util.ArrayList;

import se.miun.android_app.Model.FactoryObject;

public class ObjectAdapter extends ArrayAdapter<FactoryObject> {

    private Activity activity;
    private ArrayList<FactoryObject> objects;
    private static LayoutInflater inflater = null;

    public ObjectAdapter(Activity activity, int textViewResourceId, ArrayList<FactoryObject> objects) {
        super(activity, textViewResourceId, objects);

        this.activity = activity;
        this.objects = objects;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return objects.size();
    }

    public FactoryObject getItem(FactoryObject position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView objectName;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_item_object, null);
            holder = new ViewHolder();

            holder.objectName = (TextView) vi.findViewById(R.id.factoryObjectName);


            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.objectName.setText(objects.get(position).getObjectName());


        return vi;
    }
}