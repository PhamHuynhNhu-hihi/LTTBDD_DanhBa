package com.example.baitapcuoiky.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapcuoiky.R;

public class ContactDetailActivity extends AppCompatActivity {

    private TextView tvContactName, tvContactPhone, tvContactNote;
    private LinearLayout layoutNote;
    private Button btnCallDetail, btnEditDetail, btnDeleteDetail;
    private ImageView btnBack;
    private int contactId;
    private String name, phone, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // Khởi tạo views
        tvContactName = findViewById(R.id.tvContactName);
        tvContactPhone = findViewById(R.id.tvContactPhone);
        tvContactNote = findViewById(R.id.tvContactNote);
        layoutNote = findViewById(R.id.layoutNote);
        btnCallDetail = findViewById(R.id.btnCallDetail);
        btnEditDetail = findViewById(R.id.btnEditDetail);
        btnDeleteDetail = findViewById(R.id.btnDeleteDetail);
        btnBack = findViewById(R.id.btnBackDetail);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        contactId = intent.getIntExtra("contactId", -1);
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        note = intent.getStringExtra("note");

        //Hiển thị thông tin
        tvContactName.setText(name);
        tvContactPhone.setText(phone);
        // Hiển thị ghi chú

        if (note != null && !note.isEmpty()) {
            tvContactNote.setText(note);
            layoutNote.setVisibility(View.VISIBLE);
        } else {
            layoutNote.setVisibility(View.GONE);
        }

        btnBack.setOnClickListener(v -> finish());
        // Xử lý nút Gọi

        btnCallDetail.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        });
        // Xử lý nút Sửa

        btnEditDetail.setOnClickListener(v -> {
            Intent editIntent = new Intent(ContactDetailActivity.this, AddContactActivity.class);
            editIntent.putExtra("contactId", contactId);
            editIntent.putExtra("name", name);
            editIntent.putExtra("phone", phone);
            editIntent.putExtra("note", note);
            editIntent.putExtra("isEdit", true);
            startActivityForResult(editIntent, 2);
        });
        // Xử lý nút Xóa

        btnDeleteDetail.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa liên hệ " + name + "?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("action", "delete");
                        resultIntent.putExtra("contactId", contactId);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            // Cập nhật lại thông tin sau khi sửa
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}

