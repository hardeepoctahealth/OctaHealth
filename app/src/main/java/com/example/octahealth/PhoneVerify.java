package com.example.octahealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

import static maes.tech.intentanim.CustomIntent.customType;

public class PhoneVerify extends AppCompatActivity {

    EditText code1, code2, code3, code4, code5, code6;
    TextView sendingverifcationtext, resend,proceed,noverified;
    String code;
    String phone, v_id;
    int counter = 30, a = 0;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;
    CardView cardView;
    PhoneAuthProvider.ForceResendingToken resendotp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);


        progressBar=findViewById(R.id.progressBar);
        phone = getIntent().getStringExtra("phone");
        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);
        resend=findViewById(R.id.resend);
        sendingverifcationtext=findViewById(R.id.codestatus);
        constraintLayout=findViewById(R.id.cons);
        proceed=findViewById(R.id.proceed);
        cardView = findViewById(R.id.cardView3);
        noverified=findViewById(R.id.textView6);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PhoneVerify.this,Home.class);
                startActivity(intent);
                customType(PhoneVerify.this,"fadein-to-fadeout");
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                code = phoneAuthCredential.getSmsCode();

                if (code != null) {
                    code1.setText(String.valueOf(code.charAt(0)));
                    code2.setText(String.valueOf(code.charAt(1)));
                    code3.setText(String.valueOf(code.charAt(2)));
                    code4.setText(String.valueOf(code.charAt(3)));
                    code5.setText(String.valueOf(code.charAt(4)));
                    code6.setText(String.valueOf(code.charAt(5)));

                    code6.clearFocus();
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                onBackPressed();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull final PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.i("token", forceResendingToken.toString());

                v_id = s;
                resendotp = forceResendingToken;

                progressBar.setVisibility(View.GONE);
                sendingverifcationtext.setText("Verification Code Sent");

                code1.setEnabled(true);
                code2.setEnabled(true);
                code3.setEnabled(true);
                code4.setEnabled(true);
                code5.setEnabled(true);
                code6.setEnabled(true);


                if (a == 0) {
                    new CountDownTimer(30000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            resend.setText("Send Again In " + counter);
                            counter--;
                        }

                        @Override
                        public void onFinish() {
                            resend.setText("Send Again");

                            resend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    a = 1;
                                    resend.setText("Code Sent");
                                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                            "+91"+phone,        // Phone number to verify
                                            1,               // Timeout duration
                                            TimeUnit.MINUTES,   // Unit of timeout
                                            PhoneVerify.this,               // Activity (for callback binding)
                                            mCallBacks,         // OnVerificationStateChangedCallbacks
                                            resendotp);
                                }
                            });
                        }
                    }.start();
                }
            }
        };


        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1) {
                    code1.clearFocus();
                    code2.requestFocus();
                }
                if (!code1.getText().toString().equals("") && !code2.getText().toString().equals("") && !code3.getText().toString().equals("") && !code4.getText().toString().equals("") && !code5.getText().toString().equals("") && !code6.getText().toString().equals("")) {
                    signInWithPhoneAuthCredential(v_id, code1.getText().toString() + code2.getText().toString() + code3.getText().toString() + code4.getText().toString() + code5.getText().toString() + code6.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1) {
                    code2.clearFocus();
                    code3.requestFocus();
                } else {
                    code2.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                            if (keyCode == KeyEvent.KEYCODE_DEL && s.toString().length() == 0) {
                                code2.clearFocus();
                                code1.requestFocus();
                            }
                            return false;
                        }
                    });
                }
                if (!code1.getText().toString().equals("") && !code2.getText().toString().equals("") && !code3.getText().toString().equals("") && !code4.getText().toString().equals("") && !code5.getText().toString().equals("") && !code6.getText().toString().equals("")) {
                    signInWithPhoneAuthCredential(v_id, code1.getText().toString() + code2.getText().toString() + code3.getText().toString() + code4.getText().toString() + code5.getText().toString() + code6.getText().toString());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1) {
                    code3.clearFocus();
                    code4.requestFocus();
                } else {
                    code3.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                            if (keyCode == KeyEvent.KEYCODE_DEL && s.toString().length() == 0) {
                                code3.clearFocus();
                                code2.requestFocus();
                            }
                            return false;
                        }
                    });
                }
                if (!code1.getText().toString().equals("") && !code2.getText().toString().equals("") && !code3.getText().toString().equals("") && !code4.getText().toString().equals("") && !code5.getText().toString().equals("") && !code6.getText().toString().equals("")) {
                    signInWithPhoneAuthCredential(v_id, code1.getText().toString() + code2.getText().toString() + code3.getText().toString() + code4.getText().toString() + code5.getText().toString() + code6.getText().toString());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1) {
                    code4.clearFocus();
                    code5.requestFocus();
                } else {
                    code4.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                            if (keyCode == KeyEvent.KEYCODE_DEL && s.toString().length() == 0) {
                                code4.clearFocus();
                                code3.requestFocus();
                            }
                            return false;
                        }
                    });
                }
                if (!code1.getText().toString().equals("") && !code2.getText().toString().equals("") && !code3.getText().toString().equals("") && !code4.getText().toString().equals("") && !code5.getText().toString().equals("") && !code6.getText().toString().equals("")) {
                    signInWithPhoneAuthCredential(v_id, code1.getText().toString() + code2.getText().toString() + code3.getText().toString() + code4.getText().toString() + code5.getText().toString() + code6.getText().toString());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1) {
                    code5.clearFocus();
                    code6.requestFocus();
                } else {
                    code5.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                            if (keyCode == KeyEvent.KEYCODE_DEL && s.toString().length() == 0) {
                                code5.clearFocus();
                                code4.requestFocus();
                            }
                            return false;
                        }
                    });
                }
                if (!code1.getText().toString().equals("") && !code2.getText().toString().equals("") && !code3.getText().toString().equals("") && !code4.getText().toString().equals("") && !code5.getText().toString().equals("") && !code6.getText().toString().equals("")) {
                    signInWithPhoneAuthCredential(v_id, code1.getText().toString() + code2.getText().toString() + code3.getText().toString() + code4.getText().toString() + code5.getText().toString() + code6.getText().toString());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (!code1.getText().toString().equals("") && !code2.getText().toString().equals("") && !code3.getText().toString().equals("") && !code4.getText().toString().equals("") && !code5.getText().toString().equals("") && !code6.getText().toString().equals("")) {
                    signInWithPhoneAuthCredential(v_id, code1.getText().toString() + code2.getText().toString() + code3.getText().toString() + code4.getText().toString() + code5.getText().toString() + code6.getText().toString());
                }

                if (code6.getText().toString().equals("")) {
                    code6.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                            if (keyCode == KeyEvent.KEYCODE_DEL && s.toString().length() == 0) {
                                code6.clearFocus();
                                code5.requestFocus();
                            }
                            return false;
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (PhoneAuthProvider.getInstance() != null) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91"+phone,
                    30,
                    TimeUnit.SECONDS,
                    PhoneVerify.this,
                    mCallBacks);
        }
        else {
            Log.i("null","1");
        }


    }

    private void signInWithPhoneAuthCredential(String id, String c) {

        final AlertDialog alertDialog = new SpotsDialog.Builder()
                .setCancelable(false)
                .setContext(PhoneVerify.this)
                .setTheme(R.style.ProgressDialog)
                .setMessage("Verifying Phone")
                .build();

        alertDialog.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, c);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        alertDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            cardView.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.SlideInUp)
                                    .duration(1000)
                                    .playOn(proceed);

                            code1.setKeyListener(null);
                            code2.setKeyListener(null);
                            code3.setKeyListener(null);
                            code4.setKeyListener(null);
                            code5.setKeyListener(null);
                            code6.setKeyListener(null);
                            code1.clearFocus();
                            code2.clearFocus();
                            code3.clearFocus();
                            code4.clearFocus();
                            code5.clearFocus();
                            code6.clearFocus();

                            sendingverifcationtext.setText("OTP Verified");

                            noverified.setVisibility(View.GONE);
                            resend.setVisibility(View.GONE);

                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());

                            code1.setText("");
                            code2.setText("");
                            code3.setText("");
                            code4.setText("");
                            code5.setText("");
                            code6.setText("");
                            code6.clearFocus();
                            sendingverifcationtext.setText("Incorrect OTP. Please Try Again");
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);


                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}