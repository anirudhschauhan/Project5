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
    /**
     * initializes the activity, and initializes all the items used later on
     * @param savedInstanceState
     */
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
    /**
     * creates a list of pizzas that are currently in the order
     */
    public void createPizzaList(){
        if(order!=null){
            for(Pizza za: order.getOrders()){
                pizzaList.add(toString(za));
            }
        }
    }
    /**
     * creates buttons to be clicked on
     * @param arrayAdapter
     */
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
    /**
     * removes pizza from the current order
     * @param arrayAdapter
     */
    public void removePizza(ArrayAdapter<String> arrayAdapter){
        if(pizzaInfo==null){
            Toast.makeText(CurrentOrderActivity.this, "Cannot remove any pizzas", Toast.LENGTH_SHORT).show();
            return;
        }
        pia = order.getOrders().get(pizzaPos);

        double tempPrice = removeTotal(pia.price());
        setTotalPriceText(tempPrice);
        setSalesTaxText(tempPrice);
        tempNum = tempPrice + saleTaxPrice(tempPrice);
        setOrderTotalText(tempNum);
        order.remove(order.getOrders().get(pizzaPos));
        pizzaList.remove(pizzaPos);
        arrayAdapter.notifyDataSetChanged();

    }
    /**
     * removes ALL pizzas from the current order
     * @param arrayAdapter
     */
    public void removeAllPizzas(ArrayAdapter<String> arrayAdapter){
        if(pizzaList== null){
            Toast.makeText(CurrentOrderActivity.this, "Cannot remove pizzas", Toast.LENGTH_SHORT).show();
        }
        order.getOrders().clear();
        pizzaList.clear();
        setAllBlank();
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * places the order to the store containing pizzas
     * to be made
     * @param arrayAdapter
     */
    public void placeOrder(ArrayAdapter<String> arrayAdapter){
        if(pizzaList==null){
            Toast.makeText(CurrentOrderActivity.this, "Cannot order any pizzas if there are none", Toast.LENGTH_SHORT).show();
            return;
        }
        MainActivity.addStoreOrd(order);
        order.setTaxPrice(tempNum);
        MainActivity.resetOrder();
        pizzaList.clear();
        setAllBlank();
        arrayAdapter.notifyDataSetChanged();
    }
    /**
     * back button for the program, returns to main menu
     */
    public void backButton(){openMainActivity();}
    /**
     * opens the main menu
     */
    public void openMainActivity(){
        Intent intent = new Intent(CurrentOrderActivity.this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * displays the total price of the current order
     * @param price
     * @return
     */
    public double totalPrice(double price){
        total += price;
        return total;
    }
    /**
     * sets the price of the current order
     * @param temper
     */
    public void setTotalPriceText(double temper){
        String totalPriceString = df.format(temper);
        totalPriceText.setText("$"+ totalPriceString);
    }
    /**
     * sets the sales tax of the current order
     * @param temper
     */
    public void setSalesTaxText(double temper){
        String taxString = df.format(saleTaxPrice(temper));
        salesTaxText.setText("$" + taxString);
    }
    /**
     * sets the total price of the current order
     * @param temper
     */
    public void setOrderTotalText(double temper){
        String orderString = df.format(temper);
        orderTotalText.setText("$" + orderString);
    }

    /**
     * sets the order number
     */
    public void setOrderNumberText(){
        orderNumberText.setText("" + order.getOrderSerial());
    }
    /**
     * sets the total price of pizzas, sales tax,
     * and order total cost to blanks
     */
    public void setAllBlank(){
        setTotalPriceText(STARTING_PRICE);
        setSalesTaxText(STARTING_PRICE);
        setOrderTotalText(STARTING_PRICE);
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
    /**
     * returns the pizza and its toppings in a string format
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