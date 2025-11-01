package com.example.baitapcuoiky.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
    private ImageView imgSearch;
    private FloatingActionButton btnAddContact;

    private ContactAdapter adapter;
    private List<Contact> contactList;
    private List<Contact> filteredList;
    private DatabaseHelper dbHelper;

    private static final int REQUEST_ADD_CONTACT = 1;
    private static final int REQUEST_VIEW_DETAIL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo views
        listViewContacts = findViewById(R.id.listViewContacts);
        edtSearch = findViewById(R.id.edtSearch);
        imgSearch = findViewById(R.id.imgSearch);
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

        // Xử lý tìm kiếm theo tên
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterContacts();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Xử lý nút tìm kiếm
        imgSearch.setOnClickListener(v -> {
            filterContacts();
        });

        // Xử lý click vào item để xem chi tiết
        adapter.setOnItemClickListener(contact -> {
            Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
            intent.putExtra("contactId", contact.getId());
            intent.putExtra("name", contact.getName());
            intent.putExtra("phone", contact.getPhone());
            intent.putExtra("note", contact.getNote());
            startActivityForResult(intent, REQUEST_VIEW_DETAIL);
        });

        // Xử lý nút thêm liên hệ
        btnAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivityForResult(intent, REQUEST_ADD_CONTACT);
        });
    }

    // Phương thức lọc danh sách liên hệ
    private void filterContacts() {
        String searchText = edtSearch.getText().toString().toLowerCase().trim();

        filteredList.clear();

        for (Contact contact : contactList) {
            boolean matchSearch = contact.getName().toLowerCase().contains(searchText) ||
                    contact.getPhone().contains(searchText);
            if (matchSearch) {
                filteredList.add(contact);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String action = data.getStringExtra("action");

            if ("add".equals(action)) {
                // Thêm liên hệ mới
                String name = data.getStringExtra("name");
                String phone = data.getStringExtra("phone");
                String note = data.getStringExtra("note");

                Contact newContact = new Contact(name, phone, note != null ? note : "");
                long newId = dbHelper.addContact(newContact);
                newContact.setId((int) newId);

                // Reload danh sách từ database
                contactList = dbHelper.getAllContacts();
                filterContacts();

                Toast.makeText(this, "Đã thêm liên hệ mới", Toast.LENGTH_SHORT).show();

            } else if ("edit".equals(action)) {
                // Sửa liên hệ
                int contactId = data.getIntExtra("contactId", -1);
                String name = data.getStringExtra("name");
                String phone = data.getStringExtra("phone");
                String note = data.getStringExtra("note");

                Contact contact = new Contact(contactId, name, phone, note != null ? note : "");
                dbHelper.updateContact(contact);

                // Reload danh sách từ database
                contactList = dbHelper.getAllContacts();
                filterContacts();
                Toast.makeText(this, "Đã cập nhật liên hệ", Toast.LENGTH_SHORT).show();

            } else if ("delete".equals(action)) {
                // Xóa liên hệ
                int contactId = data.getIntExtra("contactId", -1);
                dbHelper.deleteContact(contactId);

                // Reload danh sách từ database
                contactList = dbHelper.getAllContacts();
                filterContacts();
                Toast.makeText(this, "Đã xóa liên hệ", Toast.LENGTH_SHORT).show();
            }
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