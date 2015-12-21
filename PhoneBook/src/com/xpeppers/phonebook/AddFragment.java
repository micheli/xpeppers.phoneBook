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

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xpeppers.phonebook.db.DatabaseHandler;
import com.xpeppers.phonebook.model.Contact;

public class AddFragment extends Fragment {
	private EditText ed_name, ed_surname, ed_phone;
	private Button ifc;
	private final int PICK_CONTACT = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add, container,
				false);

		ed_name = (EditText) rootView.findViewById(R.id.editTextName);
		ed_surname = (EditText) rootView.findViewById(R.id.editTextSurname);
		ed_phone = (EditText) rootView.findViewById(R.id.editTextPhone);
		setHasOptionsMenu(true);
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.add, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_edit) {

			final DatabaseHandler db = new DatabaseHandler(getActivity());

			String name = ed_name.getText().toString();
			String surname = ed_surname.getText().toString();
			String phone = ed_phone.getText().toString();
			boolean phoneOk = phone.matches("\\+\\d+ \\d+ \\d{6}\\d*");
			if (!name.isEmpty() && (!surname.isEmpty()) && (phoneOk))
				db.addContact(new Contact(name, surname, phone));
			else {
				Toast.makeText(getActivity(), "Constraint!", Toast.LENGTH_SHORT)
						.show();
			}
			getActivity().onBackPressed();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStart() {
		ifc = (Button) getActivity().findViewById(R.id.buttonIFC);
		ifc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);
			}
		});
		super.onStart();
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (PICK_CONTACT):
			if (resultCode == getActivity().RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = getActivity().getContentResolver().query(
						contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String id = c.getString(c
							.getColumnIndex(ContactsContract.Contacts._ID));
					String nameSurname = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					String phoneNumber = "";
					if (Integer
							.parseInt(c.getString(c
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						// Query phone here. Covered next
						Cursor phones = getActivity()
								.getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = " + id, null, null);
						while (phones.moveToNext()) {
							phoneNumber = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

						}
						phones.close();
					}

					if (nameSurname.length() > 0) {
						if (nameSurname.contains(" ")) {
							String name = nameSurname.substring(0,
									nameSurname.indexOf(' '));
							String surname = nameSurname.substring(
									nameSurname.indexOf(' '),
									nameSurname.length());
							ed_name.setText(name);
							ed_surname.setText(surname);
						} else {
							ed_name.setText(nameSurname);
						}
					}
					if (!phoneNumber.equals(""))
						ed_phone.setText(phoneNumber);
				}
			}
			break;
		}
	}

}
