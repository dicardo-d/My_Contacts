package com.cambrian.cambrianfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ViewOrUpdateContact extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, emailEditText;
    private Button deleteContactButton;
    private Contact model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_or_update_contact);

        this.getSupportActionBar().setTitle("Add/View Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        deleteContactButton = findViewById(R.id.deleteContactButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            model = (Contact) getIntent().getSerializableExtra("Model");
            nameEditText.setText(model.getName());
            phoneEditText.setText(model.getNumber());
            emailEditText.setText(model.getEmail());

            deleteContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent dataPassIntent = new Intent();
                    dataPassIntent.putExtra("Model", model);
                    setResult(-1, dataPassIntent);
                    finish();
                }
            });
        }

        deleteContactButton.setVisibility(bundle == null ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.save) {
            final Contact newContact = getContact();
            if (newContact != null) {
                Intent dataPassIntent = new Intent();
                dataPassIntent.putExtra("Model", newContact);
                setResult(model == null ? 0 : 1, dataPassIntent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    private Contact getContact() {
        if (model == null) {
            final Contact model = new Contact(nameEditText.getText().toString(), phoneEditText.getText().toString(), emailEditText.getText().toString());
            return model;
        } else {
            model.setName(nameEditText.getText().toString());
            model.setEmail(emailEditText.getText().toString());
            model.setNumber(phoneEditText.getText().toString());
            return model;
        }
    }
}