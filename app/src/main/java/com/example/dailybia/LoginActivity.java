package com.example.dailybia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dailybia.model.Category;
import com.example.dailybia.model.Product;

import java.util.ArrayList;




public class LoginActivity extends AppCompatActivity {
    EditText txtUsername, txtPassword;
    Button btnLogin;

    String userstate = "stateuser";
    //TextView link_signup;
    final String DATABASE_NAME = "qlbia.sqlite";
    SQLiteDatabase database;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        addControls();
        addActions();
    }



    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getSharedPreferences(userstate, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username",txtUsername.getText().toString());
        editor.putString("password",txtPassword.getText().toString());
        editor.apply();
    }



    private void getCategory(){
        //get category
        database = Database.initDatabase(this,DATABASE_NAME);
        Data.listDataCategory = new ArrayList<Category>();

        Cursor cursor = database.rawQuery("SELECT * FROM Category", null);
        while (cursor.moveToNext()) {
            String idcategory = cursor.getString(0);
            String namecategory = cursor.getString(1);
            Category category = new Category(idcategory, namecategory);
            Data.listDataCategory.add(category);
        }
        cursor.close();
    }

    private void createCode(){
        database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM IDManager", null);
        cursor.moveToFirst();
        int idProduct = cursor.getInt(0);
        int idCategory = cursor.getInt(1);
        //Toast.makeText(LoginActivity.this, String.valueOf(idCategory),Toast.LENGTH_SHORT).show();
        cursor.close();

        Data.idProduct.setKey("SP");
        Data.idProduct.setNextId(idProduct);

        Data.idCategory.setNextId(idCategory);
        Data.idCategory.setKey("ML");
    }

    private void getProduct() {
        Data.listDataProduct = new ArrayList<Product>();

        //copy database
        database = Database.initDatabase(this,DATABASE_NAME);

        //get product
        Cursor cursor2 = database.rawQuery("SELECT * FROM Product", null);
        while (cursor2.moveToNext()) {
            String id = cursor2.getString(0);
            String name = cursor2.getString(1);
            byte[] image = cursor2.getBlob(2);
            Double price = cursor2.getDouble(3);
            Integer volumetric = cursor2.getInt(4);
            String production = cursor2.getString(5);
            String idCategory = cursor2.getString(6);

            Cursor cursor = database.rawQuery("SELECT * FROM Category Where idCategory = '" + idCategory + "'" , null);
            cursor.moveToFirst();
            String idCategory2   = cursor.getString(0);
            String nameCategory = cursor.getString(1);
            Category category = new Category(idCategory2, nameCategory);
            cursor.close();

            Product product = new Product(id,name,image,category,price,volumetric,production);
            Data.listDataProduct.add(product);
        }
        cursor2.close();
    }


    private void addControls() {
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin    = (Button)   findViewById(R.id.btnLogin);
    }

    private void addActions() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtUsername.getText().toString().trim().equals("the") && txtPassword.getText().toString().trim().equals("123")){
                    getCategory();
                    getProduct();
                    createCode();

                    Toast.makeText(LoginActivity.this,LoginActivity.this.getText(R.string.simple_login),Toast.LENGTH_LONG).show();
                    Intent intentLogin = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intentLogin);
                }else if (txtUsername.getText().length()==0 || txtPassword.getText().length()==0) {
                    Toast.makeText(LoginActivity.this,"Điền đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this,LoginActivity.this.getText(R.string.simple_loginError),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
