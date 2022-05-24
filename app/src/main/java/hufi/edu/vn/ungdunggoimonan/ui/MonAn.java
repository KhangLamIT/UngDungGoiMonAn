package hufi.edu.vn.ungdunggoimonan.ui;

import java.io.Serializable;

public class MonAn implements Serializable {
    private String IDMonAn;
    private String TenMonAn;
    private Double Gia;
    private String Hinh;
    private String idLoaiMonAn;
    private int soLuong;

    public MonAn(String IDMonAn, String tenMonAn, Double gia, String hinh, String idLoaiMonAn) {
        this.IDMonAn = IDMonAn;
        TenMonAn = tenMonAn;
        Gia = gia;
        Hinh = hinh;
        this.idLoaiMonAn = idLoaiMonAn;
        soLuong = 0;
    }

    public MonAn(String IDMonAn, String tenMonAn, Double gia, String hinh, String idLoaiMonAn, int soLuong) {
        this.IDMonAn = IDMonAn;
        TenMonAn = tenMonAn;
        Gia = gia;
        Hinh = hinh;
        this.idLoaiMonAn = idLoaiMonAn;
        this.soLuong = soLuong;
    }

    public MonAn() {
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getHinh() {
        return Hinh;
    }

    public void setHinh(String hinh) {
        Hinh = hinh;
    }

    public String getIDMonAn() {
        return IDMonAn;
    }

    public void setIDMonAn(String IDMonAn) {
        this.IDMonAn = IDMonAn;
    }

    public String getTenMonAn() {
        return TenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        TenMonAn = tenMonAn;
    }

    public Double getGia() {
        return Gia;
    }

    public void setGia(Double gia) {
        Gia = gia;
    }

    public String getIdLoaiMonAn() {
        return idLoaiMonAn;
    }

    public void setIdLoaiMonAn(String idLoaiMonAn) {
        this.idLoaiMonAn = idLoaiMonAn;
    }
}
