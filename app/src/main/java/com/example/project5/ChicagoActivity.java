package com.example.project5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * activity interface for the chicago pizzas
 * @author Anirudh Chauhan, Matthew Calora
 */

public class ChicagoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RecyclerView mRecyclerView, addedRecyclerView;
    private ToppingRecyclerAdapter mAdapter, addedAdapter;
    private RecyclerView.LayoutManager mLayoutManager, addedLayoutManager;
    private PizzaFactory piza;
    private Pizza pia;
    private RadioGroup sizeGroup;
    private RadioButton sizeSetter;
    private TextView priceTextView, crustTextView;
    private Button addOrderButton, backButton;
    private boolean addTop, removTop;
    private static final int MAX_TOPPINGS = 7;
    private AlertDialog.Builder addingOrderAlert;
    private ArrayList<ToppingRecycler> toppingRecyclerList, addedToppingRecyclerList;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        piza = new ChicagoPizza();
        pia = piza.createDeluxe();
        pia.setSize(Size.valueOf("SMALL"));
        addTop=false;
        removTop=false;
        setContentView(R.layout.activity_chicago);
        Spinner spinner = findViewById(R.id.flavorLabel);
        sizeGroup = findViewById(R.id.sizeGroup);
        priceTextView = findViewById(R.id.priceTextView);
        crustTextView = findViewById(R.id.crustTextView);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.flavors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        toppingRecyclerList = new ArrayList<>();
        addedToppingRecyclerList = new ArrayList<>();

        for (Topping t : Topping.values()){
            toppingRecyclerList.add(new ToppingRecycler(t.toString()));
        }
        makeRecyclers();
        for(Topping t : pia.getToppingList()){
            addedToppingRecyclerList.add(new ToppingRecycler(t.toString()));
        }
        makeButtons();

        changeCrust();
    }
    /**
     * makes recyclers for the options
     */
    public void makeRecyclers(){
        mRecyclerView = findViewById(R.id.toppingRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ToppingRecyclerAdapter(toppingRecyclerList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ToppingRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if(addTop){
                    addTopping(position);
                }
                else{
                    Toast.makeText(ChicagoActivity.this, "Cannot add any more toppings", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addedRecyclerView = findViewById(R.id.addedToppingRecycler);
        addedRecyclerView.setHasFixedSize(true);
        addedLayoutManager = new LinearLayoutManager(this);
        addedAdapter = new ToppingRecyclerAdapter(addedToppingRecyclerList);
        addedRecyclerView.setLayoutManager(addedLayoutManager);
        addedRecyclerView.setAdapter(addedAdapter);
        addedAdapter.setOnItemClickListener(new ToppingRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if(removTop==true){
                    removeTopping(position);
                }
                else{
                    Toast.makeText(ChicagoActivity.this, "Cannot remove any toppings", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * creates buttons to be clicked on
     */
    public void makeButtons(){
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {backButton();}
        });
        addOrderButton = (Button) findViewById(R.id.addOrderButton);
        addOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {addToOrder();}
        });
    }
    /**
     * the "back" button for the program. returns to the main menu
     */
    public void backButton(){
        openMainActivity();

    }
    /**
     * adds topping to pizza
     * @param position
     * @return
     */
    public boolean addTopping(int position){
        try{
            ToppingRecycler selectedRecycler = toppingRecyclerList.get(position);

            Toast.makeText(this, selectedRecycler.getText() + " ADDED", Toast.LENGTH_SHORT).show();
            Topping currentTopping = (Topping.valueOf(selectedRecycler.getText()));
            if (pia.add(currentTopping)) {
                addedToppingRecyclerList.add(selectedRecycler);
                addedRecyclerView.setLayoutManager(addedLayoutManager);
                addedRecyclerView.setAdapter(addedAdapter);
            }
            if (pia.getToppingList().size() >= MAX_TOPPINGS) {
                addTop = false;
            }
            changePrice();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    /**
     * removes topping from pizza
     * @param position
     * @return
     */
    public boolean removeTopping(int position){
        try{
            ToppingRecycler selectedRecycler = addedToppingRecyclerList.get(position);
            Toast.makeText(this, selectedRecycler.getText() + " REMOVED",Toast.LENGTH_SHORT).show();
            Topping currentTopping = (Topping.valueOf(selectedRecycler.getText()));
            if (pia.remove(currentTopping)) {
                addedToppingRecyclerList.remove(selectedRecycler);
                addedRecyclerView.setLayoutManager(addedLayoutManager);
                addedRecyclerView.setAdapter(addedAdapter);
            }
            if (pia.getToppingList().size() < MAX_TOPPINGS) {
                addTop = true;
            }
           changePrice();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    /**
     * creates a pizza given the flavor
     * @param parent
     * @param view
     * @param pos
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        String flav = parent.getItemAtPosition(pos).toString();
        addTop=false;
        removTop=false;
        if (flav.equals("DELUXE")) {
            pia = piza.createDeluxe();
            changeCrust();
        }
        if (flav.equals("MEATZZA")) {
            pia = piza.createMeatzza();
            changeCrust();
        }
        if (flav.equals("BBQCHICKEN")) {
            pia = piza.createBBQChicken();
            changeCrust();
        }
        if (flav.equals("BUILDYOUROWN")) {
            pia = piza.createBuildYourOwn();
            changeCrust();
            addTop=true;
            removTop=true;
        }
       addedToppingRecyclerList.clear();
        for(Topping t : pia.getToppingList()){
            addedToppingRecyclerList.add(new ToppingRecycler(t.toString()));
        }
        addedRecyclerView.setLayoutManager(addedLayoutManager);
        addedRecyclerView.setAdapter(addedAdapter);
        changeSize();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    /**
     * changes the size of the pizza
     */
    public void changeSize(){

       int radioId = sizeGroup.getCheckedRadioButtonId();

       sizeSetter = findViewById(radioId);

        if (sizeSetter.getText().equals("SMALL")){
            pia.setSize(Size.valueOf("SMALL"));
        }

        if (sizeSetter.getText().equals("MEDIUM")){
            pia.setSize(Size.valueOf("MEDIUM"));
        }
        if (sizeSetter.getText().equals("LARGE")){
            pia.setSize(Size.valueOf("LARGE"));
        }
        changePrice();


    }
    /**
     * alters the size of the pizza on the view
     * @param view
     */
    public void alterSize(View view){
        int radioId = sizeGroup.getCheckedRadioButtonId();
        sizeSetter = findViewById(radioId);
        if (sizeSetter.getText().equals("SMALL")){
            pia.setSize(Size.valueOf("SMALL"));
        }
        if (sizeSetter.getText().equals("MEDIUM")){
            pia.setSize(Size.valueOf("MEDIUM"));
        }
        if (sizeSetter.getText().equals("LARGE")){
            pia.setSize(Size.valueOf("LARGE"));
        }
          changePrice();

    }
    /**
     * adds the pizza to the order
     */
    public void addToOrder(){
        addingOrderAlert = new AlertDialog.Builder(ChicagoActivity.this);
        addingOrderAlert.setTitle("ORDER")
                .setMessage("The pizza will be added to the order. Continue?")
                .setCancelable(true)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.addToOrder(pia);
                        openMainActivity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ChicagoActivity.this, "Pizza was not added to the order", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

    }
    /**
     * changes the price of the order
     */
    public void changePrice(){
        String priceString = df.format(pia.price());
        priceTextView.setText("Price: $"+priceString);
    }
    /**
     * changes the crust of the pizza
     */
    public void changeCrust(){
        String crustString = pia.getCrust().toString();
        crustTextView.setText("CRUST: " + crustString);

    }
    /**
     * opens the main activity interface / main menu
     */
    public void openMainActivity(){
        Intent intent = new Intent(ChicagoActivity.this, MainActivity.class);
        startActivity(intent);
    }




}