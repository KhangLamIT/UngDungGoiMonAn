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
import java.util.List;

import hufi.edu.vn.ungdunggoimonan.R;
import hufi.edu.vn.ungdunggoimonan.ui.BanAn;
import hufi.edu.vn.ungdunggoimonan.ui.MonAn;

public class QLMonAnFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private List<MonAn> lstMonAn = new ArrayList<>();
    private MyQLMonAnRecyclerViewAdapter myQLMonAnRecyclerViewAdapter;
    public QLMonAnFragment() {
    }

    public static QLMonAnFragment newInstance(int columnCount) {
        QLMonAnFragment fragment = new QLMonAnFragment();
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
        View view = inflater.inflate(R.layout.fragment_qlmon_an_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            loadMonAn();

            myQLMonAnRecyclerViewAdapter = new MyQLMonAnRecyclerViewAdapter(getContext(),(QuanLyMonAnFragment) getParentFragment(),lstMonAn);
            recyclerView.setAdapter(myQLMonAnRecyclerViewAdapter);
        }
        return view;
    }

    private void loadMonAn() {
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
                myQLMonAnRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi Load Bàn Ăn!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}