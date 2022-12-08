package com.example.project5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CurrentOrderActivity extends AppCompatActivity {
    private ListView orderList;
    private Order order;
    private ArrayList<String> pizzaList;
    private Button backButton, removePButton, removeAButton, placeButton;
    private TextView orderNumberText, totalPriceText, salesTaxText, orderTotalText;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private String pizzaInfo;
    private int pizzaPos;
    private double tempNum;
    private static final double STARTING_PRICE = 0.00;
    private static final double TAX = 0.06625;
    double total = STARTING_PRICE;
    private ArrayAdapter<String> arrayAdapter;
    private Pizza pia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        order = MainActivity.getOrd();
        orderList = (ListView)findViewById(R.id.orderList);
        pizzaList = new ArrayList<String>();
        createPizzaList();
        arrayAdapter = new ArrayAdapter<>(CurrentOrderActivity.this,android.R.layout.simple_list_item_single_choice, pizzaList);
        orderNumberText = findViewById(R.id.orderNumberText);
        totalPriceText = findViewById(R.id.totalPriceText);
        salesTaxText = findViewById(R.id.salesTaxText);
        orderTotalText = findViewById(R.id.orderTotalText);
        orderList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        orderList.setAdapter(arrayAdapter);
        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                pizzaInfo = arrayAdapter.getItem(position);
                pizzaPos = position;
                Toast.makeText(getApplicationContext(), "Pizza selected", Toast.LENGTH_SHORT).show();
            }
        });
        for (Pizza za : order.getOrders()) {
            double temp = totalPrice(za.price());
            setTotalPriceText(temp);
            setSalesTaxText(temp);
            tempNum = temp + saleTaxPrice(temp);
            setOrderTotalText(tempNum);
        }
        setOrderNumberText();
        makeButtons(arrayAdapter);
    }
    public void createPizzaList(){
        if(order!=null){
            for(Pizza za: order.getOrders()){
                pizzaList.add(toString(za));
            }
        }
    }
    public void makeButtons(ArrayAdapter<String> arrayAdapter) {
        backButton = (Button) findViewById(R.id.backCurrentButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {backButton();}
        });
        removePButton = (Button) findViewById(R.id.removePButton);
        removePButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {removePizza(arrayAdapter);}
        });
        removeAButton = (Button) findViewById(R.id.removeAButton);
        removeAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {removeAllPizzas(arrayAdapter);}
        });
        placeButton = (Button) findViewById(R.id.placeButton);
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder(arrayAdapter);
            }
        });
    }
    public void removePizza(ArrayAdapter<String> arrayAdapter){
        if(pizzaInfo==null){

        }
        pia = order.getOrders().get(pizzaPos);

        double tempPrice = removeTotal(pia.price());
        setTotalPriceText(tempPrice);
        setSalesTaxText(tempPrice);
        setOrderTotalText(tempPrice);
        order.remove(order.getOrders().get(pizzaPos));

        pizzaList.remove(pizzaPos);
        arrayAdapter.notifyDataSetChanged();

    }
    public void removeAllPizzas(ArrayAdapter<String> arrayAdapter){
        order.getOrders().clear();
        pizzaList.clear();
        arrayAdapter.notifyDataSetChanged();
    }
    public void placeOrder(ArrayAdapter<String> arrayAdapter){
        MainActivity.addStoreOrd(order);
        order.setTaxPrice(tempNum);
        MainActivity.resetOrder();
        pizzaList.clear();
        arrayAdapter.notifyDataSetChanged();
    }
    public void backButton(){openMainActivity();}
    public void openMainActivity(){
        Intent intent = new Intent(CurrentOrderActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public double totalPrice(double price){
        total += price;
        return total;
    }
    public void setTotalPriceText(double temper){
        String totalPriceString = df.format(temper);
        totalPriceText.setText(totalPriceString);
    }
    public void setSalesTaxText(double temper){
        String taxString = df.format(saleTaxPrice(temper));
        salesTaxText.setText("$" + taxString);
    }
    public void setOrderTotalText(double temper){
        String orderString = df.format(temper);
        orderTotalText.setText("$" + orderString);
    }
    public void setOrderNumberText(){
        orderNumberText.setText("" + order.getOrderSerial());
    }

    /**
     * sets sale tax price
     * @param total
     * @return saleTaxPrice
     */
    public double saleTaxPrice(double total){
        return (TAX * total);
    }

    /**
     * when pizza is removed, removes the pizzas
     * price from total price
     * @param price
     * @return total
     */
    public double removeTotal(double price) {
        total -= price;
        return total;
    }
    public String toString(Pizza pia) {
        String str;
        String type="";
        if(pia instanceof Deluxe){type = "Deluxe; ";}
        if(pia instanceof BuildYourOwn){type = "Build Your Own; ";}
        if(pia instanceof BBQChicken){type = "BBQChicken; ";}
        if(pia instanceof Meatzza){type = "Meatzza; ";}
        String price = df.format(pia.price());
        if (pia.getCrust().equals(Crust.valueOf("DEEPDISH")) || pia.getCrust().equals(Crust.valueOf("PAN")) || pia.getCrust().equals(Crust.valueOf("STUFFED"))){
            str = "" + type + "Pizza Style: CHICAGO"  + "; Crust: " + pia.getCrust()+ "; Toppings: " + pia.printArrayList() + "; Price: $" + price;
            return str;
        }
        else{
            str = "" + type + "Pizza Style: NEW YORK"  + "; Crust: " + pia.getCrust()+ "; Toppings: " + pia.printArrayList() + "; Price: $" + price;
            return (str);
        }
    }
}