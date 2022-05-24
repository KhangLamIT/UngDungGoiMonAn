package hufi.edu.vn.ungdunggoimonan.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import hufi.edu.vn.ungdunggoimonan.ChonMonAnActivity;
import hufi.edu.vn.ungdunggoimonan.R;

import java.util.List;

public class MyMonAnRecyclerViewAdapter extends RecyclerView.Adapter<MyMonAnRecyclerViewAdapter.ViewHolder> {
    private final List<MonAn> lstMonAn;
    private Context context;
    public MyMonAnRecyclerViewAdapter(Context context,List<MonAn> items) {
        lstMonAn = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_monan, parent, false);
        return new MyMonAnRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MonAn monAn = lstMonAn.get(position);
        holder.lbMonAn.setText(monAn.getTenMonAn());
        Picasso.with(context)
                .load(monAn.getHinh())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgMonAn);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                showDialogChonSL(position);
            }
        });

    }

    public void showDialogChonSL(int i)
    {
        final Dialog d = new Dialog(context);
        d.setTitle("Chọn Số Lượng");
        d.setContentView(R.layout.dialog);
        Button btnHuy = (Button) d.findViewById(R.id.btnHuy);
        Button btnChon = (Button) d.findViewById(R.id.btnChon);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.chonSoLuongMon);
        np.setMaxValue(10); // max 10
        np.setValue(lstMonAn.get(i).getSoLuong());
        np.setMinValue(0);   // min 0
        np.setWrapSelectorWheel(false);
        btnChon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                lstMonAn.get(i).setSoLuong(np.getValue());
                ChonMonAnActivity chonMonAnActivity = (ChonMonAnActivity) context;
                chonMonAnActivity.themMonAnDaChon(lstMonAn.get(i));
                chonMonAnActivity.capNhatSoLuong();
                d.dismiss();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public int getItemCount() {
        return lstMonAn.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView lbMonAn;
        public ImageView imgMonAn;
        public ItemClickListener itemClickListener;

        public ViewHolder(View view) {
            super(view);
            lbMonAn = view.findViewById(R.id.lbMonAn);
            imgMonAn = view.findViewById(R.id.imgMonAn);
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