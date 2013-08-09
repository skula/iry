package com.skula.iry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skula.iry.models.Person;

public class PersonAdapter extends ArrayAdapter<Person> {
	Context context;
	int layoutResourceId;
	Person data[] = null;

	public PersonAdapter(Context context, int layoutResourceId, Person[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Person p = data[position];
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.item_layout, parent, false);

		TextView id = (TextView) rowView.findViewById(R.id.itemlistid);
		id.setText(p.getId());
		TextView itemlistname = (TextView) rowView
				.findViewById(R.id.itemlistname);
		itemlistname.setText(p.getLastname() + " " + p.getFirstname());
		return rowView;
	}
}
