/*
This file is part of xpeppers.phoneBook

   Copyright 2015 Micheli Luca

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.xpeppers.phonebook.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.xpeppers.phonebook.R;
import com.xpeppers.phonebook.model.Contact;

public class CustomAdapter extends ArrayAdapter<Contact> implements Filterable {

	private Filter contactFilter;
	private List<Contact> contactList;
	private Context context;
	private List<Contact> origContactList;

	public CustomAdapter(Context context, int textViewResourceId,
			List<Contact> objects) {
		super(context, textViewResourceId, objects);
		this.contactList = objects;
		this.context = context;
		this.origContactList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getViewOptimize(position, convertView, parent);
	}

	public View getViewOptimize(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_custom, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.textViewNameSurname);
			viewHolder.number = (TextView) convertView
					.findViewById(R.id.textViewNumber);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Contact contatto = getItem(position);
		viewHolder.name.setText(contatto.getName() + " "
				+ contatto.getSurname());
		viewHolder.number.setText(contatto.getPhoneNumber());
		return convertView;
	}

	private class ViewHolder {
		public TextView name;
		public TextView number;
	}

	public void resetData() {
		contactList = origContactList;
	}

	@Override
	public Filter getFilter() {
		if (contactFilter == null)
			contactFilter = new ContactFilter();

		return contactFilter;
	}

	private class ContactFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = origContactList;
				results.count = origContactList.size();

			} else {
				// We perform filtering operation
				List<Contact> nContactList = new ArrayList<Contact>();
				for (Contact p : contactList) {
					if ((p.getName().toUpperCase()
							.startsWith(constraint.toString().toUpperCase()))||(p.getPhoneNumber().startsWith(constraint.toString())))
						nContactList.add(p);
				}
				results.values = nContactList;
				results.count = nContactList.size();

			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0) {
				notifyDataSetInvalidated();
			} else {
				contactList = (List<Contact>) results.values;
				 clear();
				 addAll(contactList);
				notifyDataSetChanged();
			}

		}

	}

}
