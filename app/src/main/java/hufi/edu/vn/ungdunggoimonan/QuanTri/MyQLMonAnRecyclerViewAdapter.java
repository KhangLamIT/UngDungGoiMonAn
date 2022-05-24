package hufi.edu.vn.ungdunggoimonan.QuanTri;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hufi.edu.vn.ungdunggoimonan.R;
import hufi.edu.vn.ungdunggoimonan.ui.ItemClickListener;
import hufi.edu.vn.ungdunggoimonan.ui.MonAn;

import java.util.List;

public class MyQLMonAnRecyclerViewAdapter extends RecyclerView.Adapter<MyQLMonAnRecyclerViewAdapter.ViewHolder> {

    private final List<MonAn> lstMonAn;
    private Context context;
    private QuanLyMonAnFragment quanLyMonAnFragment;
    public MyQLMonAnRecyclerViewAdapter(Context context,QuanLyMonAnFragment quanLyMonAnFragment,List<MonAn> items) {
        lstMonAn = items;
        this.context = context;
        this.quanLyMonAnFragment = quanLyMonAnFragment;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_qlmon_an, parent, false);
        return new MyQLMonAnRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MonAn monAn = lstMonAn.get(position);
        holder.lbTenMonAn.setText(monAn.getTenMonAn());
        holder.lbGia.setText(String.valueOf(monAn.getGia()));
        Picasso.with(context)
                .load(monAn.getHinh())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgMA);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                quanLyMonAnFragment.loadThongTinMA(monAn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstMonAn.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView lbTenMonAn,lbGia;
        public ImageView imgMA;
        public ItemClickListener itemClickListener;

        public ViewHolder(View view) {
            super(view);
            lbTenMonAn = view.findViewById(R.id.QLMonAn_TenMonAn);
            lbGia = view.findViewById(R.id.QLMonAn_GiaMonAn);
            imgMA = view.findViewById(R.id.imgQLMonAn);
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