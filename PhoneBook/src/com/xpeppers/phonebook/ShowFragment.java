package com.xpeppers.phonebook;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.xpeppers.phonebook.model.Contact;

public class ShowFragment extends Fragment {
	private EditText ed_name, ed_surname, ed_phone;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_show, container,
				false);
		setHasOptionsMenu(true);
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.show, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_edit) {
			Bundle args = getArguments();
			Contact c = (Contact) args.getSerializable("CONTACT");

			args.putSerializable("CONTACT", c);
			Fragment edit = new EditFragment();
			edit.setArguments(args);
			getFragmentManager().beginTransaction()
					.replace(R.id.container, edit, "EDIT")
					.addToBackStack("EDIT").commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStart() {
		Bundle args = getArguments();
		Contact c = (Contact) args.getSerializable("CONTACT");
		View v = this.getView();
		ed_name = (EditText) v.findViewById(R.id.editTextName);
		ed_surname = (EditText) v.findViewById(R.id.editTextSurname);
		ed_phone = (EditText) v.findViewById(R.id.editTextPhone);

		ed_name.setText(c.getName());
		ed_surname.setText(c.getSurname());
		ed_phone.setText(c.getPhoneNumber());
		super.onStart();
	}

}
