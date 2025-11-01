package com.example.baitapcuoiky.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapcuoiky.Model.Contact;
import com.example.baitapcuoiky.Adapter.ContactAdapter;
import com.example.baitapcuoiky.sqlite.DatabaseHelper;
import com.example.baitapcuoiky.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewContacts;
    private EditText edtSearch;
    private FloatingActionButton btnAddContact;

    private ContactAdapter adapter;
    private List<Contact> contactList;
    private List<Contact> filteredList;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo views
        listViewContacts = findViewById(R.id.listViewContacts);
        edtSearch = findViewById(R.id.edtSearch);
        btnAddContact = findViewById(R.id.btnAddContact);

        // Khởi tạo database helper
        dbHelper = new DatabaseHelper(this);

        // Load danh sách liên hệ từ database
        contactList = dbHelper.getAllContacts();

        // Nếu chưa có dữ liệu, thêm dữ liệu mẫu
        if (contactList.isEmpty()) {
            dbHelper.addContact(new Contact("Nguyễn Văn A", "0123456789", "Đây là ghi chú mẫu"));
            dbHelper.addContact(new Contact("Trần Thị B", "0987654321", ""));
            dbHelper.addContact(new Contact("Lê Văn C", "0369852147", "Gọi vào giờ hành chính"));
            contactList = dbHelper.getAllContacts();
        }

        filteredList = new ArrayList<>(contactList);

        // Thiết lập adapter
        adapter = new ContactAdapter(this, filteredList);
        listViewContacts.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterContacts();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        adapter.setOnItemClickListener(contact -> {
            Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
            intent.putExtra("contactId", contact.getId());
            intent.putExtra("name", contact.getName());
            intent.putExtra("phone", contact.getPhone());
            intent.putExtra("note", contact.getNote());
            startActivityForResult(intent, 1);
        });

        btnAddContact.setOnClickListener(v -> {
        // Xử lý nút thêm liên hệ
            startActivityForResult(new Intent(this, AddContactActivity.class), 1);
        });
    }

    private void filterContacts() {
    // Phương thức lọc danh sách liên hệ
        String search = edtSearch.getText().toString().toLowerCase().trim();
        filteredList.clear();
        for (Contact c : contactList) {
            if (c.getName().toLowerCase().contains(search) || c.getPhone().contains(search)) {
                filteredList.add(c);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String action = data.getStringExtra("action");
            String name = data.getStringExtra("name");
            String phone = data.getStringExtra("phone");
            String note = data.getStringExtra("note");
            int contactId = data.getIntExtra("contactId", -1);

            if ("add".equals(action)) {
                dbHelper.addContact(new Contact(name, phone, note != null ? note : ""));
                Toast.makeText(this, "Đã thêm liên hệ", Toast.LENGTH_SHORT).show();
            } else if ("edit".equals(action)) {
                dbHelper.updateContact(new Contact(contactId, name, phone, note != null ? note : ""));
                Toast.makeText(this, "Đã cập nhật liên hệ", Toast.LENGTH_SHORT).show();
            } else if ("delete".equals(action)) {
                dbHelper.deleteContact(contactId);
                Toast.makeText(this, "Đã xóa liên hệ", Toast.LENGTH_SHORT).show();
            }
            contactList = dbHelper.getAllContacts();
            filterContacts();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}