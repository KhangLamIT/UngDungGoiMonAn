package hufi.edu.vn.ungdunggoimonan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hufi.edu.vn.ungdunggoimonan.QuanTri.NhanVien;
import hufi.edu.vn.ungdunggoimonan.ui.BanAn;
import hufi.edu.vn.ungdunggoimonan.ui.MonAn;
import hufi.edu.vn.ungdunggoimonan.ui.MonAnFragment;

public class ChonMonAnActivity extends AppCompatActivity {
    private TextView lbSoLuong,lbTenBan;
    private Button btnXemHoaDon;
    private BanAn banAn;
    private NhanVien nhanVien;
    private List<MonAn> lstMonAn = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chon_mon_an);
        lbSoLuong = findViewById(R.id.lbSoLuong);
        lbTenBan = findViewById(R.id.lbChonMon_TenBan);
        capNhatSoLuong();
        nhanVien = (NhanVien) getIntent().getSerializableExtra("NhanVien");
        banAn = (BanAn) getIntent().getSerializableExtra("BanAnGoiMon");
        lbTenBan.setText(banAn.getTenBan());

        btnXemHoaDon = findViewById(R.id.btnXemHoaDon);
        btnXemHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(demSoLuong()==0){
                    Toast.makeText(ChonMonAnActivity.this, "Vui lòng chọn món ăn!", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<MonAn> monAnDaChon = new ArrayList<>();
                for (MonAn monAn:lstMonAn) {
                    if(monAn.getSoLuong()>0)
                        monAnDaChon.add(monAn);
                }
                Intent intent = new Intent(ChonMonAnActivity.this,ChiTietHoaDonActivity.class);
                intent.putExtra("ListMonAn", (Serializable) monAnDaChon);
                intent.putExtra("BanAnGoiMon",banAn);
                intent.putExtra("NhanVien",nhanVien);
                startActivity(intent);
            }
        });
    }

    public void loadMonAnTheoLoai(String idLoai){
        MonAnFragment monAnFragment = (MonAnFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentChonMonAn);
        if(idLoai.equals("loadfull")){
            monAnFragment.loadMonAnAll_CapNhatSoLuong(lstMonAn);
        }
        else{
            monAnFragment.loadMonAnTheoLoai_CapNhatSoLuong(idLoai,lstMonAn);
        }
    }

    public int demSoLuong(){
        int dem = 0;
        for (MonAn monAn :lstMonAn){
            dem+=monAn.getSoLuong();
        }
        return dem;
    }

    public void themMonAnDaChon(MonAn monAn){
        //kiểm tra món ăn đã được thêm chưa
        int kt = 0;
        for (int i = 0; i<lstMonAn.stream().count();i++){
            if(lstMonAn.get(i).getIDMonAn().equals(monAn.getIDMonAn())){
                //cập nhật số lượng cho món ăn đó
                lstMonAn.get(i).setSoLuong(monAn.getSoLuong());

                //có món ăn đã gọi
                kt=1;
                break;
            }
        }
        //nếu món ăn chưa có thì thêm vào
        if(kt==0){
            lstMonAn.add(monAn);
        }
    }

    public void capNhatSoLuong(){
        lbSoLuong.setText(String.valueOf("Số Lượng: " + demSoLuong()));
    }
}