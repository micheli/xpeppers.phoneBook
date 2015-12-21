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
package com.xpeppers.phonebook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.xpeppers.phonebook.db.DatabaseHandler;
import com.xpeppers.phonebook.model.Contact;
import com.xpeppers.phonebook.utils.CustomAdapter;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			getFragmentManager().beginTransaction()
					.replace(R.id.container, new AddFragment())
					.addToBackStack("ADD").commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		CustomAdapter adapter;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;

		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			final DatabaseHandler db = new DatabaseHandler(getActivity());
			List<Contact> contacts = new ArrayList<Contact>();
			contacts = db.getAllContacts();

			final ListView listContacts = (ListView) getActivity()
					.findViewById(R.id.listViewContacts);
			adapter = new CustomAdapter(getActivity(), R.layout.row_custom,
					contacts);
			listContacts.setAdapter(adapter);
			listContacts.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Contact c = (Contact) listContacts
							.getItemAtPosition(position);
					Bundle data = new Bundle();
					data.putSerializable("CONTACT", c);

					Fragment show = new ShowFragment();
					show.setArguments(data);
					getFragmentManager().beginTransaction()
							.replace(R.id.container, show, "SHOW")
							.addToBackStack("SHOW").commit();

				}
			});
			listContacts
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, final int position, long id) {

							final Contact c = (Contact) listContacts
									.getItemAtPosition(position);
							AlertDialog.Builder builder = new AlertDialog.Builder(
									getActivity());
							// Add the buttons
							builder.setMessage(R.string.dialog_message_cancel)
									.setTitle(R.string.dialog_title_cancel);

							builder.setPositiveButton(R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// User clicked OK button
											db.deleteContact(c);
											adapter.notifyDataSetChanged();
											dialog.dismiss();
										}
									});
							builder.setNegativeButton(R.string.no,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.dismiss();
										}
									});
							// Set other dialog properties

							// Create the AlertDialog
							AlertDialog dialog = builder.create();
							dialog.show();
							return true;

						}
					});
			listContacts.setTextFilterEnabled(true);
			EditText search = (EditText) getActivity().findViewById(
					R.id.editTextSearch);
			search.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (s.length()==0){
						adapter.resetData();
					}
					// Call back the Adapter with current character to Filter
					if ((count < before)) {
						// We're deleting char so we need to reset the adapter
						// data
						adapter.resetData();
					}
					adapter.getFilter().filter(s.toString());
					
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

			super.onStart();
		}
	}
}
