package com.example.android.firebasemyprojectsavedata;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.firebasemyprojectsavedata.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button buttonLogout;

    //for Firebase Database
    private DatabaseReference databaseReference;
    //***Table for
    private TextView textViewTable;
    private EditText editTextName, editTextPhone, editTextReq;
    //***add spinner for table & time
    private Spinner spinnerTables;
    private Spinner spinnerTime;

    //***add Calendar
    private Intent currentIntent;
    private EditText editTextDate;
    final Calendar c = Calendar.getInstance();

    private Button buttonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        String auth = firebaseAuth.getCurrentUser().getUid();
        if(auth == null) {
            //if(firebaseAuth.getCurrentUser() == null) {
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

        //initialize database objects
        //databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference = FirebaseDatabase.getInstance().getReference("user-"+auth);
        //***Table for
        textViewTable = (TextView) findViewById(R.id.textViewTable);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextReq = (EditText) findViewById(R.id.editTextReq);
        //***add spinner for table & time
        //spinnerTables = (Spinner)findViewById(R.id.spinnerTables);
        spinnerTables = (Spinner)findViewById(R.id.spinnerTables);
        spinnerTime = (Spinner)findViewById(R.id.spinnerTime);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        //***Show current date
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(mClickListener);
        currentIntent = getIntent();
        Bundle extras = currentIntent.getExtras();
        if(extras == null)
            setCurrentDateOnView();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initialize views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //display logged in user name
        textViewUserEmail.setText("Welcome "+user.getEmail());  //override Large Text in profile layout

        //add listener to button
        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    private void saveUserInformation() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String request = editTextReq.getText().toString().trim();
        //***add spinner for table & time
        //String table = spinnerTables.getSelectedItem().toString().trim();
        //int table = Integer.parseInt(spinnerTables.getSelectedItem().toString().trim());
        //int table = (Integer)spinnerTables.getSelectedItem();
        String table = spinnerTables.getSelectedItem().toString().trim();
        String time = spinnerTime.getSelectedItem().toString().trim();
        String date = editTextDate.getText().toString().trim();
        UserInformation userInformation = new UserInformation(name, phone,request,table,date,time);

        //FirebaseUser user = firebaseAuth.getCurrentUser();
        //FirebaseUser user = new firebaseAuth.getCurrentUser();
        //databaseReference.child(user.getUid()).setValue(userInformation);
        String id = databaseReference.push().getKey();
        databaseReference.child(id).setValue(userInformation);
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();


    }

    /*DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void OnDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH,monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setCurrentDateOnView();
        }
    };*/

   // public void dateOnClick(View view) {
    //    new DatePickerDialog(this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    //}
    public void setCurrentDateOnView() {
        String dateFormat = "MM-dd-yyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editTextDate.setText(sdf.format(c.getTime()));
    }

    View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateDialog();
        }
    };

    public void DateDialog(){
        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH,monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setCurrentDateOnView();
            }};
            new DatePickerDialog(ProfileActivity.this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

            //DatePickerDialog dpDialog=new DatePickerDialog(this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            //dpDialog.show();
        //setCurrentDateOnView();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, com.example.android.firebasemyprojectsavedata.LoginActivity.class));
        }

        if(view == buttonSave){
            saveUserInformation();
        }

        if(view == editTextDate){
            //DateDialog();
        }
    }
}