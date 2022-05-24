package hufi.edu.vn.ungdunggoimonan.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hufi.edu.vn.ungdunggoimonan.R;

import java.util.List;

public class MyChiTietHoaDonRecyclerViewAdapter extends RecyclerView.Adapter<MyChiTietHoaDonRecyclerViewAdapter.ViewHolder> {
    private final List<MonAn> lstMonAn;
    private Context context;
    public MyChiTietHoaDonRecyclerViewAdapter(Context context,List<MonAn> items) {
        lstMonAn = items;
        this.context = context;
    }

    @Override
    public MyChiTietHoaDonRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chitiethoadon, parent, false);
        return new MyChiTietHoaDonRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyChiTietHoaDonRecyclerViewAdapter.ViewHolder holder, int position) {
        MonAn monAn = lstMonAn.get(position);
        holder.lbTenMonAn.setText(monAn.getTenMonAn());
        holder.lbGia.setText(String.valueOf(monAn.getGia()));
        holder.lbSoLuong.setText(String.valueOf(monAn.getSoLuong()));
        Picasso.with(context)
                .load(monAn.getHinh())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgChiTietMonAn);
    }

    @Override
    public int getItemCount() {
        return lstMonAn.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView lbTenMonAn;
        public TextView lbGia;
        public TextView lbSoLuong;
        public ImageView imgChiTietMonAn;

        public ViewHolder(View view) {
            super(view);
            lbTenMonAn = view.findViewById(R.id.lbChiTiet_TenMonAn);
            lbGia = view.findViewById(R.id.lbChiTiet_GiaMonAn);
            lbSoLuong = view.findViewById(R.id.lbChiTiet_SoLuong);
            imgChiTietMonAn = view.findViewById(R.id.imgChiTietMonAn);
        }

    }
}