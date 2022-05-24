package hufi.edu.vn.ungdunggoimonan.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hufi.edu.vn.ungdunggoimonan.ChiTietHoaDonActivity;
import hufi.edu.vn.ungdunggoimonan.ChonMonAnActivity;
import hufi.edu.vn.ungdunggoimonan.MainActivity;
import hufi.edu.vn.ungdunggoimonan.QuanTri.ChucVu;
import hufi.edu.vn.ungdunggoimonan.QuanTri.NhanVien;
import hufi.edu.vn.ungdunggoimonan.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyBanAnRecyclerViewAdapter extends RecyclerView.Adapter<MyBanAnRecyclerViewAdapter.ViewHolder> {

    private final List<BanAn> lstBanAn;
    private Context context;
    private NhanVien nhanVien;

    public MyBanAnRecyclerViewAdapter(Context context,List<BanAn> lstBanAn) {
        this.lstBanAn = lstBanAn;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_ban_an, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BanAn banAn = lstBanAn.get(position);
        holder.lbTenBanAn.setText(banAn.getTenBan());
        //trạng thái 1: chưa gọi món
        if(banAn.getTrangThai()==1){
            holder.imgBanAn.setImageResource(R.drawable.iconbantrong);
            holder.lbTenBanAn.setTextColor(Color.BLACK);
        }
        //trạng thái 2: đã gọi món, đang chuẩn bị món
        if(banAn.getTrangThai()==2){
            holder.imgBanAn.setImageResource(R.drawable.iconbanando);
            holder.lbTenBanAn.setTextColor(Color.WHITE);
        }
        //trạng thái 3: đã gọi món, chuẩn bị xong
        if(banAn.getTrangThai()==3){
            holder.imgBanAn.setImageResource(R.drawable.iconbananxanh);
        }
        //trạng thái 4: khách đang ăn
        if(banAn.getTrangThai()==4){
            holder.imgBanAn.setImageResource(R.drawable.iconbanan);
        }

        MainActivity mainActivity = (MainActivity)context;
        nhanVien = mainActivity.getNhanVienDangNhap();

        //lấy danh sách goi món ăn từ firebase
        List<GoiMonAn> lstGoiMA = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        String ngayThangNam = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "-" + String.valueOf(cal.get(Calendar.MONTH)) + "-" + String.valueOf(cal.get(Calendar.YEAR));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbGoiMon = database.getReference("GoiMonAn/" + ngayThangNam);
        dbGoiMon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (lstGoiMA != null)
                    lstGoiMA.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    GoiMonAn GMA = postSnapshot.getValue(GoiMonAn.class);
                    lstGoiMA.add(GMA);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(banAn.getTrangThai()==1 && nhanVien.getChucVu().getId()==2){
                    return;
                }

                //trạng thái 1: chưa gọi món
                //và chỉ nhân viên bồi bàn được gọi món ăn
                if(banAn.getTrangThai()==1 && nhanVien.getChucVu().getId()==1){
                    //chuyển sang màn hình gọi món
                    Intent intent = new Intent(context, ChonMonAnActivity.class);
                    intent.putExtra("BanAnGoiMon",banAn);
                    intent.putExtra("NhanVien",nhanVien);
                    context.startActivity(intent);
                    return;
                }

                //lấy danh sách món ăn đã gọi
                GoiMonAn gma = new GoiMonAn();
                for (GoiMonAn GMA:lstGoiMA) {
                    if(GMA.getBanAn().getId()== banAn.getId()
                            && GMA.getBanAn().getTrangThai() == banAn.getTrangThai()
                            && !GMA.isTrangThaiThanhToan()) {
                        gma = GMA;
                        break;
                    }
                }
                //chuyển sang màn hình cho quản lý chuẩn bị món
                Intent intent = new Intent(context, ChiTietHoaDonActivity.class);
                intent.putExtra("BanAnGoiMon",banAn);
                List<MonAn> monAnDaChon = gma.getDanhSachMonAnGoi();
                intent.putExtra("ListMonAn", (Serializable) monAnDaChon);
                intent.putExtra("NhanVien",nhanVien);
                intent.putExtra("MonAnDaGoi",gma);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstBanAn.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       public TextView lbTenBanAn;
       public ImageView imgBanAn;
       public ItemClickListener itemClickListener;
        public ViewHolder(View view) {
            super(view);
            lbTenBanAn = view.findViewById(R.id.lbTenBan);
            imgBanAn = view.findViewById(R.id.imgBanAn);
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