package fr.piotr.economies.persistance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import fr.piotr.economies.managers.ProcessManager;
import fr.piotr.economies.persistance.serializable.Buy;
import fr.piotr.economies.persistance.serializable.Month;
import fr.piotr.economies.persistance.serializable.Profile;

public class SQLHelper extends SQLiteOpenHelper {

	private static final String		DD_MM_YYYY				= "dd/MM/yyyy";
	private static final String		TABLE_PROFILS			= "profils";
	private static final String		COLUMN_PROFIL_ID		= "id";
	private static final String		COLUMN_PROFIL_NAME		= "nom";

	private static final String		TABLE_MOIS				= "mois";
	private static final String		COLUMN_MOIS_ID			= "id";
	private static final String		COLUMN_MOIS_PROFILE		= "profile";
	private static final String		COLUMN_MOIS_SALAIRE		= "salaire";
	private static final String		COLUMN_MOIS_LABEL		= "label";
	private static final String		COLUMN_MOIS_YEAR		= "year";
	private static final String		COLUMN_MOIS_REST		= "rest";

	private static final String		TABLE_DEPENSE			= "depense";
	private static final String		COLUMN_DEPENSE_ID		= "id";
	private static final String		COLUMN_DEPENSE_MOIS		= "id_mois";
	private static final String		COLUMN_DEPENSE_LABEL	= "label";
	private static final String		COLUMN_DEPENSE_MONTANT	= "montant";
	private static final String		COLUMN_DEPENSE_MENSUEL	= "mensuel";
	private static final String		COLUMN_DEPENSE_DATE		= "dateDep";

	private static final String		CREATE_TABLE_PROFILS	= " CREATE TABLE "
																	+ TABLE_PROFILS
																	+ " ("
																	+ COLUMN_PROFIL_ID
																	+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
																	+ COLUMN_PROFIL_NAME + " TEXT)";

	private static final String		CREATE_TABLE_MOIS		= "CREATE TABLE "
																	+ TABLE_MOIS
																	+ " ("
																	+ COLUMN_MOIS_ID
																	+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
																	+ COLUMN_MOIS_PROFILE
																	+ " INTEGER,"
																	+ COLUMN_MOIS_SALAIRE
																	+ " TEXT, " + COLUMN_MOIS_LABEL
																	+ " TEXT, " + COLUMN_MOIS_YEAR
																	+ " TEXT, " + COLUMN_MOIS_REST
																	+ " TEXT)";

	private static final String		CREATE_TABLE_DEPENSE	= " CREATE TABLE "
																	+ TABLE_DEPENSE
																	+ " ("
																	+ COLUMN_DEPENSE_ID
																	+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
																	+ COLUMN_DEPENSE_MOIS
																	+ " INTEGER,"
																	+ COLUMN_DEPENSE_LABEL
																	+ " TEXT, "
																	+ COLUMN_DEPENSE_MONTANT
																	+ " TEXT,"
																	+ COLUMN_DEPENSE_MENSUEL
																	+ " INTEGER, "
																	+ COLUMN_DEPENSE_DATE
																	+ " TEXT);";

	private final SQLiteDatabase	bdd;

	public SQLHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		bdd = getWritableDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PROFILS);
		db.execSQL(CREATE_TABLE_MOIS);
		db.execSQL(CREATE_TABLE_DEPENSE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		resetDB(db);
	}

	public void resetDB() {
		resetDB(bdd);
	}

	/**
	 * @param db
	 */
	private void resetDB(SQLiteDatabase db) {
		//
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILS + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOIS + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPENSE + ";");

		onCreate(db);
	}

	public List<Profile> getProfiles() {
		Cursor cursor = bdd.query(TABLE_PROFILS, new String[] { COLUMN_PROFIL_ID,
				COLUMN_PROFIL_NAME }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			List<Profile> profiles = new ArrayList<Profile>();
			do {
				Integer id = cursor.getInt(0);
				String name = cursor.getString(1);
				BigDecimal rest = getRestOfTheMonthForProfile(id);
				Profile profile = new Profile(id, name, rest);
				profiles.add(profile);
			} while (cursor.moveToNext());
			cursor.close();
			return profiles;
		}
		cursor.close();
		return new ArrayList<Profile>();
	}

	private BigDecimal getRestOfTheMonthForProfile(Integer id) {
		Month currentMonth = new Month(BigDecimal.ZERO);
		return getMonth(id, currentMonth.getLabel(), currentMonth.getYear()).getRest();
	}

	public void saveProfile(final String profile) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_PROFIL_NAME, profile);
		bdd.insert(TABLE_PROFILS, null, values);
	}

	public void deleteProfile(final Integer profileID) {

		List<Month> monthes = getMonthes(profileID);
		for (final Month month : monthes) {
			deleteBuys(month.getId());
		}

		deleteMonthes(profileID);
		bdd.delete(TABLE_PROFILS, COLUMN_PROFIL_ID + "=" + profileID, null);
	}

	public Month getMonth(final Integer profileId, final String label, final String year) {
		Month month = new Month();

		String[] columns = new String[] { COLUMN_MOIS_ID, COLUMN_MOIS_SALAIRE, COLUMN_MOIS_LABEL,
				COLUMN_MOIS_YEAR, COLUMN_MOIS_REST };
		Cursor cursor = bdd.query(TABLE_MOIS, columns, COLUMN_MOIS_PROFILE + " = " + profileId
				+ " AND " + COLUMN_MOIS_LABEL + " = '" + label + "' AND " + COLUMN_MOIS_YEAR
				+ " = '" + year + "'", null, null, null, null);
		if (cursor.moveToFirst()) {
			month.setId(cursor.getInt(0));
			month.setSalaire(new BigDecimal(cursor.getString(1)));
			month.setLabel(cursor.getString(2));
			month.setYear(cursor.getString(3));
			month.setRest(new BigDecimal(cursor.getString(4)));
		}
		cursor.close();
		return month;
	}

	public List<Month> getMonthes(final Integer profileId) {
		String[] columns = new String[] { COLUMN_MOIS_ID, COLUMN_MOIS_SALAIRE, COLUMN_MOIS_LABEL,
				COLUMN_MOIS_YEAR, COLUMN_MOIS_REST };
		Cursor cursor = bdd.query(TABLE_MOIS, columns, COLUMN_MOIS_PROFILE + " = " + profileId,
				null, null, null, COLUMN_MOIS_ID + " DESC");
		if (cursor.moveToFirst()) {
			List<Month> months = new ArrayList<Month>();
			do {
				Month month = new Month();
				month.setId(cursor.getInt(0));
				month.setSalaire(new BigDecimal(cursor.getString(1)));
				month.setLabel(cursor.getString(2));
				month.setYear(cursor.getString(3));
				month.setRest(new BigDecimal(cursor.getString(4)));
				months.add(month);
			} while (cursor.moveToNext());
			cursor.close();
			return months;
		}
		cursor.close();
		return new ArrayList<Month>();
	}

	public void saveMonth(final Integer profile, final Month month) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_MOIS_PROFILE, profile);
		values.put(COLUMN_MOIS_SALAIRE, month.getSalaire().toString());
		values.put(COLUMN_MOIS_LABEL, month.getLabel());
		values.put(COLUMN_MOIS_YEAR, month.getYear());
		values.put(COLUMN_MOIS_REST, month.getRest().toString());
		bdd.insert(TABLE_MOIS, null, values);
	}

	public void updateMonthRest(final Month month) {
		List<Buy> buys = getBuys(month.getId());
		month.setRest(ProcessManager.getRestant(month, buys));
		ContentValues values = new ContentValues();
		values.put(COLUMN_MOIS_REST, month.getRest().toString());
		bdd.update(TABLE_MOIS, values, COLUMN_MOIS_ID + " = " + month.getId(), null);
	}

	public void deleteMonthes(final Integer profileId) {
		bdd.delete(TABLE_MOIS, COLUMN_MOIS_PROFILE + "=" + profileId, null);
	}

	public void deleteMonth(final Integer monthId) {
		deleteBuys(monthId);
		bdd.delete(TABLE_MOIS, COLUMN_MOIS_ID + "=" + monthId, null);
	}

	public void updateSalaire(final Month month) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_MOIS_SALAIRE, month.getSalaire().toString());
		bdd.update(TABLE_MOIS, values, COLUMN_MOIS_ID + "=" + month.getId(), null);
	}

	public Buy getBuy(final Integer buyId) {

		String[] columns = new String[] { COLUMN_DEPENSE_ID, COLUMN_DEPENSE_LABEL,
				COLUMN_DEPENSE_MONTANT, COLUMN_DEPENSE_MENSUEL, COLUMN_DEPENSE_DATE };
		Cursor cursor = bdd.query(TABLE_DEPENSE, columns, COLUMN_DEPENSE_ID + "=" + buyId, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			int id = cursor.getInt(0);
			String label = cursor.getString(1);
			BigDecimal montant = new BigDecimal(cursor.getString(2));
			boolean mensuel = cursor.getInt(3) == 1 ? true : false;
			Date date = null;
			try {
				date = new SimpleDateFormat(DD_MM_YYYY).parse(cursor.getString(4));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			cursor.close();
			return new Buy(label, montant, id, mensuel, date);
		}
		cursor.close();
		return null;
	}

	public List<Buy> getBuys(final Integer moisId) {

		String[] columns = new String[] { COLUMN_DEPENSE_ID, COLUMN_DEPENSE_LABEL,
				COLUMN_DEPENSE_MONTANT, COLUMN_DEPENSE_MENSUEL, COLUMN_DEPENSE_DATE };
		Cursor cursor = bdd.query(TABLE_DEPENSE, columns, COLUMN_DEPENSE_MOIS + "=" + moisId, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			List<Buy> months = new ArrayList<Buy>();
			do {
				int id = cursor.getInt(0);
				String label = cursor.getString(1);
				BigDecimal montant = new BigDecimal(cursor.getString(2));
				boolean mensuel = cursor.getInt(3) == 1 ? true : false;
				Date date = null;
				try {
					date = new SimpleDateFormat(DD_MM_YYYY).parse(cursor.getString(4));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Buy buy = new Buy(label, montant, id, mensuel, date);
				months.add(buy);
			} while (cursor.moveToNext());
			cursor.close();
			return months;
		}
		cursor.close();
		return new ArrayList<Buy>();
	}

	public void updateBuy(Buy buy, Month month) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_DEPENSE_LABEL, buy.getLabel());
		values.put(COLUMN_DEPENSE_MONTANT, buy.getAmount().toString());
		values.put(COLUMN_DEPENSE_MENSUEL, buy.isMonthly() ? 1 : 0);
		values.put(COLUMN_DEPENSE_DATE, new SimpleDateFormat(DD_MM_YYYY).format(buy.getDate()));
		bdd.update(TABLE_DEPENSE, values, COLUMN_DEPENSE_ID + " = " + buy.getId(), null);
		updateMonthRest(month);
	}

	public void createBuy(Buy buy, Month month) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_DEPENSE_MOIS, month.getId());
		values.put(COLUMN_DEPENSE_LABEL, buy.getLabel());
		values.put(COLUMN_DEPENSE_MONTANT, buy.getAmount().toString());
		values.put(COLUMN_DEPENSE_MENSUEL, buy.isMonthly() ? 1 : 0);
		values.put(COLUMN_DEPENSE_DATE, new SimpleDateFormat(DD_MM_YYYY).format(buy.getDate()));
		bdd.insert(TABLE_DEPENSE, null, values);
		updateMonthRest(month);
	}

	public void deleteBuy(int buyId) {
		bdd.delete(TABLE_DEPENSE, COLUMN_DEPENSE_ID + "=" + buyId, null);
	}

	public void deleteBuys(Integer monthId) {
		bdd.delete(TABLE_DEPENSE, COLUMN_DEPENSE_MOIS + "=" + monthId, null);
	}

	public List<String> getDistinctsDepensesDesignations() {
		String[] columns = new String[] { COLUMN_DEPENSE_LABEL };
		Cursor cursor = bdd.query(true, TABLE_DEPENSE, columns, null, null, null, null,
				COLUMN_DEPENSE_LABEL, "50");
		if (cursor.moveToFirst()) {
			List<String> designations = new ArrayList<String>();
			do {
				designations.add(cursor.getString(0));
			} while (cursor.moveToNext());
			cursor.close();
			return designations;
		}
		cursor.close();
		return new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#close()
	 */
	@Override
	public synchronized void close() {
		super.close();
		bdd.close();
	}

}
