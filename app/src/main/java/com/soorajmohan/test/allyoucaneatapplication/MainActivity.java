package com.soorajmohan.test.allyoucaneatapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//Main activity has only two buttons - one to create new order and other to view previous orders
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserOrderViewModel userOrderViewModel = ViewModelProviders.of(this).get(UserOrderViewModel.class);

        Button newOrder = findViewById(R.id.newOrder);
        Button prevOrder = findViewById(R.id.prevOrder);

        newOrder.setOnClickListener(this);
        prevOrder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.newOrder)
        {
            startActivity(new Intent(getApplicationContext(), NewOrder.class));
        }
        else if(v.getId()==R.id.prevOrder)
        {
            startActivity(new Intent(getApplicationContext(), PreviousOrder.class));
        }
    }
}