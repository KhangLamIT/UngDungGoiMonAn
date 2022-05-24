package hufi.edu.vn.ungdunggoimonan.QuanTri;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hufi.edu.vn.ungdunggoimonan.R;
import hufi.edu.vn.ungdunggoimonan.ui.ItemClickListener;
import hufi.edu.vn.ungdunggoimonan.ui.LoaiMonAn;
import hufi.edu.vn.ungdunggoimonan.ui.MonAn;

import java.util.List;

public class MyQLNhanVienRecyclerViewAdapter extends RecyclerView.Adapter<MyQLNhanVienRecyclerViewAdapter.ViewHolder> {

    private final List<NhanVien> lstNhanVien;
    private Context context;
    private QuanLyNhanVienFragment quanLyNhanVienFragment;
    public MyQLNhanVienRecyclerViewAdapter(Context context,QuanLyNhanVienFragment quanLyNhanVienFragment,List<NhanVien> items) {
        lstNhanVien = items;
        this.context = context;
        this.quanLyNhanVienFragment = quanLyNhanVienFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_qlnhan_vien, parent, false);
        return new MyQLNhanVienRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NhanVien nhanVien = lstNhanVien.get(position);
        holder.lbTenNV.setText(nhanVien.getHoTen());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                quanLyNhanVienFragment.loadNhanVien(nhanVien);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstNhanVien.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView lbTenNV;
        public ItemClickListener itemClickListener;
        public ViewHolder(View view) {
            super(view);
            lbTenNV = view.findViewById(R.id.QLNhanVien_TenNV);
            view.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAbsoluteAdapterPosition());
        }
    }
}