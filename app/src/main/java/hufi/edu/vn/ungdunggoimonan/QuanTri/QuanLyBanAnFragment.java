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
                    Toast.makeText(getContext(), "Vui l??ng ch???n b??n ??n ????? x??a!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(banAn.getTrangThai()!=1){
                    Toast.makeText(getContext(), "Vui l??ng ch???n b??n tr???ng!", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(getActivity())
                        .setTitle("Th??ng b??o")
                        .setMessage("B???n c?? mu???n x??a " + banAn.getTenBan().toString() + " kh??ng?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("BanAn/"+banAn.getId());
                                myRef.removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Toast.makeText(getContext(), "X??a "+banAn.getTenBan().toString()+" th??nh c??ng.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Vui l??ng nh???p t??n b??n ??n ????? th??m!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //g??n d??? li???u cho b??n ??n c???n th??m
                banAn = new BanAn();
                banAn.setId(qlBanAnFragment.getIdBanAnMax()+1);
                banAn.setTenBan(txtTenBanAn.getText().toString());
                banAn.setTrangThai(1);

                //l??u th??m
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("BanAn");
                myRef.child(String.valueOf(banAn.getId())).setValue(banAn);

                Toast.makeText(getContext(), "Th??m "+txtTenBanAn.getText().toString()+" th??nh c??ng!", Toast.LENGTH_SHORT).show();

                //x??a d??? li???u t??n b??n ??n
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
                    Toast.makeText(getContext(), "Vui l??ng ch???n b??n ??n ????? s???a!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtTenBanAn.getText().toString().trim().length()==0){
                    Toast.makeText(getContext(), "Vui l??ng nh???p t??n b??n ??n ????? s???a!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //set d??? li???u c???n s???a
                banAn.setTenBan(txtTenBanAn.getText().toString());

                //l??u s???a
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("BanAn");
                myRef.child(String.valueOf(banAn.getId())).setValue(banAn);
                Toast.makeText(getContext(), "S???a "+txtTenBanAn.getText().toString()+" th??nh c??ng!", Toast.LENGTH_SHORT).show();
                //l??u th??nh c??ng set t??n b??n ??n tr???ng
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