package hufi.edu.vn.ungdunggoimonan.ui;

import java.io.Serializable;
import java.util.List;

public class GoiMonAn implements Serializable {
    private String iD;
    private BanAn banAn;
    private List<MonAn> danhSachMonAnGoi;
    private double tongTien;
    private boolean trangThaiThanhToan;

    public GoiMonAn() {
    }

    public GoiMonAn(String iD, BanAn banAn, List<MonAn> danhSachMonAnGoi, double tongTien, boolean trangThaiThanhToan) {
        this.iD = iD;
        this.banAn = banAn;
        this.danhSachMonAnGoi = danhSachMonAnGoi;
        this.tongTien = tongTien;
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public BanAn getBanAn() {
        return banAn;
    }

    public void setBanAn(BanAn banAn) {
        this.banAn = banAn;
    }

    public List<MonAn> getDanhSachMonAnGoi() {
        return danhSachMonAnGoi;
    }

    public void setDanhSachMonAnGoi(List<MonAn> danhSachMonAnGoi) {
        this.danhSachMonAnGoi = danhSachMonAnGoi;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public boolean isTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(boolean trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }
}
