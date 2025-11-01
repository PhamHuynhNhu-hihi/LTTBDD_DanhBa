package com.example.baitapcuoiky.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapcuoiky.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddContactActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtPhone, edtNote;
    private Button btnSave;
    private ImageView btnBack;
    private boolean isEditMode = false;
    private int contactId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtNote = findViewById(R.id.edtNote);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEdit", false);

        if (isEditMode) {
            contactId = intent.getIntExtra("contactId", -1);
            edtName.setText(intent.getStringExtra("name"));
            edtPhone.setText(intent.getStringExtra("phone"));
            edtNote.setText(intent.getStringExtra("note"));
            btnSave.setText("Cập nhật");
        }


        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String note = edtNote.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phone.matches("\\d{10}")) {
                Toast.makeText(this, "Chưa đúng định dạng của số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("action", isEditMode ? "edit" : "add");
            resultIntent.putExtra("contactId", contactId);
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("phone", phone);
            resultIntent.putExtra("note", note);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
