import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistem Peminjaman & Pengembalian Alat Lab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);

        // --- Form Peminjaman
        JLabel lblNamaPeminjam = new JLabel("Nama Peminjam:");
        JTextField tfNamaPeminjam = new JTextField(20);

        JLabel lblNamaAlat = new JLabel("Nama Alat:");
        JTextField tfNamaAlat = new JTextField(20);

        JButton btnPinjam = new JButton("Pinjam Alat");

        // --- Form Pengembalian
        JLabel lblIDPeminjaman = new JLabel("ID Peminjaman:");
        JTextField tfIDPeminjaman = new JTextField(10);
        JButton btnKembali = new JButton("Kembalikan Alat");

        // --- Panel Layout
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(lblNamaPeminjam);
        panel.add(tfNamaPeminjam);
        panel.add(lblNamaAlat);
        panel.add(tfNamaAlat);
        panel.add(new JLabel());
        panel.add(btnPinjam);
        panel.add(new JLabel("---- Pengembalian ----"));
        panel.add(new JLabel());
        panel.add(lblIDPeminjaman);
        panel.add(tfIDPeminjaman);
        panel.add(new JLabel());
        panel.add(btnKembali);

        frame.add(panel);
        frame.setVisible(true);

        // --- Action Peminjaman
        btnPinjam.addActionListener(e -> {
            String namaPeminjam = tfNamaPeminjam.getText().trim();
            String namaAlat = tfNamaAlat.getText().trim();

            if (namaPeminjam.isEmpty() || namaAlat.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nama peminjam dan alat harus diisi!");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                // Cek alat
                PreparedStatement psAlat = conn.prepareStatement("SELECT id, stok FROM alat WHERE nama=?");
                psAlat.setString(1, namaAlat);
                ResultSet rsAlat = psAlat.executeQuery();

                if (rsAlat.next()) {
                    int idAlat = rsAlat.getInt("id");
                    int stok = rsAlat.getInt("stok");
                    if (stok > 0) {
                        // Cek/insert peminjam
                        PreparedStatement psPeminjam = conn.prepareStatement("SELECT id FROM peminjam WHERE nama=?");
                        psPeminjam.setString(1, namaPeminjam);
                        ResultSet rsPeminjam = psPeminjam.executeQuery();
                        int idPeminjam;
                        if (rsPeminjam.next()) {
                            idPeminjam = rsPeminjam.getInt("id");
                        } else {
                            PreparedStatement psInsertPeminjam = conn.prepareStatement("INSERT INTO peminjam (nama) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                            psInsertPeminjam.setString(1, namaPeminjam);
                            psInsertPeminjam.executeUpdate();
                            ResultSet rsKey = psInsertPeminjam.getGeneratedKeys();
                            rsKey.next();
                            idPeminjam = rsKey.getInt(1);
                        }
                        // Insert peminjaman
                        PreparedStatement psPinjam = conn.prepareStatement(
                            "INSERT INTO peminjaman (id_alat, id_peminjam, tanggal_pinjam, status) VALUES (?, ?, CURDATE(), 'dipinjam')", Statement.RETURN_GENERATED_KEYS);
                        psPinjam.setInt(1, idAlat);
                        psPinjam.setInt(2, idPeminjam);
                        psPinjam.executeUpdate();
                        ResultSet rsPinjamKey = psPinjam.getGeneratedKeys();
                        rsPinjamKey.next();
                        int idPeminjaman = rsPinjamKey.getInt(1);

                        // Update stok
                        PreparedStatement psUpdateStok = conn.prepareStatement("UPDATE alat SET stok=stok-1 WHERE id=?");
                        psUpdateStok.setInt(1, idAlat);
                        psUpdateStok.executeUpdate();

                        JOptionPane.showMessageDialog(frame, "Peminjaman berhasil! ID Peminjaman: " + idPeminjaman);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Stok alat habis!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Alat tidak ditemukan!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Terjadi kesalahan: " + ex.getMessage());
            }
        });

        // --- Action Pengembalian
        btnKembali.addActionListener(e -> {
            String idPeminjamanStr = tfIDPeminjaman.getText().trim();
            if (idPeminjamanStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "ID Peminjaman harus diisi!");
                return;
            }
            try (Connection conn = DBConnection.getConnection()) {
                // Cek peminjaman
                PreparedStatement psCek = conn.prepareStatement("SELECT id_alat, status FROM peminjaman WHERE id=?");
                psCek.setInt(1, Integer.parseInt(idPeminjamanStr));
                ResultSet rsCek = psCek.executeQuery();
                if (rsCek.next()) {
                    String status = rsCek.getString("status");
                    int idAlat = rsCek.getInt("id_alat");
                    if (status.equals("dipinjam")) {
                        // Update status dan tanggal kembali
                        PreparedStatement psUpdate = conn.prepareStatement(
                            "UPDATE peminjaman SET status='dikembalikan', tanggal_kembali=CURDATE() WHERE id=?");
                        psUpdate.setInt(1, Integer.parseInt(idPeminjamanStr));
                        psUpdate.executeUpdate();

                        // Update stok alat
                        PreparedStatement psUpdateStok = conn.prepareStatement("UPDATE alat SET stok=stok+1 WHERE id=?");
                        psUpdateStok.setInt(1, idAlat);
                        psUpdateStok.executeUpdate();

                        JOptionPane.showMessageDialog(frame, "Pengembalian berhasil!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Alat sudah dikembalikan sebelumnya!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "ID Peminjaman tidak ditemukan!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Terjadi kesalahan: " + ex.getMessage());
            }
        });
    }
}
