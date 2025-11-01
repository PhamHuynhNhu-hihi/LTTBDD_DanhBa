package com.example.baitapcuoiky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private OnItemClickListener listener;

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);

        if (contact != null) {
            tvName.setText(contact.getName());
            tvPhone.setText(contact.getPhone());
        }

        convertView.setOnClickListener(v -> {
            if (listener != null && contact != null) {
                listener.onItemClick(contact);
            }
        });

        return convertView;
    }
}
