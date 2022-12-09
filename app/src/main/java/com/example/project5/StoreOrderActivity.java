package com.example.project5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * activity interface for the store orders
 * @author Anirudh Chauhan, Matthew Calora
 */
public class StoreOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private StoreOrders storeOrders;
    private ArrayList<String> pizzaList;
    private ArrayList<String> orderList;
    private ListView storderListView;
    private TextView orderPriceView;
    private static final int STORE_ORDER_NUMBER = 1;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter,arrayAdapter;
    private Button backButton, cancelButton, exportButton;
    private int orderNumber;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * initializes the activity, and initializes all the items used later on
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order);
        storeOrders = MainActivity.getStoreOrd();
        storderListView = findViewById(R.id.storderListView);
        pizzaList = new ArrayList<String>();
        orderList = new ArrayList<String>();
        storderListView = (ListView)findViewById(R.id.storderListView);
        storderListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        createOrderList();
        checkIfStorderNull();
        arrayAdapter = new ArrayAdapter<>(StoreOrderActivity.this,android.R.layout.simple_list_item_single_choice, pizzaList);
        storderListView.setAdapter(arrayAdapter);
        orderPriceView = findViewById(R.id.orderPriceTextView);
        buildSpinner();
        buildButtons();
        storeOrders = MainActivity.getStoreOrd();
    }

    /**
     * creates a string arraylist to hold pizza information
     * @param order
     */
    public void createPizzaList(Order order) {
        if(order != null) {
            for(Pizza za : order.getOrders()) {
                pizzaList.add(toString(za));
            }
        }
    }

    /**
     * builds the spinner to use in the create method
     */
    public void buildSpinner(){
        spinner = findViewById(R.id.orderSpinner);
        spinnerAdapter = new ArrayAdapter<>(StoreOrderActivity.this, android.R.layout.simple_spinner_item, orderList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * checks if store order is null, if not, creates the pizza list using the order
     */
    public void checkIfStorderNull() {
        if(storeOrders.getStoreOrders() != null) {
            Order order = null;
            for(Order ord : storeOrders.getStoreOrders()) {
                if(ord.getOrderSerial() == MainActivity.getOrderNumArrayList().get(0)) {order = ord;}
            }
            if(order != null) {createPizzaList(order);}
        }
    }

    /**
     * creates an arraylist of strings holding orders
     */
    public void createOrderList() {
        for(Integer counter : MainActivity.getOrderNumArrayList()) {
            orderList.add(Integer.toString(counter));
        }
        if(!(MainActivity.getOrderNumArrayList().size() <= STORE_ORDER_NUMBER)) {
            int numberOrders = MainActivity.getOrderNumArrayList().size() - 1;
            orderList.remove(numberOrders);
        }
    }

    /**
     * builds the buttons used in the activity
     */
    public void buildButtons(){
        backButton = (Button) findViewById(R.id.backMainButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {backButton();}
        });
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {cancelOrder();}
        });
    }

    /**
     * method used to go back to the main page
     */
    public void backButton(){openMainActivity();}
    /**
     * opens the main activity again
     */
    public void openMainActivity(){
        Intent intent = new Intent(StoreOrderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Makes the diplayed string for the pizzas
     * @param pia
     * @return
     */
    public String toString(Pizza pia) {
        String str;
        String type="";
        if(pia instanceof Deluxe){type = "Deluxe; ";}
        if(pia instanceof BuildYourOwn){type = "Build Your Own; ";}
        if(pia instanceof BBQChicken){type = "BBQChicken; ";}
        if(pia instanceof Meatzza){type = "Meatzza; ";}
        if (pia.getCrust().equals(Crust.valueOf("DEEPDISH")) || pia.getCrust().equals(Crust.valueOf("PAN")) || pia.getCrust().equals(Crust.valueOf("STUFFED"))){
            str = "" + type + "Pizza Style: CHICAGO"  + "; Crust: " + pia.getCrust()+ "; Toppings: " + pia.printArrayList() + "; Price: $" + pia.price() + "";
            return str;
        }
        else{
            str = "" + type + "Pizza Style: NEW YORK"  + "; Crust: " + pia.getCrust()+ "; Toppings: " + pia.printArrayList() + "; Price: $" + pia.price() + "";
            return (str);
        }
    }

    /**
     * changes the order when an item on the spinner is selected
     * @param parent
     * @param view
     * @param position
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        try {
            orderNumber = Integer.parseInt(parent.getItemAtPosition(position).toString());
            showOrder();
        }
        catch(Exception e) {
            pizzaList.clear();
            arrayAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * method to make sure the order is not null when displaying it
     */
    public void showOrder(){
        Order order = null;
        for (Order ord : storeOrders.getStoreOrders()) {
            if (ord.getOrderSerial() == orderNumber) {order = ord;}
        }
        if (order != null) {
            pizzaList.clear();
            createPizzaList(order);
            arrayAdapter.notifyDataSetChanged();
            orderPriceView.setText("$" + df.format(order.getTaxPrice()));
        }
    }

    /**
     * method used to cancel the order
     */
    public void cancelOrder(){
        try {
            Order order = null;
            for(Order ord : storeOrders.getStoreOrders()) {
                if(ord.getOrderSerial() == orderNumber) {order = ord;}
            }
            if(storeOrders.remove(order)) {
                int removei = MainActivity.getOrderNumArrayList().indexOf(orderNumber);
                orderList.remove(removei);
                spinnerAdapter.notifyDataSetChanged();
                MainActivity.getOrderNumArrayList().remove(Integer.valueOf(orderNumber));
                if(MainActivity.getOrderNumArrayList().size() == STORE_ORDER_NUMBER) {
                    pizzaList.clear();
                    arrayAdapter.notifyDataSetChanged();
                    orderPriceView.setText("$0.00");
                }
                orderNumber = Integer.parseInt(spinner.getSelectedItem().toString());
                showOrder();
            }
            else {
                Toast.makeText(StoreOrderActivity.this, "Nothing to cancel", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        catch(Exception e) {
            pizzaList.clear();
            arrayAdapter.notifyDataSetChanged();
            orderPriceView.setText("$0.00");
        }
    }
}