package com.cambrian.cambrianfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerAdapterInterface {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ViewModel viewModel;
    private ActivityResultLauncher<Intent> newActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().setTitle("My Contacts");

        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable List<Contact> contactModels) {
                recyclerViewAdapter.setContacts(viewModel.getContacts().getValue());
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerViewAdapter.setContacts(viewModel.getContacts().getValue());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 0) {
                            Intent data = result.getData();
                            final Contact model = (Contact) data.getSerializableExtra("Model");
                            viewModel.insert(model);
                        } else if (result.getResultCode() == 1) {
                            Intent data = result.getData();
                            final Contact model = (Contact) data.getSerializableExtra("Model");
                            viewModel.delete(model);
                            viewModel.insert(model);
                        } else if (result.getResultCode() == -1) {
                            Intent data = result.getData();
                            final Contact model = (Contact) data.getSerializableExtra("Model");
                            viewModel.delete(model);
                        }
                    }
                });

        FloatingActionButton floatingActionButton = findViewById(R.id.addContact);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewContactIntent = new Intent(MainActivity.this, ViewOrUpdateContact.class);
                newActivityLauncher.launch(createNewContactIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterContactList(s.toLowerCase());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void deleteContact(Contact model) {
        viewModel.delete(model);
    }

    @Override
    public void contactDidTap(Contact model) {
        Intent viewContactIntent = new Intent(MainActivity.this, ViewOrUpdateContact.class);
        viewContactIntent.putExtra("Model", model);
        newActivityLauncher.launch(viewContactIntent);
    }

    private void filterContactList(String text) {
        List<Contact> filteredModels = new ArrayList<Contact>();
        for (Contact model:
                viewModel.getContacts().getValue()) {
            final String fullName = model.getName();
            if (fullName.toLowerCase().contains(text.toLowerCase())) {
                filteredModels.add(model);
            }
        }
        recyclerViewAdapter.setContacts(text.isEmpty() ? viewModel.getContacts().getValue() : filteredModels);
    }
}