package com.example.android.justjava;


import java.net.URI;
import java.text.NumberFormat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 0;
    int addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Setting up limits for ordering > than 100 and less than 1 cup of coffee.
     * Setting up Toast feature for pop up window with WARNING.
     */

    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);

    }

    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);


    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whipped_cream_checkbox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whipped_cream_checkbox.isChecked();

        CheckBox chocolate_checkbox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolate_checkbox.isChecked();

        EditText name_field = (EditText) findViewById(R.id.name_field);
        String name = name_field.getText().toString();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);

//Intent that actually send filled order to some EMAIL address
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }



    }

    /**
     * Calculates the price of the order with chocolate and cream.
     */
    private int calculatePrice(boolean addChocolate, boolean addWhippedCream) {

        int basePrice = 5;

        if (addWhippedCream) {
            basePrice = basePrice + 1;
        }
        if (addChocolate) {
            basePrice = basePrice + 2;
        }

        return quantity * basePrice;


    }

    /**
     * Creates an order summary.Include name,quantity, total price.
     * hasWhippedCream show true/false of the request
     */

    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolates, String name) {
        String priceMessage = getString(R.string.order_all_summary_name, name);
        priceMessage += "\n" + getString(R.string.add_whipped_cream) + addWhippedCream;
        priceMessage += "\n" + getString(R.string.add_a_chocolate) + addChocolates;
        priceMessage += "\n" + getString(R.string.quantity_of) + quantity;
        priceMessage += "\n" + getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

}