package hufi.edu.vn.ungdunggoimonan.QuanTri;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import hufi.edu.vn.ungdunggoimonan.R;
import hufi.edu.vn.ungdunggoimonan.ui.BanAn;
import hufi.edu.vn.ungdunggoimonan.ui.ItemClickListener;
import java.util.List;

public class MyQLBanAnRecyclerViewAdapter extends RecyclerView.Adapter<MyQLBanAnRecyclerViewAdapter.ViewHolder> {

    private final List<BanAn> lstBanAn;
    private Context context;
    private QuanLyBanAnFragment quanLyBanAnFragment;
    public MyQLBanAnRecyclerViewAdapter(Context context,QuanLyBanAnFragment quanLyBanAnFragment,List<BanAn> lstBanAn) {
        this.lstBanAn = lstBanAn;
        this.context = context;
        this.quanLyBanAnFragment = quanLyBanAnFragment;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_qlban_an, parent, false);
        return new MyQLBanAnRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BanAn banAn = lstBanAn.get(position);
        holder.lbTenBanAn.setText(banAn.getTenBan());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                quanLyBanAnFragment.loadTenBan(banAn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstBanAn.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView lbTenBanAn;
        public ItemClickListener itemClickListener;
        public ViewHolder(View view) {
            super(view);
            lbTenBanAn = view.findViewById(R.id.lbQLTenBan);
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