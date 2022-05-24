package hufi.edu.vn.ungdunggoimonan.QuanTri;

import java.io.Serializable;

public class NhanVien implements Serializable {
    private int Id;
    private String HoTen;
    private String SoDT;
    private ChucVu chucVu;
    private String MatKhau;
    private String Hinh;
    private boolean TrangThai;

    public String getHinh() {
        return Hinh;
    }

    public void setHinh(String hinh) {
        Hinh = hinh;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getSoDT() {
        return SoDT;
    }

    public void setSoDT(String soDT) {
        SoDT = soDT;
    }

    public ChucVu getChucVu() {
        return chucVu;
    }

    public void setChucVu(ChucVu chucVu) {
        this.chucVu = chucVu;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean trangThai) {
        TrangThai = trangThai;
    }

    public NhanVien() {
    }

    public NhanVien(String hoTen) {
        HoTen = hoTen;
    }

    public NhanVien(int id, String hoTen, String soDT, ChucVu chucVu, String matKhau, String hinh, boolean trangThai) {
        Id = id;
        HoTen = hoTen;
        SoDT = soDT;
        this.chucVu = chucVu;
        MatKhau = matKhau;
        Hinh = hinh;
        TrangThai = trangThai;
    }

    public NhanVien(int id, String hoTen, String soDT, ChucVu chucVu, String matKhau, boolean trangThai) {
        Id = id;
        HoTen = hoTen;
        SoDT = soDT;
        this.chucVu = chucVu;
        MatKhau = matKhau;
        TrangThai = trangThai;
    }


}
