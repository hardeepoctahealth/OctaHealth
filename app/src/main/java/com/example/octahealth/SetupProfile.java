package com.example.octahealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SetupProfile extends AppCompatActivity {


    TextView name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        /*GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SetupProfile.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            if(personEmail!=null)
            {
                name.setText(personName);
            }
        }*/

        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);


        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    1, // e.g. 1
                    account,
                    fitnessOptions);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            long endTime = cal.getTimeInMillis();
            cal.add(Calendar.YEAR, -1);
            long startTime = cal.getTimeInMillis();

            DataReadRequest readRequest = new DataReadRequest.Builder()
                    .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .bucketByTime(365, TimeUnit.DAYS)
                    .build();

            DataReadRequest readRequest2 = new DataReadRequest.Builder()
                    .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .bucketByTime(365, TimeUnit.DAYS)
                    .build();


            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy , hh:mm aaa");

            Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                    .readData(readRequest)
                    .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                        @Override
                        public void onSuccess(DataReadResponse dataReadResponse) {
                            for (Bucket bucket : dataReadResponse.getBuckets()) {
                                for (DataSet dataSet : bucket.getDataSets()) {

                                    for (DataPoint dp : dataSet.getDataPoints()) {
                                        Log.i("TAG", "Data point:");
                                        Log.i("TAG", "\tType: " + dp.getDataType().getName());
                                        for (Field field : dp.getDataType().getFields()) {
                                            Log.i("TAG", "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                                        }
                                        Log.i("TAG", "\tStart: " + simpleDateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                                        Log.i("TAG", "\tEnd: " + simpleDateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                                    }
                                    Log.i("STEP", dataSet.toString());
                                }

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

            Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                    .readData(readRequest2)
                    .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                        @Override
                        public void onSuccess(DataReadResponse dataReadResponse) {

                            for (Bucket bucket : dataReadResponse.getBuckets()) {
                                for (DataSet dataSet : bucket.getDataSets()) {

                                    for (DataPoint dp : dataSet.getDataPoints()) {
                                        Log.i("TAG", "Data point:");
                                        Log.i("TAG", "\tType: " + dp.getDataType().getName());
                                        Log.i("TAG", "\tStart: " + simpleDateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                                        Log.i("TAG", "\tEnd: " + simpleDateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                                    }
                                    Log.i("DISTANCE", dataSet.toString());
                                }

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("st","Failed");
                        }
                    });



        }
    }
}