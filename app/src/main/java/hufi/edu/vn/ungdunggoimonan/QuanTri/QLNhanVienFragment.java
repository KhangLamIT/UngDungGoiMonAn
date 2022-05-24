package hufi.edu.vn.ungdunggoimonan.QuanTri;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import hufi.edu.vn.ungdunggoimonan.R;
import hufi.edu.vn.ungdunggoimonan.ui.BanAn;

public class QLNhanVienFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private List<NhanVien> lstNhanVien = new ArrayList<>();
    private MyQLNhanVienRecyclerViewAdapter myQLNhanVienRecyclerViewAdapter;
    public QLNhanVienFragment() {
    }

    public static QLNhanVienFragment newInstance(int columnCount) {
        QLNhanVienFragment fragment = new QLNhanVienFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qlnhan_vien_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            loadNhanVien();
            myQLNhanVienRecyclerViewAdapter = new MyQLNhanVienRecyclerViewAdapter(getContext(),(QuanLyNhanVienFragment)getParentFragment(),lstNhanVien);
            recyclerView.setAdapter(myQLNhanVienRecyclerViewAdapter);
        }
        return view;
    }

    private void loadNhanVien() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("NhanVien");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstNhanVien!=null)
                    lstNhanVien.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    NhanVien nhanVien = postSnapshot.getValue(NhanVien.class);
                    if(nhanVien.getChucVu().getId()==3)
                        continue;
                    lstNhanVien.add(nhanVien);
                }
                myQLNhanVienRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi Load Nhân Viên!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getIdNhanVienMax(){
        if(lstNhanVien.stream().count()==0)
            return 0;
        return lstNhanVien.stream().max(Comparator.comparingInt(NhanVien::getId)).get().getId();
    }

    public boolean ktTrungSoDT(int id,String sodt){
        for (NhanVien nv:lstNhanVien) {
            if(nv.getSoDT().equals(sodt) && nv.getId()!=id)
                return true;
        }
        return false;
    }
}