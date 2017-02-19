package fr.piotr.economies.persistance;

import android.content.Context;

public class PersistanceHelper extends SQLHelper {

	private static final int			BDD_VERSION	= 3;
	private final static String			dbName		= "economies.db";

	private static PersistanceHelper	instance;

	public static PersistanceHelper getInstance(Context context) {
		if (instance != null) {
			instance.close();
		}
		instance = new PersistanceHelper(context);
		return instance;
	}

	private PersistanceHelper(Context context) {
		super(context, dbName, null, BDD_VERSION);
	}

}
