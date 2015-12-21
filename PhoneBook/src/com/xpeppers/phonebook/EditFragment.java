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
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.xpeppers.phonebook.MainActivity.PlaceholderFragment;
import com.xpeppers.phonebook.db.DatabaseHandler;
import com.xpeppers.phonebook.model.Contact;

public class EditFragment extends Fragment {
	private EditText ed_name, ed_surname, ed_phone;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edit, container,
				false);
		setHasOptionsMenu(true);
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.edit, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_edit) {
			View v = this.getView();
			final DatabaseHandler db = new DatabaseHandler(getActivity());
			ed_name = (EditText) v.findViewById(R.id.editTextName);
			ed_surname = (EditText) v.findViewById(R.id.editTextSurname);
			ed_phone = (EditText) v.findViewById(R.id.editTextPhone);
			String name = ed_name.getText().toString();
			String surname = ed_surname.getText().toString();
			String phone = ed_phone.getText().toString();
			boolean phoneOk = phone.matches("\\+\\d+ \\d+ \\d{6}\\d*");
			if (!name.isEmpty() && (!surname.isEmpty()) && (phoneOk)) {
				Bundle args = getArguments();
				Contact c = (Contact) args.getSerializable("CONTACT");
				db.deleteContact(c);
				db.addContact(new Contact(name, surname, phone));
			} else {
				Toast.makeText(getActivity(), "Constraint!", Toast.LENGTH_SHORT)
						.show();
			}
			FragmentManager fm = getActivity().getFragmentManager();
			for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
				fm.popBackStack();
			}
			getFragmentManager().beginTransaction()
					.replace(R.id.container, new PlaceholderFragment())
					.commit();
		}

		return super.onOptionsItemSelected(item);
	}
}
