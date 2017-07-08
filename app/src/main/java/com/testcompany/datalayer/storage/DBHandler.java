package com.testcompany.datalayer.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.testcompany.datalayer.models.PatientDisorder;
import com.testcompany.datalayer.models.PatientDrug;
import com.testcompany.datalayer.models.PatientModel;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "patient";
    private static final String TABLE_PATIENT_DETAILS = "patient_details";
    private static final String TABLE_PATIENT_DISORDERS = "patient_disorders";
    private static final String TABLE_PATIENT_DRUGS = "patient_drugs";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_HAS_DRUG = "hasDrug";
    private static final String COLUMN_BOOKMARKED = "isBookMarked";
    private static final String COLUMN_HAS_DISORDER = "hasDisorder";
    private static final String COLUMN_HAS_UNDERAGE = "isUnderAge";
    private static final String COLUMN_DISORDER_STRING = "DisorderString";
    private static final String COLUMN_PERCENETAGE = "percentage";
    private static final String COLUMN_SEX = "sex";

    private static final String COLUMN_DATE_CREATED = "date_created";
    private static final String COLUMN_DATE_UPDATED = "date_updated";

    private static final String COLUMN_DISORDER_NAME = "disorder_name";
    private static final String COLUMN_DISORDER_SEVERITY = "disorder_severity";

    private static final String COLUMN_DRUG_NAME = "drug_name";
    private static final String COLUMN_DRUG_DOSAGE = "drug_dosage";
    private static final String COLUMN_DRUG_DATE_UPDATED = "drug_date_updated";

    private static DBHandler sInstance;

    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBHandler getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PATIENT_DETAILS + " (" +
                COLUMN_ID + " INTEGER," +
                COLUMN_NAME + " TEXT," +
                COLUMN_AGE + " INTEGER," +
                COLUMN_SEX + " TEXT," +
                COLUMN_BOOKMARKED + " TEXT," +
                COLUMN_HAS_DRUG + " TEXT," +
                COLUMN_HAS_DISORDER + " TEXT," +
                COLUMN_PERCENETAGE + " TEXT," +
                COLUMN_DISORDER_STRING + " TEXT," +
                COLUMN_HAS_UNDERAGE + " TEXT," +
                COLUMN_DATE_CREATED + " INTEGER," +
                COLUMN_DATE_UPDATED + " INTEGER" +
                ")";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_PATIENT_DISORDERS + " (" +
                COLUMN_ID + " INTEGER," +
                COLUMN_DISORDER_NAME + " TEXT," +
                COLUMN_DISORDER_SEVERITY + " TEXT" +
                ")";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_PATIENT_DRUGS + " (" +
                COLUMN_ID + " INTEGER," +
                COLUMN_DRUG_NAME + " TEXT," +
                COLUMN_DRUG_DOSAGE + " INTEGER," +
                COLUMN_DRUG_DATE_UPDATED + " INTEGER" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT_DISORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT_DRUGS);
    }

    public List<PatientDisorder> getAllDataFromDisorders(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PATIENT_DISORDERS + " Where id = '" + id + "'", null);
        List<PatientDisorder> personDataModelClasses = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                PatientDisorder patientDisorder = new PatientDisorder();
                patientDisorder.setName(cursor.getString(cursor.getColumnIndex(COLUMN_DISORDER_NAME)));
                patientDisorder.setSeverity(cursor.getString(cursor.getColumnIndex(COLUMN_DISORDER_SEVERITY)));
                personDataModelClasses.add(patientDisorder);
                cursor.moveToNext();
            }
        }
        return personDataModelClasses;
    }


    public List<PatientDrug> getAllDataFromDrugs(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PATIENT_DRUGS + " Where id = '" + id + "'", null);
        List<PatientDrug> personDataModelClasses = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                PatientDrug patientDrug = new PatientDrug();
                patientDrug.setDrug_date_updated(cursor.getInt(cursor.getColumnIndex(COLUMN_DRUG_DATE_UPDATED)));
                patientDrug.setDrug_name(cursor.getString(cursor.getColumnIndex(COLUMN_DRUG_NAME)));
                patientDrug.setDosage(cursor.getInt(cursor.getColumnIndex(COLUMN_DRUG_DOSAGE)));
                personDataModelClasses.add(patientDrug);
                cursor.moveToNext();
            }
        }
        return personDataModelClasses;
    }

    public List<PatientModel> getAllDataFromDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PATIENT_DETAILS, null);
        List<PatientModel> personDataModelClasses = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                PatientModel patientList = new PatientModel();
                patientList.setPatient_id(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                patientList.setPatient_name(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                patientList.setPatient_age(cursor.getInt(cursor.getColumnIndex(COLUMN_AGE)));
                patientList.setPatient_sex(cursor.getString(cursor.getColumnIndex(COLUMN_SEX)));
                patientList.setPatient_date_created(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_CREATED)).trim()));
                patientList.setBookmarked(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_BOOKMARKED)).trim()));
                patientList.setPatient_date_updated(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_UPDATED)).trim()));
                patientList.setHasDrug(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_HAS_DRUG)).trim()));
                patientList.setHasDisorder(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_HAS_DISORDER)).trim()));
                patientList.setUnderAge(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_HAS_UNDERAGE)).trim()));
                patientList.setDisorderString(cursor.getString(cursor.getColumnIndex(COLUMN_DISORDER_STRING)));
                patientList.setPercentage(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PERCENETAGE)).trim()));
                patientList.setPatient_disorders(getAllDataFromDisorders(patientList.getPatient_id()));
                patientList.setPatient_drugs(getAllDataFromDrugs(patientList.getPatient_id()));
                personDataModelClasses.add(patientList);

                cursor.moveToNext();
            }
        }

        return personDataModelClasses;
    }


    public boolean updateBookmarked(String bookmark, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String ids[] = new String[]{id};
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BOOKMARKED, bookmark);
        String wherecluses = COLUMN_ID + "=?";
        db.update(TABLE_PATIENT_DETAILS, contentValues, wherecluses, ids);
        return true;
    }

    public List<PatientModel> getAllDataFromBookmarkedDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PATIENT_DETAILS + " Where isBookMarked = 'true'", null);
        List<PatientModel> personDataModelClasses = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                PatientModel patientList = new PatientModel();
                patientList.setPatient_id(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                patientList.setPatient_name(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                patientList.setPatient_age(cursor.getInt(cursor.getColumnIndex(COLUMN_AGE)));
                patientList.setPatient_sex(cursor.getString(cursor.getColumnIndex(COLUMN_SEX)));
                patientList.setPatient_date_created(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_CREATED)).trim()));
                patientList.setBookmarked(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_BOOKMARKED)).trim()));
                patientList.setPatient_date_updated(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_UPDATED)).trim()));
                patientList.setHasDrug(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_HAS_DRUG)).trim()));
                patientList.setHasDisorder(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_HAS_DISORDER)).trim()));
                patientList.setUnderAge(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_HAS_UNDERAGE)).trim()));
                patientList.setDisorderString(cursor.getString(cursor.getColumnIndex(COLUMN_DISORDER_STRING)));
                patientList.setPercentage(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PERCENETAGE)).trim()));
                patientList.setPatient_disorders(getAllDataFromDisorders(patientList.getPatient_id()));
                patientList.setPatient_drugs(getAllDataFromDrugs(patientList.getPatient_id()));
                personDataModelClasses.add(patientList);

                cursor.moveToNext();
            }
        }

        return personDataModelClasses;
    }

    public void insertBulkDataInPatientList(List<PatientModel> patientLists) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();
        try {
            for (PatientModel patientList : patientLists) {

                values.put(COLUMN_ID, patientList.getPatient_id());
                values.put(COLUMN_NAME, patientList.getPatient_name());
                values.put(COLUMN_AGE, patientList.getPatient_age());
                values.put(COLUMN_SEX, patientList.getPatient_sex());
                values.put(COLUMN_DATE_CREATED, patientList.getPatient_date_created());
                values.put(COLUMN_DATE_UPDATED, patientList.getPatient_date_updated());
                values.put(COLUMN_BOOKMARKED, "false");
                values.put(COLUMN_HAS_DRUG, patientList.isHasDrug());
                values.put(COLUMN_HAS_DISORDER, patientList.isHasDisorder());
                values.put(COLUMN_HAS_UNDERAGE, patientList.isUnderAge());
                values.put(COLUMN_DISORDER_STRING, patientList.getDisorderString());
                values.put(COLUMN_PERCENETAGE, patientList.getPercentage());
                db.insertWithOnConflict(TABLE_PATIENT_DETAILS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                try {
                    if (patientList.getPatient_disorders() != null && patientList.getPatient_disorders().size() > 0) {
                        insertBulkDataInPatientDisorder(patientList.getPatient_disorders(), patientList.getPatient_id());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (patientList.getPatient_drugs() != null && patientList.getPatient_drugs().size() > 0) {
                        insertBulkDataInPatientDrug(patientList.getPatient_drugs(), patientList.getPatient_id());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void insertBulkDataInPatientDisorder(List<PatientDisorder> patientDisorders, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        try {
            for (PatientDisorder patientList : patientDisorders) {
                values.put(COLUMN_ID, id);
                values.put(COLUMN_DISORDER_NAME, patientList.getName());
                values.put(COLUMN_DISORDER_SEVERITY, patientList.getSeverity());
                db.insert(TABLE_PATIENT_DISORDERS, null, values);

            }
            db.setTransactionSuccessful();
        } catch (
                Exception e)

        {
            e.printStackTrace();
        } finally

        {
            db.endTransaction();
        }

    }

    public void insertBulkDataInPatientDrug(List<PatientDrug> patientDrugs, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();
        try {
            for (PatientDrug patientList : patientDrugs) {

                values.put(COLUMN_ID, id);
                values.put(COLUMN_DRUG_NAME, patientList.getDrug_name());
                values.put(COLUMN_DRUG_DOSAGE, patientList.getDosage());
                values.put(COLUMN_DRUG_DATE_UPDATED, patientList.getDrug_date_updated());
                db.insert(TABLE_PATIENT_DRUGS, null, values);
            }
            db.setTransactionSuccessful();
        } catch (
                Exception e)

        {
            e.printStackTrace();
        } finally

        {
            db.endTransaction();
        }

    }

}
