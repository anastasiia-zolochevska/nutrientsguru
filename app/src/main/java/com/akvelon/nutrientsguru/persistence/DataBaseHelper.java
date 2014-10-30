package com.akvelon.nutrientsguru.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;


//singleton
public class DataBaseHelper extends SQLiteOpenHelper {

    public static String DB_PATH = "/data/data/com.akvelon.nutrientsguru/databases/";
    public static String DB_NAME = "usda4";

    public static final String C_NUTRIENT_NAME = "NutrientName";
    public static final String C_NUTRIENT_GROUP = "NutrientGroup";
    public static final String C_NUTRIENT_GROUP_ID = "NutrientGroupId";
    public static final String C_NUTRIENT_VALUE = "NutrientValue";
    public static final String C_NUTRIENT_UNITS = "NutrientUnits";
    public static final String C_NUTRIENT_ID = "NutrientId";

    public static final String C_PRODUCT_NAME = "ProductName";
    public static final String C_PRODUCT_ID = "_id";
    public static final String C_PRODUCT_GROUP = "ProductGroup";
    public static final String C_PRODUCT_CARBOHYDRATE_FACTOR = "CarbohydrateFactor";
    public static final String C_PRODUCT_FAT_FACTOR = "FatFactor";
    public static final String C_PRODUCT_PROTEIN_FACTOR = "ProteinFactor";
    public static final String C_PRODUCT_NITROGEN_TO_PROTEIN_FACTOR = "NitrogenToProteinFactor";

    public static final String C_MEASUREMENT_DESC = "MeasurementDesc";
    public static final String C_MEASUREMENT_WEIGHT = "Weight";


    private static DataBaseHelper instance;

    private SQLiteDatabase database;

    private final Context myContext;




    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        try {
            createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        openDataBase();
    }

    public static DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context);

        }
        return instance;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (database != null)
            database.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public Cursor filterProducts(String query) {
        return database.rawQuery("select desc._id as "+C_PRODUCT_ID+", desc.Long_Desc as "+C_PRODUCT_NAME+
                ", gr.FdGrp_Desc as "+C_PRODUCT_GROUP+ " from food_description desc" +
                " inner join food_groups gr on gr._id=desc.FdGrp_Cd where Long_Desc like '%"+query+"%'", null);
    }

    public Cursor getAllProducts() {
        return database.rawQuery("select desc._id as "+C_PRODUCT_ID+", desc.Long_Desc as "+C_PRODUCT_NAME+
                ", gr.FdGrp_Desc as "+C_PRODUCT_GROUP+ " from food_description desc" +
                " inner join food_groups gr on gr._id=desc.FdGrp_Cd", null);
    }

    public Cursor getProductsNutrientValues(Collection<Long> productIds) {
        String productIdsString = PersistenceUtils.transformListToCommaSeparatedString(productIds);
        return database.rawQuery("select data.Nutr_No as " + C_NUTRIENT_ID+
                ", data.Nutr_Val as " +C_NUTRIENT_VALUE+
                ", data.NDB_No as " +C_PRODUCT_ID+
                " from nutrition_data data"+
                " where data.NDB_No in "+productIdsString, null);
    }

    public Cursor getProductNutrientValue(Long productId, Long nutrientId) {
        return database.rawQuery("select data.Nutr_Val as " +C_NUTRIENT_VALUE+
                " from nutrition_data data"+
                " where data.NDB_No = "+productId+" and data.Nutr_No = "+nutrientId, null);
    }


    public Cursor getNutrients() {

        return database.rawQuery("select def.NutrDesc as " + C_NUTRIENT_NAME+
                ", def._id as " + C_NUTRIENT_ID+
                ", gr.name as " +C_NUTRIENT_GROUP+
                ", gr._id as " +C_NUTRIENT_GROUP_ID+
                ", def.Units as " +C_NUTRIENT_UNITS+
                " from nutrition_definition def "+
                " inner join nutrient_group gr on gr._id=def.group_id", null);
    }

    public Cursor getMeasurements(Long productId) {
        return database.rawQuery("select Msre_Desc as "+C_MEASUREMENT_DESC+
                ", Gm_Wgt as "+C_MEASUREMENT_WEIGHT+
                " from weight where NDB_No = "+productId, null);
    }


    public Cursor getProducts(List<Long> productIds) {
        String productIdsString = PersistenceUtils.transformListToCommaSeparatedString(productIds);
        return database.rawQuery("select desc._id as "+C_PRODUCT_ID+
                ", desc.Long_Desc as "+C_PRODUCT_NAME+
                ", desc.Fat_Factor as "+C_PRODUCT_FAT_FACTOR+
                ", desc.Pro_Factor as "+C_PRODUCT_PROTEIN_FACTOR+
                ", desc.CHO_Factor as "+C_PRODUCT_CARBOHYDRATE_FACTOR+
                ", desc.N_Factor as "+C_PRODUCT_NITROGEN_TO_PROTEIN_FACTOR+
                " from food_description desc" +
                " where desc._id in "+productIdsString, null);

    }
}