package hufi.edu.vn.ungdunggoimonan.ui;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class LoaiMonAn implements Serializable {
    private String id;
    private String tenLoai;
    private String hinh;

    public LoaiMonAn(String id, String tenLoai, String hinh) {
        this.id = id;
        this.tenLoai = tenLoai;
        this.hinh = hinh;
    }

    public LoaiMonAn() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    @NonNull
    @Override
    public String toString() {
        return tenLoai;
    }
}
