package hufi.edu.vn.ungdunggoimonan.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hufi.edu.vn.ungdunggoimonan.MainActivity;
import hufi.edu.vn.ungdunggoimonan.QuanTri.NhanVien;
import hufi.edu.vn.ungdunggoimonan.R;

public class ThongTinNhanVienFragment extends Fragment {
    private Button btnDoiMatKhau,btnHuy,btnLuu;
    private RelativeLayout relativeLayout;
    private TextView lbTTTenNhanVien,lbTTSoDT,lbTTChucVu;
    private EditText txtTTPassWordCu,txtTTPassWordMoi,txtTTRePassWordMoi;
    private NhanVien tTNhanVien;

    public ThongTinNhanVienFragment() {
    }

    public static ThongTinNhanVienFragment newInstance() {
        ThongTinNhanVienFragment fragment = new ThongTinNhanVienFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_tin_nhan_vien, container, false);

        loadControlView(view);
        xuLybtnDoiMatKhau();
        xuLybtnHuy();
        xuLybtnLuu();
        return view;
    }

    private void xuLybtnLuu() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiểm tra mật khẩu cũ có trùng ko
                String matKhauCu = txtTTPassWordCu.getText().toString().trim();
                if(!matKhauCu.equals(tTNhanVien.getMatKhau())){
                    Toast.makeText(getContext(), "Mật khẩu cũ không chính xác.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String matKhauMoi = txtTTPassWordMoi.getText().toString().trim();
                String reMatKhauMoi = txtTTRePassWordMoi.getText().toString().trim();
                if(!matKhauMoi.equals(reMatKhauMoi)){
                    Toast.makeText(getContext(), "Mật khẩu mới và nhập lại mật khẩu mới không trùng khớp.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(matKhauMoi.equals(matKhauCu)){
                    Toast.makeText(getContext(), "Mật khẩu mới và mật khẩu cũ không được giống nhau.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //lưu
                tTNhanVien.setMatKhau(matKhauMoi);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("NhanVien");
                myRef.child(String.valueOf(tTNhanVien.getId())).setValue(tTNhanVien);
                Toast.makeText(getContext(),"Lưu mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                relativeLayout.setVisibility(View.INVISIBLE);
                btnDoiMatKhau.setVisibility(View.VISIBLE);
            }
        });
    }

    private void xuLybtnHuy() {
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.INVISIBLE);
                btnDoiMatKhau.setVisibility(View.VISIBLE);
            }
        });
    }

    private void xuLybtnDoiMatKhau() {
        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.VISIBLE);
                btnDoiMatKhau.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void loadControlView(View view) {
        //lấy thông tin nhân viên
        MainActivity mainActivity = (MainActivity)getActivity();
        tTNhanVien = mainActivity.getNhanVienDangNhap();

        lbTTChucVu = view.findViewById(R.id.lbTTChucVu);
        lbTTChucVu.setText(tTNhanVien.getChucVu().getTenChucVu());

        //gán tên nv
        lbTTTenNhanVien = view.findViewById(R.id.lbTTTenNhanVien);
        lbTTTenNhanVien.setText(tTNhanVien.getHoTen());

        //gán sdt
        lbTTSoDT = view.findViewById(R.id.lbTTSoDT);
        lbTTSoDT.setText(tTNhanVien.getSoDT());

        btnDoiMatKhau = view.findViewById(R.id.btnDoiPass);
        btnLuu = view.findViewById(R.id.btnLuuPass);
        btnHuy = view.findViewById(R.id.btnHuy);
        relativeLayout = view.findViewById(R.id.layoutDoiMatKhau);
        txtTTPassWordCu = view.findViewById(R.id.txtTTPassWordCu);
        txtTTPassWordMoi = view.findViewById(R.id.txtTTPassWordMoi);
        txtTTRePassWordMoi = view.findViewById(R.id.txtTTRePassWordMoi);
    }
}