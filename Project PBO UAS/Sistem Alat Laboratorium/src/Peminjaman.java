import java.util.Date;

public class Peminjaman {
    private int id;
    private int idAlat;
    private int idPeminjam;
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private String status;

    public Peminjaman(int id, int idAlat, int idPeminjam, Date tanggalPinjam, Date tanggalKembali, String status) {
        this.id = id;
        this.idAlat = idAlat;
        this.idPeminjam = idPeminjam;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public int getIdAlat() {
        return idAlat;
    }
    public int getIdPeminjam() {
        return idPeminjam;
    }
    public Date getTanggalPinjam() {
        return tanggalPinjam;
    }
    public Date getTanggalKembali() {
        return tanggalKembali;
    }
    public String getStatus() {
        return status;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setIdAlat(int idAlat) {
        this.idAlat = idAlat;
    }
    public void setIdPeminjam(int idPeminjam) {
        this.idPeminjam = idPeminjam;
    }
    public void setTanggalPinjam(Date tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }
    public void setTanggalKembali(Date tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
