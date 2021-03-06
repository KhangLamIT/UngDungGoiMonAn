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
import java.util.Calendar;
import java.util.List;

import hufi.edu.vn.ungdunggoimonan.ChiTietHoaDonActivity;
import hufi.edu.vn.ungdunggoimonan.LoginActivity;
import hufi.edu.vn.ungdunggoimonan.MainActivity;
import hufi.edu.vn.ungdunggoimonan.QuanTri.NhanVien;
import hufi.edu.vn.ungdunggoimonan.R;

public class ChiTietHoaDonFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private MyChiTietHoaDonRecyclerViewAdapter myChiTietHoaDonRecyclerViewAdapter;
    private List<MonAn> lstMonAn;

    public List<MonAn> getLstMonAn() {
        return lstMonAn;
    }

    public void setLstMonAn(List<MonAn> lstMonAn) {
        this.lstMonAn = lstMonAn;
    }

    public ChiTietHoaDonFragment() {

    }

    public static ChiTietHoaDonFragment newInstance(int columnCount) {
        ChiTietHoaDonFragment fragment = new ChiTietHoaDonFragment();
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
        View view = inflater.inflate(R.layout.fragment_chitiethoadon_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            myChiTietHoaDonRecyclerViewAdapter = new MyChiTietHoaDonRecyclerViewAdapter(getContext(),lstMonAn);
            recyclerView.setAdapter(myChiTietHoaDonRecyclerViewAdapter);
        }
        return view;
    }
}