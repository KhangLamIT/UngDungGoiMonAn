package hufi.edu.vn.ungdunggoimonan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import hufi.edu.vn.ungdunggoimonan.QuanTri.NhanVien;
import hufi.edu.vn.ungdunggoimonan.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Button btnDangXuat;
    private TextView lbTenNV;
    private NhanVien nhanVienDangNhap;
    private ImageView imglogoNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_BanAn,R.id.nav_ThongTinNV,R.id.nav_QLBanAn,R.id.nav_QLMonAn,R.id.nav_QLNhanVien)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //phân quyền
        phanQuyen(navigationView, navController);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        getSupportFragmentManager();
    }

    private void phanQuyen(NavigationView navigationView,NavController navController) {
        Menu menu = navigationView.getMenu();
        nhanVienDangNhap = (NhanVien)getIntent().getSerializableExtra("NhanVienDangNhap");
        //chức vụ admin
        if(nhanVienDangNhap.getChucVu().getId()==3){
            //mặc định hiện admin_navigation
            navController.setGraph(R.navigation.admin_navigation);

            MenuItem menuItemQLNV = menu.findItem(R.id.nav_QLNhanVien);
            menuItemQLNV.setVisible(true);
            MenuItem menuItemQLBA = menu.findItem(R.id.nav_QLBanAn);
            menuItemQLBA.setVisible(true);
            MenuItem menuItemQLMA = menu.findItem(R.id.nav_QLMonAn);
            menuItemQLMA.setVisible(true);

            //ẩn các chức năng của nhân viên
            MenuItem menuItemBA = menu.findItem(R.id.nav_BanAn);
            menuItemBA.setVisible(false);
            MenuItem menuItemTTNV = menu.findItem(R.id.nav_ThongTinNV);
            menuItemTTNV.setVisible(false);
        }
        else {
            navController.setGraph(R.navigation.mobile_navigation);

            MenuItem menuItemBA = menu.findItem(R.id.nav_BanAn);
            menuItemBA.setVisible(true);
            MenuItem menuItemTTNV = menu.findItem(R.id.nav_ThongTinNV);
            menuItemTTNV.setVisible(true);

            //ẩn các chức năng của admin
            MenuItem menuItemQLNV = menu.findItem(R.id.nav_QLNhanVien);
            menuItemQLNV.setVisible(false);
            MenuItem menuItemQLBA = menu.findItem(R.id.nav_QLBanAn);
            menuItemQLBA.setVisible(false);
            MenuItem menuItemQLMA = menu.findItem(R.id.nav_QLMonAn);
            menuItemQLMA.setVisible(false);
        }
    }

    public NhanVien getNhanVienDangNhap() {
        return nhanVienDangNhap;
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //set view
        lbTenNV = findViewById(R.id.lbTenNV);
        lbTenNV.setText(nhanVienDangNhap.getHoTen());
        imglogoNV = findViewById(R.id.imglogoNV);
        Picasso.with(MainActivity.this)
                .load(nhanVienDangNhap.getHinh())
                .placeholder(R.mipmap.ic_launcher)
                .into(imglogoNV);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}