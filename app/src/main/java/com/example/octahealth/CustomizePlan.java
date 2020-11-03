package com.example.octahealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomizePlan extends AppCompatActivity implements PaymentResultListener {

    LinearLayout health,insurance;
    String plan="";
    int amount;
    RecyclerView recyclerView,recyclerView2;
    TextView total,buynow,selecteditems;
    ArrayList<Option> healthoptions;
    ArrayList<Option> insuranceoptions;
    ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_plan);

        health=findViewById(R.id.linearLayout5);
        insurance=findViewById(R.id.linearLayout6);
        recyclerView=findViewById(R.id.optionsrecyview);
        recyclerView2=findViewById(R.id.options2recyview);
        total=findViewById(R.id.amount);
        buynow=findViewById(R.id.buynow);
        items=new ArrayList<>();
        selecteditems=findViewById(R.id.items);

        Checkout.preload(getApplicationContext());
        healthoptions=new ArrayList<>();
        insuranceoptions=new ArrayList<>();

        healthoptions.add(new Option("Doctor Consultation","10"));

        insuranceoptions.add(new Option("Insurance","15"));

        recyclerView.setLayoutManager(new LinearLayoutManager(CustomizePlan.this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(CustomizePlan.this));

        recyclerView.setAdapter(new OptionsAdapter(healthoptions));
        recyclerView2.setAdapter(new OptionsAdapter(insuranceoptions));

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               recyclerView.setVisibility(View.VISIBLE);
               recyclerView2.setVisibility(View.GONE);
                health.setBackgroundColor(getColor(R.color.selectioncolor));
                insurance.setBackgroundColor(getColor(R.color.white));

            }
        });

        insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
                insurance.setBackgroundColor(getColor(R.color.selectioncolor));
                health.setBackgroundColor(getColor(R.color.white));

            }
        });


        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Checkout checkout = new Checkout();

                try {
                    JSONObject options = new JSONObject();

                    options.put("name", "Merchant Name");
                    options.put("currency", "INR");
                    options.put("prefill.email", "email@mail.com");
                    options.put("prefill.contact", "8219583372");
                    options.put("amount", amount+"00");//pass amount in currency subunits
                    checkout.open(CustomizePlan.this, options);


                } catch (Exception e) {
                    Log.e("TAG", "Error in starting Razorpay Checkout", e);
                }

            }
        });


    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(CustomizePlan.this,PaymentInfo.class);
        intent.putExtra("amount",String.valueOf(amount));
        intent.putExtra("planttype","Customized Plan");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();

    }

    class OptionsAdapter extends RecyclerView.Adapter<OptionsViewHolder>
    {

        ArrayList<Option> options;

        public OptionsAdapter(ArrayList<Option> options) {
            this.options = options;
        }

        @NonNull
        @Override
        public OptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.optionsitem,parent,false);
            return new OptionsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OptionsViewHolder holder, final int position) {

            holder.title.setText(options.get(position).getTitle());

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(compoundButton.isChecked())
                    {
                        items.add(options.get(position).getTitle());
                        amount=amount+Integer.valueOf(options.get(position).getCost());
                        total.setText("₹ "+amount);
                    }
                    else {
                        items.remove(options.get(position).getTitle());
                        amount=amount-Integer.valueOf(options.get(position).getCost());
                        total.setText("₹ "+amount);
                    }

                    String s="";
                    for(int i=0;i<items.size();i++)
                    {
                       if(i==0)
                       {
                           s=s+items.get(i);
                       }
                       else {
                           s=s+","+items.get(i);
                       }
                    }
                    selecteditems.setText(s);
                }
            });

        }

        @Override
        public int getItemCount() {
            return options.size();
        }
    }


    class OptionsViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        CheckBox checkBox;
        public OptionsViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            checkBox=itemView.findViewById(R.id.checkbox);
        }
    }
}