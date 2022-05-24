package hufi.edu.vn.ungdunggoimonan.QuanTri;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ChucVu implements Serializable {
    private int Id;
    private String TenChucVu;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTenChucVu() {
        return TenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        TenChucVu = tenChucVu;
    }

    public ChucVu() {
    }

    public ChucVu(int id, String tenChucVu) {
        Id = id;
        TenChucVu = tenChucVu;
    }

    @NonNull
    @Override
    public String toString() {
        return TenChucVu;
    }
}
