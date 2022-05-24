package hufi.edu.vn.ungdunggoimonan.ui;

import java.io.Serializable;

public class BanAn implements Serializable {
    private int Id;
    private String TenBan;
    private int TrangThai;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTenBan() {
        return TenBan;
    }

    public void setTenBan(String tenBan) {
        TenBan = tenBan;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    public BanAn(int id) {
        Id = id;
    }

    public BanAn(int id, String tenBan, int trangThai) {
        Id = id;
        TenBan = tenBan;
        TrangThai = trangThai;
    }

    public BanAn() {
    }
}
