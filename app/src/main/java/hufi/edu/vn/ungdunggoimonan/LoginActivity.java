package hufi.edu.vn.ungdunggoimonan;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hufi.edu.vn.ungdunggoimonan.QuanTri.NhanVien;
import hufi.edu.vn.ungdunggoimonan.ui.BanAn;
import hufi.edu.vn.ungdunggoimonan.ui.GoiMonAn;
import hufi.edu.vn.ungdunggoimonan.ui.MonAn;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText txtUserName, txtPassWord;
    private List<NhanVien> lstNhanVien = new ArrayList<NhanVien>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        txtUserName = findViewById(R.id.txtUserName);
        txtPassWord = findViewById(R.id.txtPassword);
        loadlstNhanVien();
        btnLogin = findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NhanVien nhanVien = null;

                for (NhanVien nv : lstNhanVien) {
                    if (txtUserName.getText().toString().trim().equals(nv.getSoDT()) &&
                            txtPassWord.getText().toString().trim().equals(nv.getMatKhau())) {
                        nhanVien = nv;
                        break;
                    }
                }
                if (nhanVien == null) {
                    Toast.makeText(LoginActivity.this, "T??n t??i kho???n ho???c m???t kh???u kh??ng ch??nh x??c!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!nhanVien.isTrangThai()) {
                    Toast.makeText(LoginActivity.this, "T??i kho???n ???? b??? kh??a!", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("NhanVienDangNhap", nhanVien);
                startActivity(intent);
            }
        });
    }

    private void loadlstNhanVien() {
        DatabaseReference myRef = database.getReference("NhanVien");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (lstNhanVien != null)
                    lstNhanVien.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    NhanVien nhanVien = postSnapshot.getValue(NhanVien.class);
                    lstNhanVien.add(nhanVien);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "L???i Load Nh??n Vi??n!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}