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

import hufi.edu.vn.ungdunggoimonan.MainActivity;
import hufi.edu.vn.ungdunggoimonan.QuanTri.MyQLBanAnRecyclerViewAdapter;
import hufi.edu.vn.ungdunggoimonan.QuanTri.NhanVien;
import hufi.edu.vn.ungdunggoimonan.R;

public class BanAnFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private List<BanAn> lstBanAn = new ArrayList<>();
    private MyBanAnRecyclerViewAdapter myBanAnRecyclerViewAdapter;

    public BanAnFragment() {
    }

    public static BanAnFragment newInstance(int columnCount) {
        BanAnFragment fragment = new BanAnFragment();
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
        View view = inflater.inflate(R.layout.fragment_ban_an_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            loadBanAn();
            myBanAnRecyclerViewAdapter = new MyBanAnRecyclerViewAdapter(getContext(),lstBanAn);
            recyclerView.setAdapter(myBanAnRecyclerViewAdapter);
        }
        return view;
    }

    private void loadBanAn() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("BanAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstBanAn!=null)
                    lstBanAn.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    BanAn banAn = postSnapshot.getValue(BanAn.class);
                    lstBanAn.add(banAn);
                }
                myBanAnRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi Load Bàn Ăn!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}