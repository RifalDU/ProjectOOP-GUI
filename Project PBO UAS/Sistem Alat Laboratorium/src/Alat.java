public class Alat {
    private int id;
    private String nama;
    private int stok;

    public Alat(int id, String nama, int stok) {
        this.id = id;
        this.nama = nama;
        this.stok = stok;
    }
    public int getId() {
        return id;
    }
    public String getNama() {
        return nama;
    }
    public int getStok() {
        return stok;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setStok(int stok) {
        this.stok = stok;
    }
    @Override
    public String toString() {
        return "Alat{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", stok=" + stok +
                '}';
    }
}
