package hufi.edu.vn.ungdunggoimonan.QuanTri;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hufi.edu.vn.ungdunggoimonan.R;
import hufi.edu.vn.ungdunggoimonan.ui.BanAn;
import hufi.edu.vn.ungdunggoimonan.ui.MonAn;

public class QuanLyNhanVienFragment extends Fragment {
    private Spinner spinnerChucVu;
    private Button btnThem, btnSua, btnRepassword;
    private EditText txtHoTen, txtSoDT;
    private Switch switchTrangThai;
    private Button btnThemHinh;
    private ImageView imgQuanLyNV;
    private ChucVu chucVuNV;
    private NhanVien nhanVien = null;
    private List<ChucVu> lstChucVu = new ArrayList<ChucVu>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private QLNhanVienFragment qlNhanVienFragment;
    private Uri mImageUri = null;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    public QuanLyNhanVienFragment() {
    }

    public static QuanLyNhanVienFragment newInstance() {
        QuanLyNhanVienFragment fragment = new QuanLyNhanVienFragment();
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
        View view = inflater.inflate(R.layout.fragment_quan_ly_nhan_vien, container, false);
        anhXaView(view);
        loadChucVu();
        caiDatbtnThem();
        caiDatbtnRepassword();
        caiDatbtnSua();
        caiDatbtnChonHinh();
        return view;
    }

    private void caiDatbtnChonHinh() {
        btnThemHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(getContext()).load(mImageUri).into(imgQuanLyNV);
        }
    }

    private void anhXaView(View view) {
        qlNhanVienFragment = (QLNhanVienFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerQLNV);
        txtHoTen = view.findViewById(R.id.txtQLNV_TenNV);
        txtSoDT = view.findViewById(R.id.txtQLNV_SDT);
        switchTrangThai = view.findViewById(R.id.switchTrangThaiHoatDong);
        spinnerChucVu = view.findViewById(R.id.spinnerChucVu);
        btnThem = view.findViewById(R.id.btnQLNVThem);
        btnRepassword = view.findViewById(R.id.btnQLNVMatKhauMoi);
        btnSua = view.findViewById(R.id.btnQLNVSua);
        btnThemHinh = view.findViewById(R.id.btnThemHinh);
        imgQuanLyNV = view.findViewById(R.id.imgQuanLyNV);
        mStorageRef = FirebaseStorage.getInstance().getReference("imagesNhanVien");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("NhanVien");
    }

    private void caiDatbtnSua() {
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nhanVien==null){
                    Toast.makeText(getContext(), "Vui lòng chọn nhân viên để sửa!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!ktNhap()){
                    return;
                }
                updateFile();
            }
        });
    }

    private void xoaDuLieuView() {
        //xóa dữ liệu nhân viên
        txtHoTen.setText(null);
        txtSoDT.setText(null);
        nhanVien = null;
        imgQuanLyNV.setImageResource(R.drawable.ic_baseline_insert_photo_24);
    }

    private void caiDatbtnRepassword() {
        btnRepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nhanVien==null){
                    Toast.makeText(getContext(), "Vui lòng chọn nhân viên để cài lại mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //gán dữ liệu cho nhân viên
                nhanVien.setId(nhanVien.getId());
                nhanVien.setMatKhau(nhanVien.getSoDT());

                //lưu
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("NhanVien");
                myRef.child(String.valueOf(nhanVien.getId())).setValue(nhanVien);
                Toast.makeText(getContext(),"Cài lại mật khẩu cho "+nhanVien.getHoTen() +" thành công!", Toast.LENGTH_SHORT).show();

                xoaDuLieuView();
            }
        });
    }

    private void caiDatbtnThem() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ktNhap()){
                    return;
                }
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Đang tải lên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadFile();
            }
        });

    }

    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //gán dữ liệu cho nhân viên
                            nhanVien = new NhanVien();
                            nhanVien.setId(qlNhanVienFragment.getIdNhanVienMax()+1);
                            nhanVien.setHoTen(txtHoTen.getText().toString());
                            nhanVien.setChucVu(chucVuNV);
                            nhanVien.setSoDT(txtSoDT.getText().toString());
                            nhanVien.setMatKhau(txtSoDT.getText().toString());
                            nhanVien.setTrangThai(switchTrangThai.isChecked());
                            nhanVien.setHinh(uri.toString());

                            if(qlNhanVienFragment.ktTrungSoDT(nhanVien.getId(),nhanVien.getSoDT())){
                                Toast.makeText(getContext(), "Số điện thoại đã có sẵn trên hệ thống", Toast.LENGTH_LONG).show();
                                return;
                            }
                            //lưu thêm
                            mDatabaseRef.child(String.valueOf(nhanVien.getId())).setValue(nhanVien);
                            Toast.makeText(getContext(),"Thêm "+nhanVien.getHoTen() +" thành công!", Toast.LENGTH_SHORT).show();
                            xoaDuLieuView();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Lỗi!", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getContext(), "Không có hình được chọn!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFile() {
        if (mImageUri != null) {
            final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //gán dữ liệu cho nhân viên
                            nhanVien.setId(nhanVien.getId());
                            nhanVien.setHoTen(txtHoTen.getText().toString());
                            nhanVien.setChucVu(chucVuNV);
                            nhanVien.setSoDT(txtSoDT.getText().toString());
                            nhanVien.setTrangThai(switchTrangThai.isChecked());

                            if(qlNhanVienFragment.ktTrungSoDT(nhanVien.getId(),nhanVien.getSoDT())){
                                Toast.makeText(getContext(), "Số điện thoại đã có sẵn trên hệ thống", Toast.LENGTH_LONG).show();
                                return;
                            }
                            StorageReference desertRef = FirebaseStorage.getInstance().getReferenceFromUrl(nhanVien.getHinh());
                            // Delete the file
                            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                }
                            });

                            //lưu
                            nhanVien.setHinh(uri.toString());
                            mDatabaseRef.child(String.valueOf(nhanVien.getId())).setValue(nhanVien);
                            Toast.makeText(getContext(),"Sửa "+nhanVien.getHoTen() +" thành công!", Toast.LENGTH_SHORT).show();
                            xoaDuLieuView();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //gán dữ liệu cho nhân viên
                    nhanVien.setId(nhanVien.getId());
                    nhanVien.setHoTen(txtHoTen.getText().toString());
                    nhanVien.setChucVu(chucVuNV);
                    nhanVien.setSoDT(txtSoDT.getText().toString());
                    nhanVien.setTrangThai(switchTrangThai.isChecked());
                    mDatabaseRef.child(String.valueOf(nhanVien.getId())).setValue(nhanVien);
                    Toast.makeText(getContext(),"Sửa "+nhanVien.getHoTen() +" thành công!", Toast.LENGTH_SHORT).show();
                    xoaDuLieuView();
                }
            });

        } else {
            Toast.makeText(getContext(), "Không có hình được chọn!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    private boolean ktNhap() {
        if(txtHoTen.getText().toString().trim().length()==0){
            Toast.makeText(getContext(), "Vui lòng nhập tên nhân viên!", Toast.LENGTH_SHORT).show();
            txtHoTen.requestFocus();
            return false;
        }
        if(txtSoDT.getText().toString().trim().length()==0){
            Toast.makeText(getContext(), "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            txtSoDT.requestFocus();
            return false;
        }
        return true;
    }

    private void loadChucVu() {
        //set adapter chức vụ
        ArrayAdapter<ChucVu> adapter = new ArrayAdapter<ChucVu>(getActivity(),
                android.R.layout.simple_spinner_item,
                lstChucVu);
        spinnerChucVu.setAdapter(adapter);
        spinnerChucVu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chucVuNV = lstChucVu.get(position);
                //Toast.makeText(getContext(), chucVuNV.getTenChucVu() + " id: " + chucVuNV.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //load chức vụ từ firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ChucVu");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstChucVu!=null)
                    lstChucVu.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    ChucVu cv = postSnapshot.getValue(ChucVu.class);
                    if(cv.getId()==3)
                        continue;
                    lstChucVu.add(cv);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi Load Chức Vụ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadNhanVien(NhanVien nhanVien){
        this.nhanVien = nhanVien;
        txtHoTen.setText(nhanVien.getHoTen());
        txtSoDT.setText(nhanVien.getSoDT());
        switchTrangThai.setChecked(nhanVien.isTrangThai());
        mImageUri = Uri.parse(nhanVien.getHinh());
        Picasso.with(getContext())
                .load(nhanVien.getHinh())
                .placeholder(R.mipmap.ic_launcher)
                .into(imgQuanLyNV);
        for (int i = 0;i<lstChucVu.stream().count();i++){
            if(lstChucVu.get(i).getId()==nhanVien.getChucVu().getId()){
                spinnerChucVu.setSelection(i);
                break;
            }
        }
    }
}