package com.skula.iry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.skula.iry.models.Person;
import com.skula.iry.services.DBService;

public class MainActivity extends Activity {
	public static final int STATE_NONE = 0;
	public static final int STATE_VIEW = 1;
	public static final int STATE_CREATE = 2;

	private Spinner groupSearchTxt;
	private EditText nameSearchTxt;
	private ListView personList;
	private Button btnAddPerson;
	private Button btn1;
	private Button btn2;

	private int state;

	private DBService dbService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		final MainActivity ma = this;

		this.dbService = new DBService(this);
		this.dbService.bouchon();

		this.state = STATE_NONE;

		this.groupSearchTxt = (Spinner) findViewById(R.id.groupresearch);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dbService.getGroups());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupSearchTxt.setAdapter(adapter);
		groupSearchTxt.setSelection(adapter.getPosition("Tout"));

		this.personList = (ListView) findViewById(R.id.personlist);
		updatePersonList("Tout", "");

		this.nameSearchTxt = (EditText) findViewById(R.id.nameresearch);
		nameSearchTxt.addTextChangedListener(new TextWatcher() {
			@SuppressLint("NewApi")
			public void afterTextChanged(Editable s) {
				String nameSearch = nameSearchTxt.getText().toString();
				String groupSearch = (String) groupSearchTxt.getSelectedItem();
				if (nameSearch.length() > 3) {
					updatePersonList(groupSearch, nameSearch);
				} else if (nameSearch.isEmpty()) {
					updatePersonList(groupSearch, "");
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		this.personList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				setState(STATE_VIEW);
				setBtn1("Modifier", true, View.VISIBLE);
				setBtn2("Supprimer", true, View.VISIBLE);
				Person p = (Person) personList.getItemAtPosition(position);
				fillPerson(p.getId());
			}
		});

		this.btnAddPerson = (Button) findViewById(R.id.btnAdd);
		btnAddPerson.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setState(STATE_CREATE);
				setBtn1("Confirmer", true, View.VISIBLE);
				setBtn2("Annuler", true, View.VISIBLE);
				clearForm();
			}
		});

		this.btn1 = (Button) findViewById(R.id.btn1);
		btn1.setVisibility(View.GONE);
		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switch (state) {
				case STATE_CREATE:
					dbService.insertPerson(getPerson());
					setState(STATE_NONE);
					clearForm();
					setBtn1(null, false, View.GONE);
					setBtn2(null, false, View.GONE);
					updatePersonList("Tout", "");
					break;
				case STATE_VIEW:
					
					EditText idTxt = (EditText) findViewById(R.id.id);
					final String id = idTxt.getText().toString();
					EditText lastnameTxt = (EditText) findViewById(R.id.lastname);
					String lastname = lastnameTxt.getText().toString();
					EditText firstnameTxt = (EditText) findViewById(R.id.firstname);
					String idfirtString = firstnameTxt.getText().toString();
					
					AlertDialog.Builder helpBuilder = new AlertDialog.Builder(v.getContext());
					helpBuilder.setTitle("Modification");
					helpBuilder.setMessage("Voulez-vous vraiment modifier "+lastname+" "+idfirtString+" ?");
					helpBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dbService.updatePerson(getPerson());
							fillPerson(id);
						}
					});
					helpBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
					break;
				default:
					break;
				}
			}
		});

		this.btn2 = (Button) findViewById(R.id.btn2);
		btn2.setVisibility(View.GONE);
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switch (state) {
				case STATE_CREATE:
					setState(STATE_NONE);
					clearForm();
					btn1.setVisibility(View.GONE);
					btn2.setVisibility(View.GONE);
					break;
				case STATE_VIEW:
					EditText idTxt = (EditText) findViewById(R.id.id);
					final String id = idTxt.getText().toString();
					EditText lastnameTxt = (EditText) findViewById(R.id.lastname);
					String lastname = lastnameTxt.getText().toString();
					EditText firstnameTxt = (EditText) findViewById(R.id.firstname);
					String idfirtString = firstnameTxt.getText().toString();
					
					AlertDialog.Builder helpBuilder = new AlertDialog.Builder(v.getContext());
					helpBuilder.setTitle("Suppression");
					helpBuilder.setMessage("Voulez-vous vraiment supprimer "+lastname+" "+idfirtString+" ?");
					helpBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dbService.deletePerson(id);
							clearForm();
							setBtn1(null, false, View.GONE);
							setBtn2(null, false, View.GONE);
							updatePersonList("Tout", "");
						}
					});
					helpBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					AlertDialog helpDialog = helpBuilder.create();
					helpDialog.show();
					break;
				default:
					break;
				}
			}
		});
	}

	private void updatePersonList(String group, String name) {
		Person personArray[] = null;
		if (name.equals("")) {
			personArray = dbService.getPersons(group);
		} else {
			personArray = dbService.getPersons(group, name);
		}

		PersonAdapter adapter = new PersonAdapter(this, R.layout.item_layout,
				personArray);
		personList.setAdapter(adapter);
	}

	private void fillPerson(String id) {
		Person p = dbService.getPerson(id);
		EditText idTxt = (EditText) findViewById(R.id.id);
		idTxt.setText(p.getId());
		EditText lastnameTxt = (EditText) findViewById(R.id.lastname);
		lastnameTxt.setText(p.getLastname());
		EditText firstnameTxt = (EditText) findViewById(R.id.firstname);
		firstnameTxt.setText(p.getFirstname());
		EditText birthdayTxt = (EditText) findViewById(R.id.birthday);
		birthdayTxt.setText(p.getBirthday());
		EditText addressTxt = (EditText) findViewById(R.id.address);
		addressTxt.setText(p.getAddress());
		EditText zipTxt = (EditText) findViewById(R.id.zip);
		zipTxt.setText(p.getZip());
		EditText cityTxt = (EditText) findViewById(R.id.city);
		cityTxt.setText(p.getCity());
		EditText fixnumTxt = (EditText) findViewById(R.id.fixnum);
		fixnumTxt.setText(p.getFixnum());
		EditText cellnumTxt = (EditText) findViewById(R.id.cellnum);
		cellnumTxt.setText(p.getCellnum());
		EditText emailTxt = (EditText) findViewById(R.id.email);
		emailTxt.setText(p.getEmail());
	}

	private Person getPerson() {
		Person p = new Person();
		EditText idTxt = (EditText) findViewById(R.id.id);
		p.setId(idTxt.getText().toString());
		EditText lastnameTxt = (EditText) findViewById(R.id.lastname);
		p.setLastname(lastnameTxt.getText().toString());
		EditText firstnameTxt = (EditText) findViewById(R.id.firstname);
		p.setFirstname(firstnameTxt.getText().toString());
		EditText birthdayTxt = (EditText) findViewById(R.id.birthday);
		p.setBirthday(birthdayTxt.getText().toString());
		EditText addressTxt = (EditText) findViewById(R.id.address);
		p.setAddress(addressTxt.getText().toString());
		EditText zipTxt = (EditText) findViewById(R.id.zip);
		p.setZip(zipTxt.getText().toString());
		EditText cityTxt = (EditText) findViewById(R.id.city);
		p.setCity(cityTxt.getText().toString());
		EditText fixnumTxt = (EditText) findViewById(R.id.fixnum);
		p.setFixnum(fixnumTxt.getText().toString());
		EditText cellnumTxt = (EditText) findViewById(R.id.cellnum);
		p.setCellnum(cellnumTxt.getText().toString());
		EditText emailTxt = (EditText) findViewById(R.id.email);
		p.setEmail(emailTxt.getText().toString());
		return p;
	}

	private void clearForm() {
		EditText idTxt = (EditText) findViewById(R.id.id);
		idTxt.setText("");
		EditText lastnameTxt = (EditText) findViewById(R.id.lastname);
		lastnameTxt.setText("");
		EditText firstnameTxt = (EditText) findViewById(R.id.firstname);
		firstnameTxt.setText("");
		EditText birthdayTxt = (EditText) findViewById(R.id.birthday);
		birthdayTxt.setText("");
		EditText addressTxt = (EditText) findViewById(R.id.address);
		addressTxt.setText("");
		EditText zipTxt = (EditText) findViewById(R.id.zip);
		zipTxt.setText("");
		EditText cityTxt = (EditText) findViewById(R.id.city);
		cityTxt.setText("");
		EditText fixnumTxt = (EditText) findViewById(R.id.fixnum);
		fixnumTxt.setText("");
		EditText cellnumTxt = (EditText) findViewById(R.id.cellnum);
		cellnumTxt.setText("");
		EditText emailTxt = (EditText) findViewById(R.id.email);
		emailTxt.setText("");
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setBtn1(String title, boolean enable, int visibility) {
		btn1.setEnabled(enable);
		btn1.setText(title);
		btn1.setVisibility(visibility);
	}

	public void setBtn2(String title, boolean enable, int visibility) {
		btn2.setEnabled(enable);
		btn2.setText(title);
		btn2.setVisibility(visibility);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}