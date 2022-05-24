package hufi.edu.vn.ungdunggoimonan;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hufi.edu.vn.ungdunggoimonan.QuanTri.NhanVien;
import hufi.edu.vn.ungdunggoimonan.ui.BanAn;
import hufi.edu.vn.ungdunggoimonan.ui.ChiTietHoaDonFragment;
import hufi.edu.vn.ungdunggoimonan.ui.GoiMonAn;
import hufi.edu.vn.ungdunggoimonan.ui.MonAn;

public class ChiTietHoaDonActivity extends AppCompatActivity {
    private TextView lbTongTien,lbTenBan;
    private BanAn banAn;
    private List<MonAn> lstMonAn = new ArrayList<>();
    private Button btnXacNhan;
    private int chucVuNV;
    private NhanVien nhanVien;
    private GoiMonAn monAnDaGoi;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chi_tiet_hoa_don);
        anhXaView();
        loadDSMonAnDaGoi();

        loadTieuDeBtnXacNhan();

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //lấy ngày tháng năm hiện tại
                Calendar cal = Calendar.getInstance();
                String ngayThangNam = String.valueOf(cal.get(Calendar.DAY_OF_MONTH))+"-"+String.valueOf(cal.get(Calendar.MONTH))+"-"+String.valueOf(cal.get(Calendar.YEAR));

                //chucvu = 1 => nhân viên bồi bàn
                //trạng thái bàn 1 => bàn trống
                if(chucVuNV==1 && banAn.getTrangThai()==1){
                    //set trạng thái bàn đã gọi món
                    banAn.setTrangThai(2);

                    //thêm
                    DatabaseReference dbGoiMon = database.getReference("GoiMonAn/"+ngayThangNam);
                    //gán goimon
                    String idGoiMon = dbGoiMon.push().getKey();
                    GoiMonAn goiMon = new GoiMonAn(idGoiMon,banAn,lstMonAn,tongTien(),false);
                    //lưu gọi món
                    dbGoiMon.child(goiMon.getiD()).setValue(goiMon);

                    //sửa lại trạng thái bàn ăn
                    DatabaseReference myRefBanAn = database.getReference("BanAn");
                    myRefBanAn.child(String.valueOf(banAn.getId())).setValue(banAn);

                    Toast.makeText(ChiTietHoaDonActivity.this, "Đặt Món Ăn Thành Công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChiTietHoaDonActivity.this,MainActivity.class);
                    intent.putExtra("NhanVienDangNhap",nhanVien);
                    startActivity(intent);
                }

                //chucvu = 2 => đầu bếp
                //trạng thái bàn 2 => đang gọi món
                else if(chucVuNV==2 && banAn.getTrangThai()==2){
                    //xác nhận đã chuẩn bị xong món ăn
                    DatabaseReference dbGoiMon = database.getReference("GoiMonAn/"+ngayThangNam);
                    banAn.setTrangThai(3);
                    monAnDaGoi.setBanAn(banAn);
                    dbGoiMon.child(monAnDaGoi.getiD()).setValue(monAnDaGoi);

                    //sửa lại trạng thái bàn ăn
                    DatabaseReference myRefBanAn = database.getReference("BanAn");
                    myRefBanAn.child(String.valueOf(banAn.getId())).setValue(banAn);

                    Toast.makeText(ChiTietHoaDonActivity.this, "Xác nhận đã chuẩn bị xong món ăn!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChiTietHoaDonActivity.this,MainActivity.class);
                    intent.putExtra("NhanVienDangNhap",nhanVien);
                    startActivity(intent);
                }

                //chucvu = 1 => nhân viên bồi bàn
                //trạng thái bàn 3 => đầu bếp đã chuẩn bị xong các món ăn
                // => bồi bàn vào giao cho khách
                else if(chucVuNV==1&&banAn.getTrangThai()==3){
                    //xác nhận đã giao món ăn cho khách
                    DatabaseReference dbGoiMon = database.getReference("GoiMonAn/"+ngayThangNam);
                    banAn.setTrangThai(4);
                    monAnDaGoi.setBanAn(banAn);
                    dbGoiMon.child(monAnDaGoi.getiD()).setValue(monAnDaGoi);

                    //sửa lại trạng thái bàn ăn
                    DatabaseReference myRefBanAn = database.getReference("BanAn");
                    myRefBanAn.child(String.valueOf(banAn.getId())).setValue(banAn);

                    Toast.makeText(ChiTietHoaDonActivity.this, "Xác nhận đã giao món ăn!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChiTietHoaDonActivity.this,MainActivity.class);
                    intent.putExtra("NhanVienDangNhap",nhanVien);
                    startActivity(intent);
                }

                //chucvu = 1 => nhân viên bồi bàn
                //trạng thái bàn 4 => bàn chưa thanh toán
                // => bồi bàn xác nhận thanh toán
                else if(chucVuNV==1&&banAn.getTrangThai()==4){
                    //xác nhận đã thanh toán
                    DatabaseReference dbGoiMon = database.getReference("GoiMonAn/"+ngayThangNam);
                    banAn.setTrangThai(1);
                    monAnDaGoi.setBanAn(banAn);
                    monAnDaGoi.setTrangThaiThanhToan(true);
                    dbGoiMon.child(monAnDaGoi.getiD()).setValue(monAnDaGoi);

                    //sửa lại trạng thái bàn ăn
                    DatabaseReference myRefBanAn = database.getReference("BanAn");
                    myRefBanAn.child(String.valueOf(banAn.getId())).setValue(banAn);

                    Toast.makeText(ChiTietHoaDonActivity.this, "Đã xác nhận thanh toán!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChiTietHoaDonActivity.this,MainActivity.class);
                    intent.putExtra("NhanVienDangNhap",nhanVien);
                    startActivity(intent);
                }
            }
        });

    }

    private void anhXaView() {
        btnXacNhan = findViewById(R.id.btnXacNhan);
        lbTenBan = findViewById(R.id.lbChiTiet_TenBan);
        lbTongTien = findViewById(R.id.lbTongTien);

        nhanVien = (NhanVien)getIntent().getSerializableExtra("NhanVien");
        chucVuNV = nhanVien.getChucVu().getId();
        banAn = (BanAn) getIntent().getSerializableExtra("BanAnGoiMon");
        lbTenBan.setText(banAn.getTenBan());

        try{
            monAnDaGoi = (GoiMonAn) getIntent().getSerializableExtra("MonAnDaGoi");
        }catch (Exception ex){

        }
    }

    private void loadTieuDeBtnXacNhan() {
        //chucvu = 1 => nhân viên bồi bàn
        //trạng thái bàn trống
        if(chucVuNV==1 && banAn.getTrangThai()==1){
            btnXacNhan.setText("Xác nhận");
            return;
        }
        //trạng thái bàn đang gọi món
        if(chucVuNV==1 && banAn.getTrangThai()==2){
            btnXacNhan.setVisibility(View.INVISIBLE);
            btnXacNhan.setWidth(0);
            btnXacNhan.setHeight(0);
            return;
        }
        //trạng thái bàn đã chuẩn bị xong món ăn
        if(chucVuNV==1 && banAn.getTrangThai()==3){
            btnXacNhan.setText("Giao");
            return;
        }
        //trạng thái bàn chờ thanh toán
        if(chucVuNV==1 && banAn.getTrangThai()==4){
            btnXacNhan.setText("Thanh Toán");
            return;
        }

        //chucvu = 2 => đầu bếp
        //trạng thái bàn đang chuẩn bị
        if(chucVuNV==2 && banAn.getTrangThai()==2){
            btnXacNhan.setText("Chuẩn Bị Xong");
            return;
        }

        //chucvu = 2 => đầu bếp
        //trạng thái bàn khác đang chuẩn bị
        if(chucVuNV==2 && banAn.getTrangThai()!=2){
            btnXacNhan.setVisibility(View.INVISIBLE);
            return;
        }

    }

    private void loadDSMonAnDaGoi() {
        //nếu đặt món cho bàn trống thì sẽ lấy được lstMonAn
        lstMonAn = (List<MonAn>) getIntent().getSerializableExtra("ListMonAn");

        ChiTietHoaDonFragment chiTietHoaDonFragment = (ChiTietHoaDonFragment) getSupportFragmentManager().findFragmentById(R.id.chiTietCacMonAnDaDat);
        chiTietHoaDonFragment.setLstMonAn(lstMonAn);
        lbTongTien.setText("Tổng Tiền: "+String.valueOf(tongTien()));
    }

    private void loadDSMonAn_FireBase(String getiD) {
        DatabaseReference dbCTGoiMon = database.getReference("ChiTietGoiMon");
        dbCTGoiMon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    MonAn mA = postSnapshot.getValue(MonAn.class);
                    lstMonAn.add(mA);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChiTietHoaDonActivity.this, "Lỗi load chi tiết gọi món!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private double tongTien(){
        double tt = 0;
        for (MonAn monAn : lstMonAn) {
            tt += monAn.getSoLuong() * monAn.getGia();
        }
        return tt;
    }

    public BanAn getBanAn() {
        return banAn;
    }

    public void setBanAn(BanAn banAn) {
        this.banAn = banAn;
    }
}