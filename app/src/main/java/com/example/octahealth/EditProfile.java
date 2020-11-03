package com.example.octahealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class EditProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    ImageView profilepic;
    EditText name;
    TextView gender, dob, save;
    Uri image=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilepic = findViewById(R.id.profilepic);
        name = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        dob = findViewById(R.id.dob);
        save = findViewById(R.id.save);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });

        final String[] listItems = {"Male", "Female", "Other"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Your Gender");

        builder.setItems(listItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gender.setText(listItems[which]);
            }
        });

        final AlertDialog dialog = builder.create();

        gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.show();
                return false;
            }
        });


        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(EditProfile.this)
                        .crop(6f, 6f)
                        .compress(1024)
                        .maxResultSize(540, 540)
                        .start();
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Glide.with(EditProfile.this).load(snapshot.child("profilepic").getValue(String.class)).into(profilepic);
                name.setText(snapshot.child("name").getValue(String.class));
                gender.setText(snapshot.child("gender").getValue(String.class));
                dob.setText(snapshot.child("dateofbirth").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().equals("")) {
                    name.setError("Enter Your Name");
                    name.requestFocus();
                }  else if (dob.getText().toString().equals("")) {
                    dob.setError("Your Date Of Birth");
                    dob.requestFocus();
                } else if (gender.getText().toString().equals("")) {
                    gender.setError("Select Your Gender");
                    gender.requestFocus();
                }else {

                    final android.app.AlertDialog alertDialog = new SpotsDialog.Builder()
                            .setCancelable(false)
                            .setContext(EditProfile.this)
                            .setTheme(R.style.ProgressDialog)
                            .setMessage("Saving Your Details")
                            .build();

                    alertDialog.show();

                    if(image==null) {
                        Map map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put(gender, gender.getText().toString());
                        map.put("dateofbirth", dob.getText().toString());


                            FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    alertDialog.dismiss();
                                    if (task.isSuccessful()) {

                                        Toast.makeText(EditProfile.this, "Changes Saved", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(EditProfile.this, "Some Error Occured. Please Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                    }
                    else {
                        {
                            final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Profile Pictures").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            UploadTask uploadTask;
                            uploadTask = ref.putFile(image);

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    // Continue with the task to get the download URL
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful())
                                    {
                                        Map det=new HashMap();
                                        det.clear();
                                        det.put("name", name.getText().toString());
                                        det.put("gender", gender.getText().toString());
                                        det.put("dateofbirth",dob.getText().toString());
                                        det.put("profilepic",task.getResult().toString());

                                        FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(det).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    alertDialog.dismiss();
                                                    finish();
                                                    Toast.makeText(EditProfile.this, "Changes Saved", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    alertDialog.dismiss();
                                                    Toast.makeText(EditProfile.this, "Some Error Occured. Please Try Again", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    else {
                                        Toast.makeText(EditProfile.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }


                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {
                image = data.getData();
                profilepic.setImageURI(image);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DAY_OF_MONTH, i2);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dob.setText(currentDateString);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}