package hufi.edu.vn.ungdunggoimonan.ui;

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
import java.util.List;

import hufi.edu.vn.ungdunggoimonan.R;

public class MonAnFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private List<MonAn> lstMonAn = new ArrayList<>();
    private MyMonAnRecyclerViewAdapter myMonAnRecyclerViewAdapter;

    public MonAnFragment() {
    }

    public static MonAnFragment newInstance(int columnCount) {
        MonAnFragment fragment = new MonAnFragment();
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
        View view = inflater.inflate(R.layout.fragment_monan_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            loadMonAnAll();
            myMonAnRecyclerViewAdapter = new MyMonAnRecyclerViewAdapter(getContext(),lstMonAn);
            recyclerView.setAdapter(myMonAnRecyclerViewAdapter);
        }
        return view;
    }

    public void loadMonAnAll() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MonAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstMonAn!=null)
                    lstMonAn.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    MonAn monAn = postSnapshot.getValue(MonAn.class);
                    lstMonAn.add(monAn);
                }
                myMonAnRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi Load Món Ăn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadMonAnAll_CapNhatSoLuong(List<MonAn> lstMonAnDaChon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MonAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstMonAn!=null)
                    lstMonAn.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    MonAn monAn = postSnapshot.getValue(MonAn.class);
                    lstMonAn.add(monAn);
                }
                for (MonAn ma:lstMonAnDaChon) {
                    for (int i = 0; i<lstMonAn.stream().count();i++){
                        if(lstMonAn.get(i).getIDMonAn().equals(ma.getIDMonAn())){
                            //cập nhật số lượng cho món ăn đó
                            lstMonAn.get(i).setSoLuong(ma.getSoLuong());
                            break;
                        }
                    }
                }

                myMonAnRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi Load Món Ăn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadMonAnTheoLoai_CapNhatSoLuong(String idLoai, List<MonAn> lstMonAnDaChon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MonAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstMonAn!=null)
                    lstMonAn.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    MonAn monAn = postSnapshot.getValue(MonAn.class);
                    if(monAn.getIdLoaiMonAn().equals(idLoai))
                        lstMonAn.add(monAn);
                }
                for (MonAn ma:lstMonAnDaChon) {
                    for (int i = 0; i<lstMonAn.stream().count();i++){
                        if(lstMonAn.get(i).getIDMonAn().equals(ma.getIDMonAn())){
                            //cập nhật số lượng cho món ăn đó
                            lstMonAn.get(i).setSoLuong(ma.getSoLuong());
                            break;
                        }
                    }
                }

                myMonAnRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi Load Món Ăn!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}