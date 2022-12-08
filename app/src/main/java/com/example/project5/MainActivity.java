package com.example.project5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.QuickContactBadge;

import java.util.ArrayList;

/**
 * activity interface for creating pizzas and purchasing them
 * @author Anirudh Chauhan, Matthew Calora
 */
public class MainActivity extends AppCompatActivity {
    private Button button, button2, button3;

    private static ArrayList<Integer> orderNumArrayList= new ArrayList<>();
    private static StoreOrders storeOrd= new StoreOrders();;
    private static Order ord = new Order();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.chicagobutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChicagoActivity();
            }
        });
        button2 = (Button) findViewById(R.id.currentButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openCurrentActivity();}
        });
        button3 = (Button) findViewById(R.id.storeButton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openStoreActivity();}
        });
    }
    /**
     * opens the chicago pizza activity interface
     */
    public void openChicagoActivity(){
        Intent intent = new Intent(this, ChicagoActivity.class);
        startActivity(intent);
    }
    /**
     * opens the current pizza activity interface
     */
    public void openCurrentActivity(){
        Intent intent = new Intent(this, CurrentOrderActivity.class);
        startActivity(intent);
    }
    /**
     * opens the store order activity interface
     */
    public void openStoreActivity(){
        Intent intent = new Intent(this, StoreOrderActivity.class);
        startActivity(intent);
    }
    /**
     * adds pizza to order
     */
    protected static void addToOrder(Pizza za){
        ord.add(za);
    }
    protected static Order getOrd(){return ord;}
    public static int orderNumber(){
        int i = 1;
        while(orderNumArrayList.contains(i)){
            i++;
        }
        orderNumArrayList.add(i);
        return i;
    }
    public static ArrayList<Integer> getOrderNumArrayList(){
        return orderNumArrayList;
    }
    /**
     * gets store order
     */
    protected static StoreOrders getStoreOrd(){
        return storeOrd;
    }
    /**
     * resets order
     */
    protected static void resetOrder(){
        ord = new Order();
    }
    /**
     * adds store order
     */
    protected static void addStoreOrd(Order or){
        storeOrd.add(or);
    }

}