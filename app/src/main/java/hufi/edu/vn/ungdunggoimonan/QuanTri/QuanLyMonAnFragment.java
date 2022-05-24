package hufi.edu.vn.ungdunggoimonan.QuanTri;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import hufi.edu.vn.ungdunggoimonan.ui.LoaiMonAn;
import hufi.edu.vn.ungdunggoimonan.ui.MonAn;

public class QuanLyMonAnFragment extends Fragment {
    private EditText txtTenMA,txtGiaMA;
    private Spinner spinnerLoaiMA;
    private Button btnThem,btnXoa,btnLuu,btnChonHinh;
    private LoaiMonAn loaiMonAn;
    private ImageView imgQuanLyMonAn;
    private MonAn monAn = null;
    private List<LoaiMonAn> lstLoaiMonAn = new ArrayList<LoaiMonAn>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri = null;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    public QuanLyMonAnFragment() {
        // Required empty public constructor
    }

    public static QuanLyMonAnFragment newInstance() {
        QuanLyMonAnFragment fragment = new QuanLyMonAnFragment();
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
        View view = inflater.inflate(R.layout.fragment_quan_ly_mon_an, container, false);
        loadView(view);
        loadLoaiMonAn();
        xuLybtnChonHinh();
        xuLybtnLuu();
        xuLybtnThem();
        xuLybtnXoa();

        return view;
    }

    private void xuLybtnXoa() {
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monAn==null){
                    Toast.makeText(getContext(), "Vui lòng chọn món ăn để xóa!", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(getActivity())
                        .setTitle("Thông báo")
                        .setMessage("Bạn có muốn xóa " + monAn.getTenMonAn() + " không?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("MonAn/"+monAn.getIDMonAn());
                                myRef.removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Toast.makeText(getContext(), "Xóa "+monAn.getTenMonAn()+" thành công.", Toast.LENGTH_SHORT).show();

                                        StorageReference desertRef = FirebaseStorage.getInstance().getReferenceFromUrl(monAn.getHinh());
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

                                        xoaDuLieuView();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });
    }

    private void xuLybtnThem() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Đang tải lên!", Toast.LENGTH_SHORT).show();
                } else {
                    if(txtTenMA.getText().toString().trim().length()==0){
                        Toast.makeText(getContext(), "Vui lòng nhập tên món ăn!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(txtGiaMA.getText().toString().trim().length()==0){
                        Toast.makeText(getContext(), "Vui lòng nhập giá món ăn!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mImageUri==null){
                        Toast.makeText(getContext(), "Vui lòng chọn hình!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    uploadFile();
                }
            }
        });
    }

    private void xuLybtnLuu() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(monAn==null){
                    Toast.makeText(getContext(), "Vui lòng chọn món ăn để sửa", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtTenMA.getText().toString().trim().length()==0){
                    Toast.makeText(getContext(), "Vui lòng nhập tên món ăn!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtGiaMA.getText().toString().trim().length()==0){
                    Toast.makeText(getContext(), "Vui lòng nhập giá món ăn!", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateFile();

            }
        });
    }

    private void xuLybtnChonHinh() {
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
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
            Picasso.with(getContext()).load(mImageUri).into(imgQuanLyMonAn);
        }
    }

    private void loadLoaiMonAn() {
        //set adapter loại món ăn
        ArrayAdapter<LoaiMonAn> adapter = new ArrayAdapter<LoaiMonAn>(getActivity(),
                android.R.layout.simple_spinner_item,
                lstLoaiMonAn);
        spinnerLoaiMA.setAdapter(adapter);
        spinnerLoaiMA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loaiMonAn = lstLoaiMonAn.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //load loại món ăn từ firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LoaiMonAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstLoaiMonAn!=null)
                    lstLoaiMonAn.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    LoaiMonAn lma = postSnapshot.getValue(LoaiMonAn.class);
                    if(lma.getId().equals("loadfull"))
                        continue;
                    lstLoaiMonAn.add(lma);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi Load Loại Món Ăn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadView(View view) {
        txtTenMA = view.findViewById(R.id.txtQLTenMonAn);
        txtGiaMA = view.findViewById(R.id.txtQLGiaMonAn);
        spinnerLoaiMA = view.findViewById(R.id.spinnerLoaiMonAn);
        btnThem = view.findViewById(R.id.btnQLMAThem);
        btnLuu = view.findViewById(R.id.btnQLMALuu);
        btnXoa = view.findViewById(R.id.btnQLMAXoa);
        btnChonHinh = view.findViewById(R.id.btnChonHinh);
        imgQuanLyMonAn = view.findViewById(R.id.imgQuanLyMonAn);
        mStorageRef = FirebaseStorage.getInstance().getReference("imagesMonAn");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("MonAn");
    }

    //thêm
    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String idMonAn = mDatabaseRef.push().getKey();
                            MonAn monAn = new MonAn(
                                    idMonAn,
                                    txtTenMA.getText().toString(),
                                    Double.parseDouble(txtGiaMA.getText().toString()),
                                    uri.toString(),
                                    loaiMonAn.getId(),
                                    0);
                            mDatabaseRef.child(monAn.getIDMonAn()).setValue(monAn);
                            Toast.makeText(getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
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

    //sửa
    private void updateFile() {
        if (mImageUri != null) {
            final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            StorageReference desertRef = FirebaseStorage.getInstance().getReferenceFromUrl(monAn.getHinh());
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
                            monAn.setTenMonAn(txtTenMA.getText().toString());
                            monAn.setGia(Double.parseDouble(txtGiaMA.getText().toString()));
                            monAn.setSoLuong(0);
                            monAn.setIdLoaiMonAn(loaiMonAn.getId());
                            monAn.setHinh(uri.toString());

                            mDatabaseRef.child(monAn.getIDMonAn()).setValue(monAn);
                            Toast.makeText(getContext(), "Sửa thành công!", Toast.LENGTH_SHORT).show();
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
                    monAn.setTenMonAn(txtTenMA.getText().toString());
                    monAn.setGia(Double.parseDouble(txtGiaMA.getText().toString()));
                    monAn.setSoLuong(0);
                    monAn.setIdLoaiMonAn(loaiMonAn.getId());
                    mDatabaseRef.child(monAn.getIDMonAn()).setValue(monAn);
                    Toast.makeText(getContext(), "Sửa thành công!", Toast.LENGTH_SHORT).show();
                    xoaDuLieuView();
                }
            });

        } else {
            Toast.makeText(getContext(), "Không có hình được chọn!", Toast.LENGTH_SHORT).show();
        }
    }

    private void xoaDuLieuView() {
        txtTenMA.setText(null);
        txtGiaMA.setText(null);
        mImageUri = null;
        imgQuanLyMonAn.setImageResource(R.drawable.ic_baseline_insert_photo_24);
        monAn = null;
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    public void loadThongTinMA(MonAn monAn){
        txtTenMA.setText(monAn.getTenMonAn());
        txtGiaMA.setText(String.valueOf(monAn.getGia()));
        Picasso.with(getContext())
                .load(monAn.getHinh())
                .placeholder(R.mipmap.ic_launcher)
                .into(imgQuanLyMonAn);
        mImageUri = Uri.parse(monAn.getHinh());
        for (int i = 0;i<lstLoaiMonAn.stream().count();i++){
            if(lstLoaiMonAn.get(i).getId().equals(monAn.getIdLoaiMonAn())){
                spinnerLoaiMA.setSelection(i);
                break;
            }
        }
        this.monAn = monAn;
    }
}