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
import java.util.NoSuchElementException;

import hufi.edu.vn.ungdunggoimonan.R;
import hufi.edu.vn.ungdunggoimonan.ui.BanAn;

public class QLBanAnFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private List<BanAn> lstBanAn = new ArrayList<>();
    private MyQLBanAnRecyclerViewAdapter myQLBanAnRecyclerViewAdapter;

    public QLBanAnFragment() {
    }

    public static QLBanAnFragment newInstance(int columnCount) {
        QLBanAnFragment fragment = new QLBanAnFragment();
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
        View view = inflater.inflate(R.layout.fragment_qlban_an_list, container, false);

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
            myQLBanAnRecyclerViewAdapter = new MyQLBanAnRecyclerViewAdapter(getContext(),(QuanLyBanAnFragment)getParentFragment(),lstBanAn);
            recyclerView.setAdapter(myQLBanAnRecyclerViewAdapter);
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
                myQLBanAnRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi Load Bàn Ăn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getIdBanAnMax(){
        if(lstBanAn.stream().count()==0)
            return 0;
        return lstBanAn.stream().max(Comparator.comparingInt(BanAn::getId)).get().getId();
    }
}