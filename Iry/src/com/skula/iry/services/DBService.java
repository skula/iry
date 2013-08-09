package com.skula.iry.services;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.skula.iry.models.Person;

public class DBService {
	private static final String DATABASE_NAME = "iry.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME_PERSON = "persons";
	private static final String TABLE_NAME_GROUP = "groups";

	private Context context;
	private SQLiteDatabase database;
	private SQLiteStatement statement;

	public DBService(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.database = openHelper.getWritableDatabase();
	}

	public void bouchon() {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PERSON);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUP);
		database.execSQL("CREATE TABLE "
				+ TABLE_NAME_PERSON
				+ "(id INTEGER PRIMARY KEY, lastname TEXT, firstname TEXT, birthday DATE, address TEXT, zip TEXT, city TEXT, fixnum TEXT, cellnum TEXT, email TEXT, groupid INTEGER)");
		database.execSQL("CREATE TABLE " + TABLE_NAME_GROUP
				+ "(id INTEGER PRIMARY KEY, label TEXT)");
		
		insertGroup("Tout");
		insertGroup("Amis");
		insertGroup("Famille");
		insertGroup("Collegues");
		
		insertPerson(new Person("1", "KULA", "Stéphane", "02-04-1988", "23 rue de la foret", "68400", "RIEDISHEIM", "0389541975", "0685544042", "stephane.kula@gmail.com", "Amis"));
		insertPerson(new Person("2", "REIMINGER", "Marie", "02-04-1988", "23 rue de la foret", "68400", "RIEDISHEIM", "0389541975", "0685544042", "stephane.kula@gmail.com", "Amis"));
	}

	public void insertPerson(Person person) {
		String sql = "insert into "
				+ TABLE_NAME_PERSON
				+ "(id, lastname, firstname, birthday, address, zip, city, fixnum, cellnum, email, groupid) values (?,?,?,?,?,?,?,?,?,?,?)";
		statement = database.compileStatement(sql);
		statement.bindLong(1, getNextPersonId());
		statement.bindString(2, person.getLastname());
		statement.bindString(3, person.getFirstname());
		statement.bindString(4, person.getBirthday());
		statement.bindString(5, person.getAddress());
		statement.bindString(6, person.getZip());
		statement.bindString(7, person.getCity());
		statement.bindString(8, person.getFixnum());
		statement.bindString(9, person.getCellnum());
		statement.bindString(10, person.getEmail());
		statement.bindLong(11, getGroupId(person.getGroup()));
		statement.executeInsert();
	}

	public void updatePerson(Person newPers) {
		ContentValues args = new ContentValues();
		args.put("lastname", newPers.getLastname());
		args.put("firstname", newPers.getFirstname());
		args.put("birthday", newPers.getBirthday());
		args.put("address", newPers.getAddress());
		args.put("zip", newPers.getZip());
		args.put("city", newPers.getCity());
		args.put("fixnum", newPers.getFixnum());
		args.put("cellnum", newPers.getCellnum());
		args.put("email", newPers.getEmail());
		args.put("groupid", getGroupId(newPers.getGroup()));
		database.update(TABLE_NAME_PERSON, args, "id=" + newPers.getId(), null);
	}

	public void deletePerson(String id) {
		database.delete(TABLE_NAME_PERSON, "id=" + id, null);
	}

	public Person getPerson(String id) {
		Cursor cursor = database.query(TABLE_NAME_PERSON, new String[] {
				"lastname", "firstname", "birthday", "address", "zip", "city",
				"fixnum", "cellnum", "email", "groupid" }, "id=" + id, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Person p = new Person();
				p.setId(id);
				p.setLastname(cursor.getString(0));
				p.setFirstname(cursor.getString(1));
				p.setBirthday(cursor.getString(2));
				p.setAddress(cursor.getString(3));
				p.setCity(cursor.getString(5));
				p.setZip(cursor.getString(4));
				p.setEmail(cursor.getString(8));
				p.setFixnum(cursor.getString(6));
				p.setCellnum(cursor.getString(7));
				p.setGroup(getGroupLabel(cursor.getInt(9)));
				return p;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return null;
	}

	public int getNextPersonId() {
		Cursor cursor = database.query(TABLE_NAME_PERSON,
				new String[] { "max(id)" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				return cursor.getInt(0) + 1;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return 1;
	}

	public boolean existPerson(String lastname, String firstname) {
		Cursor cursor = database.query(TABLE_NAME_PERSON,
				new String[] { "id" }, "lastname='" + lastname
						+ "' and firstname='" + firstname + "'", null, null,
				null, null);
		if (cursor.moveToFirst()) {
			return true;
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return false;
	}

	public Person[] getPersons(String group) {
		List<Person> list = new ArrayList<Person>();
		Cursor cursor = null;

		if (group.equals("Tout")) {
			cursor = database.query(TABLE_NAME_PERSON, new String[] { "id",
					"lastname", "firstname" }, null, null, null, null,
					"lastname, firstname asc");
		} else {
			cursor = database.query(TABLE_NAME_PERSON, new String[] { "id",
					"lastname", "firstname" }, "groupid=" + getGroupId(group),
					null, null, null, "lastname, firstname asc");
		}
		if (cursor.moveToFirst()) {
			do {
				Person p = new Person();
				p.setId(cursor.getString(0));
				p.setLastname(cursor.getString(1));
				p.setFirstname(cursor.getString(2));
				list.add(p);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return (Person[]) list.toArray(new Person[list.size()]);
	}

	public Person[] getPersons(String group, String research) {
		List<Person> list = new ArrayList<Person>();
		String req = null;
		if (group.equals("Tout")) {
			req = "select id, lastname, firstname from "+ TABLE_NAME_PERSON +" where lastname like '%"
					+ research + "%' or firstname like '%" + research
					+ "%' order by lastname, firstname asc";
		} else {
			req = "select id, lastname, firstname from "+ TABLE_NAME_PERSON +" where (lastname like '%"
					+ research + "%' or firstname like '%" + research
					+ "%') and groupid=" + getGroupId(group)
					+ " order by lastname, firstname asc";
		}
		Cursor cursor= null;
		try{
			cursor = database.rawQuery(req, null);
		} catch(Exception e){
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				Person p = new Person();
				p.setId(cursor.getString(0));
				p.setLastname(cursor.getString(1));
				p.setFirstname(cursor.getString(2));
				list.add(p);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return (Person[]) list.toArray(new Person[list.size()]);
	}

	public void insertGroup(String label) {
		String sql = "insert into " + TABLE_NAME_GROUP
				+ "(id, label) values (?,?)";
		statement = database.compileStatement(sql);
		statement.bindLong(1, getNextGroupId());
		statement.bindString(2, label);
		statement.executeInsert();
	}

	public void updateGroup(String id, String label) {
		ContentValues args = new ContentValues();
		args.put("label", label);
		database.update(TABLE_NAME_GROUP, args, "id=" + id, null);
	}

	public int getNextGroupId() {
		Cursor cursor = database.query(TABLE_NAME_GROUP,
				new String[] { "max(id)" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				return cursor.getInt(0) + 1;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return 1;
	}

	public boolean existGroup(String label) {
		Cursor cursor = database.query(TABLE_NAME_GROUP, new String[] { "id" },
				"label='" + label + "'", null, null, null, null);
		if (cursor.moveToFirst()) {
			return true;
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return false;
	}

	public void deleteGroup(String id) {
		database.delete(TABLE_NAME_GROUP, "id=" + id, null);
	}

	public List<String> getGroups() {
		List<String> list = new ArrayList<String>();
		Cursor cursor = database.query(TABLE_NAME_GROUP,
				new String[] { "label" }, null, null, null, null, "label asc");
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public String getGroupLabel(int id) {
		Cursor cursor = database.query(TABLE_NAME_GROUP,
				new String[] { "label" }, "id=" + id, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				return cursor.getString(0);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return null;
	}

	public int getGroupId(String label) {
		Cursor cursor = database.query(TABLE_NAME_GROUP, new String[] { "id" },
				"label='" + label + "'", null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				return cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return -1;
	}

	private static class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "
					+ TABLE_NAME_PERSON
					+ "(id INTEGER PRIMARY KEY, lastname TEXT, firstname TEXT, birthday DATE, address TEXT, zip TEXT, city TEXT, fixnum TEXT, cellnum TEXT, email TEXT, groupid INTEGER)");
			db.execSQL("CREATE TABLE " + TABLE_NAME_GROUP
					+ "(id INTEGER PRIMARY KEY, label TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PERSON);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUP);

			db.execSQL("CREATE TABLE "
					+ TABLE_NAME_PERSON
					+ "(id INTEGER PRIMARY KEY, lastname TEXT, firstname TEXT, birthday DATE, address TEXT, zip TEXT, city TEXT, fixnum TEXT, cellnum TEXT, email TEXT, groupid INTEGER)");
			db.execSQL("CREATE TABLE " + TABLE_NAME_GROUP
					+ "(id INTEGER PRIMARY KEY, label TEXT)");
			onCreate(db);
		}
	}
}
