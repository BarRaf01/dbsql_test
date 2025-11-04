import java.sql.*;
import java.util.Scanner;

public class Main {

    // Konfigurasi koneksi
    static final String URL = "jdbc:mysql://localhost:3306/db_test";
    static final String USER = "root";
    static final String PASSWORD = "root"; // isi sesuai password MySQL kamu

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println(" Koneksi ke MySQL berhasil!");
            Scanner input = new Scanner(System.in);
            int pilihan;

            do {
                System.out.println("\n==============================");
                System.out.println("     MENU DATABASE USERS");
                System.out.println("==============================");
                System.out.println("1. Tambah Data (Insert Row)");
                System.out.println("2. Tampilkan Semua Data (Show Rows)");
                System.out.println("3. Hapus Data (Delete Row)");
                System.out.println("4. Keluar");
                System.out.print("Pilih menu: ");
                pilihan = input.nextInt();
                input.nextLine(); // clear buffer

                switch (pilihan) {
                    case 1 -> {
                        System.out.print("Masukkan nama: ");
                        String nama = input.nextLine();
                        System.out.print("Masukkan email: ");
                        String email = input.nextLine();
                        tambahData(conn, nama, email);
                    }
                    case 2 -> tampilkanData(conn);
                    case 3 -> {
                        System.out.print("Masukkan ID yang ingin dihapus: ");
                        int id = input.nextInt();
                        hapusData(conn, id);
                    }
                    case 4 -> System.out.println(" Keluar dari program...");
                    default -> System.out.println(" Pilihan tidak valid!");
                }

            } while (pilihan != 4);

        } catch (SQLException e) {
            System.out.println(" Gagal koneksi ke database");
            e.printStackTrace();
        }
    }

    // Fungsi untuk menambah data
    public static void tambahData(Connection conn, String name, String email) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            int rows = stmt.executeUpdate();
            System.out.println(" " + rows + " data berhasil ditambahkan: " + name);
        } catch (SQLException e) {
            System.out.println(" Gagal menambah data");
            e.printStackTrace();
        }
    }

    // Fungsi untuk menampilkan semua data
    public static void tampilkanData(Connection conn) {
        String sql = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n Data Users:");
            System.out.println("+----+----------------------+----------------------------+");
            System.out.printf("| %-2s | %-20s | %-26s |\n", "ID", "Nama", "Email");
            System.out.println("+----+----------------------+----------------------------+");

            while (rs.next()) {
                System.out.printf("| %-2d | %-20s | %-26s |\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"));
            }

            System.out.println("+----+----------------------+----------------------------+");
        } catch (SQLException e) {
            System.out.println(" Gagal menampilkan data");
            e.printStackTrace();
        }
    }

    // Fungsi untuk menghapus data berdasarkan ID
    public static void hapusData(Connection conn, int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println(" Data dengan id " + id + " berhasil dihapus.");
            else
                System.out.println(" Tidak ada data dengan id " + id + ".");
        } catch (SQLException e) {
            System.out.println(" Gagal menghapus data");
            e.printStackTrace();
        }
    }
}
