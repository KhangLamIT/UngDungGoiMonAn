package hufi.edu.vn.ungdunggoimonan.QuanTri;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hufi.edu.vn.ungdunggoimonan.R;
import hufi.edu.vn.ungdunggoimonan.ui.BanAn;

public class QuanLyBanAnFragment extends Fragment {
    private EditText txtTenBanAn;
    private Button btnQLBAXoa, btnQLBAThem,btnQLBASua;
    private BanAn banAn = null;
    private QLBanAnFragment qlBanAnFragment;
    public QuanLyBanAnFragment() {
    }

    public static QuanLyBanAnFragment newInstance() {
        QuanLyBanAnFragment fragment = new QuanLyBanAnFragment();
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
        View view = inflater.inflate(R.layout.fragment_quan_ly_ban_an, container, false);
        qlBanAnFragment = (QLBanAnFragment)getChildFragmentManager().findFragmentById(R.id.fragmentQLBanAn);
        txtTenBanAn = view.findViewById(R.id.txtTenBan);
        btnQLBAXoa = view.findViewById(R.id.btnQLBAXoa);
        caiDatbtnXoa();

        btnQLBAThem = view.findViewById(R.id.btnQLBAThem);
        caiDatbtnThem();

        btnQLBASua = view.findViewById(R.id.btnQLBASua);
        caiDatbtnSua();
        return view;
    }

    private void caiDatbtnXoa() {
        btnQLBAXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(banAn==null){
                    Toast.makeText(getContext(), "Vui lòng chọn bàn ăn để xóa!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(banAn.getTrangThai()!=1){
                    Toast.makeText(getContext(), "Vui lòng chọn bàn trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(getActivity())
                        .setTitle("Thông báo")
                        .setMessage("Bạn có muốn xóa " + banAn.getTenBan().toString() + " không?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("BanAn/"+banAn.getId());
                                myRef.removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Toast.makeText(getContext(), "Xóa "+banAn.getTenBan().toString()+" thành công.", Toast.LENGTH_SHORT).show();
                                        txtTenBanAn.setText(null);
                                        banAn = null;
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });
    }

    private void caiDatbtnThem() {
        btnQLBAThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtTenBanAn.getText().toString().trim().length()==0){
                    Toast.makeText(getContext(), "Vui lòng nhập tên bàn ăn để thêm!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //gán dữ liệu cho bàn ăn cần thêm
                banAn = new BanAn();
                banAn.setId(qlBanAnFragment.getIdBanAnMax()+1);
                banAn.setTenBan(txtTenBanAn.getText().toString());
                banAn.setTrangThai(1);

                //lưu thêm
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("BanAn");
                myRef.child(String.valueOf(banAn.getId())).setValue(banAn);

                Toast.makeText(getContext(), "Thêm "+txtTenBanAn.getText().toString()+" thành công!", Toast.LENGTH_SHORT).show();

                //xóa dữ liệu tên bàn ăn
                txtTenBanAn.setText(null);
                banAn = null;
            }
        });
    }

    private void caiDatbtnSua() {
        btnQLBASua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(banAn==null){
                    Toast.makeText(getContext(), "Vui lòng chọn bàn ăn để sửa!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtTenBanAn.getText().toString().trim().length()==0){
                    Toast.makeText(getContext(), "Vui lòng nhập tên bàn ăn để sửa!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //set dữ liệu cần sửa
                banAn.setTenBan(txtTenBanAn.getText().toString());

                //lưu sửa
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("BanAn");
                myRef.child(String.valueOf(banAn.getId())).setValue(banAn);
                Toast.makeText(getContext(), "Sửa "+txtTenBanAn.getText().toString()+" thành công!", Toast.LENGTH_SHORT).show();
                //lưu thành công set tên bàn ăn trống
                txtTenBanAn.setText(null);
                banAn = null;
            }
        });
    }

    public void loadTenBan(BanAn banAn){
        txtTenBanAn.setText(banAn.getTenBan());
        this.banAn = banAn;
    }
}