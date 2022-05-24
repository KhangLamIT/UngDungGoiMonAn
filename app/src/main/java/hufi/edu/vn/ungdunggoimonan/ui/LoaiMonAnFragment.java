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

public class LoaiMonAnFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private List<LoaiMonAn> lstLoaMonAn = new ArrayList<>();
    private MyLoaiMonAnRecyclerViewAdapter myLoaiMonAnRecyclerViewAdapter;

    public LoaiMonAnFragment() {
    }

    public static LoaiMonAnFragment newInstance(int columnCount) {
        LoaiMonAnFragment fragment = new LoaiMonAnFragment();
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
        View view = inflater.inflate(R.layout.fragment_loai_mon_an_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            loadLoaiMonAn();
            myLoaiMonAnRecyclerViewAdapter = new MyLoaiMonAnRecyclerViewAdapter(getContext(),lstLoaMonAn);
            recyclerView.setAdapter(myLoaiMonAnRecyclerViewAdapter);
        }
        return view;
    }

    private void loadLoaiMonAn() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LoaiMonAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstLoaMonAn!=null)
                    lstLoaMonAn.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    LoaiMonAn loaiMonAn = postSnapshot.getValue(LoaiMonAn.class);
                    lstLoaMonAn.add(loaiMonAn);
                }
                myLoaiMonAnRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi Load Bàn Ăn!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}