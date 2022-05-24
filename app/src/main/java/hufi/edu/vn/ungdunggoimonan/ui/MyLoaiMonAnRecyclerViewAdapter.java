package hufi.edu.vn.ungdunggoimonan.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import hufi.edu.vn.ungdunggoimonan.ChonMonAnActivity;
import hufi.edu.vn.ungdunggoimonan.R;

public class MyLoaiMonAnRecyclerViewAdapter extends RecyclerView.Adapter<MyLoaiMonAnRecyclerViewAdapter.ViewHolder> {

    private List<LoaiMonAn> loaiMonAns;
    private Context context;
    public MyLoaiMonAnRecyclerViewAdapter(Context context, List<LoaiMonAn> loaiMonAns) {
        this.loaiMonAns = loaiMonAns;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_loai_mon_an, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LoaiMonAn loaiMonAn = loaiMonAns.get(position);
        Picasso.with(context)
                .load(loaiMonAn.getHinh())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgLoaiMonAn);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ChonMonAnActivity chonMonAnActivity = (ChonMonAnActivity) context;
                chonMonAnActivity.loadMonAnTheoLoai(loaiMonAn.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return loaiMonAns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imgLoaiMonAn;
        public ItemClickListener itemClickListener;

        public ViewHolder(View view) {
            super(view);
            imgLoaiMonAn = view.findViewById(R.id.imgLoaiMonAn);
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