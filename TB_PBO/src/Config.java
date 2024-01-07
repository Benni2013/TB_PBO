import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Config {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/tb_pbo";
    static final String USER = "root";
    static final String PASS = "";

    static Integer a = 1, b = 1; // Variabel untuk nomor Parkir

    // Method untuk membuat koneksi
    public static Connection buatKoneksi() {
        Connection conn = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Koneksi Berhasil");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi Gagal");
            e.printStackTrace();
        }

        return conn;
    }

    // Method untuk membuat Tabel Parkiran jika belum ada
    public static void buatTabel(Statement stmt) throws SQLException {
    
        try {
            // Query untuk menciptakan tabel
            String query = "CREATE TABLE IF NOT EXISTS tabel_parkiran (" +
                                "no_parkir VARCHAR(5) PRIMARY KEY, " +
                                "tanggal VARCHAR(255), " +
                                "jam_masuk VARCHAR(255), " +
                                "tukang_parkir VARCHAR(255), " +
                                "nama_pemilik VARCHAR(255), " + 
                                "no_hp VARCHAR(15)," + 
                                "jenis_kendaraan VARCHAR(5)," + 
                                "merek VARCHAR(255)," +
                                "warna VARCHAR(255), " +
                                "no_plat VARCHAR(255), " + 
                                "harga_parkir DOUBLE(5,1) " + 
                            ");";

            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Error saat menciptakan tabel: " + e.getMessage());
        }
    }

    // Method untuk menampilkan tabel
    public static void showTable(Statement stmt) throws SQLException {
        // Execute select query
        String sql = "SELECT * FROM tabel_parkiran";
        ResultSet rs = stmt.executeQuery(sql);

        // Display results
        System.out.println("\nData Tabel Parkir:");
        while (rs.next()) {
            String no_parkir = rs.getString("no_parkir");
            String tanggal = rs.getString("tanggal");
            String jam_masuk = rs.getString("jam_masuk");
            String nama_pemilik = rs.getString("nama_pemilik");
            String no_hp = rs.getString("no_hp");
            String jenis_kendaraan = rs.getString("jenis_kendaraan");
            String merek = rs.getString("merek");
            String warna = rs.getString("warna");
            String no_plat = rs.getString("no_plat");
            Double harga_parkir = rs.getDouble("harga_parkir");
            String tukang_parkir = rs.getString("tukang_parkir");

            System.out.println("No. Parkir: " + no_parkir + ", Tanggal : " + tanggal + ", Jam Masuk: " + 
                                jam_masuk + ", Tukang Parkir: " + tukang_parkir +  ", Nama Pemilik: " + nama_pemilik + 
                                ", No. HP: " + no_hp + ", Jenis Kendaraan: " + jenis_kendaraan + ", Merek Kendaraan: " + 
                                merek + ", Warna Kendaraan: " + warna + ", No. Plat: " + no_plat + ", Harga Parkir: " + 
                                harga_parkir + "\n");
        }
        System.out.println("\n");
    }

    // Method untuk membuat data
    public static void createData(Statement stmt, Scanner scanStr, String tukangParkir) throws SQLException {
        
        System.out.println("--------------------------");
        System.out.println("INPUT DATA PELANGGAN:");

        // Input customer information
        System.out.print("Nama pemilik kendaraan: ");
        String namaPemilik = scanStr.nextLine();
        System.out.print("Nomor HP pemilik: ");
        String noHpPemilik = scanStr.nextLine();
        System.out.print("Jenis kendaraan (mobil/motor): ");
        String jenisKendaraan = scanStr.nextLine();
        System.out.print("Merek kendaraan: ");
        String merekKendaraan = scanStr.nextLine();
        System.out.print("Warna kendaraan: ");
        String warnaKendaraan = scanStr.nextLine();
        System.out.print("Nomor plat kendaraan: ");
        String nomorPlat = scanStr.nextLine();

        String noParkir;
        if (jenisKendaraan.equalsIgnoreCase("mobil")) {
            noParkir = "A" + String.format("%03d", a);
            a++;
        } else {
            noParkir = "B" + String.format("%03d", b);
            b++;
        }
        // Buat objek parkiran
        Parkiran parkiran = new Parkiran(noParkir, jenisKendaraan, merekKendaraan, warnaKendaraan, nomorPlat, tukangParkir, namaPemilik, noHpPemilik);

        String tanggal = parkiran.getTanggal();
        String jam = parkiran.getJam();
        Double harga = parkiran.getHargaParkir();

        // Execute insert query
        String insertSql = "INSERT INTO tabel_parkiran VALUES ('" + noParkir + "', '" + tanggal + "', '" + jam + "', '"  + tukangParkir + 
                            "', '"  + namaPemilik + "', '" + noHpPemilik + "', '" + jenisKendaraan + "', '" + merekKendaraan.toUpperCase() + 
                            "', '" + warnaKendaraan + "', '" + nomorPlat.toUpperCase() + "', " + harga + ");";
        
        stmt.executeUpdate(insertSql);

        System.out.println("\nData berhasil ditambahkan!\n\nCetak Kartu Parkir:");

        // Cetak kartu parkir
        parkiran.cetakKartuParkir();

    }

    // Method untuk mengedit data
    public static void editData(Statement stmt, Scanner scanStr, String tukangParkir) throws SQLException {
        System.out.print("Masukkan No. Parkir yang akan diedit: ");
        String idToEdit = scanStr.nextLine();

        // Check if the data with the given ID exists
        ResultSet rs = stmt.executeQuery("SELECT * FROM tabel_parkiran WHERE no_parkir = '" + idToEdit.toUpperCase() + "'");
        if (rs.next()) {
            System.out.println("Masukkan data yang baru: ");

            // Input customer information
            System.out.print("Nama pemilik kendaraan: ");
            String newNamaPemilik = scanStr.nextLine();
            System.out.print("Nomor HP pemilik: ");
            String newNoHpPemilik = scanStr.nextLine();
            System.out.print("Merek kendaraan: ");
            String newMerekKendaraan = scanStr.nextLine();
            System.out.print("Warna kendaraan: ");
            String newWarnaKendaraan = scanStr.nextLine();
            System.out.print("Nomor plat kendaraan: ");
            String newNomorPlat = scanStr.nextLine();

            // Execute insert query
            String updateSql =  "UPDATE tabel_parkiran SET " +
                                      "nama_pemilik = '" + newNamaPemilik + "', " +
                                      "no_hp = '" + newNoHpPemilik + "', " +
                                      "merek = '" + newMerekKendaraan.toUpperCase() + "', " +
                                      "warna = '" + newWarnaKendaraan + "', " +
                                      "no_plat = '" + newNomorPlat.toUpperCase() + "' " +
                                "WHERE no_parkir = '"+ idToEdit.toUpperCase() + "';";

            stmt.executeUpdate(updateSql);

            System.out.println("\nData berhasil diperbarui.\n");
        } else {
            System.out.println("Data dengan No. Parkir " + idToEdit + " tidak ditemukan.\n");
        }
    }

    // Method untuk menghapus data
    public static void deleteData(Statement stmt, Scanner scanStr) throws SQLException {
        System.out.print("Masukkan No. Parkir yang akan dihapus: ");
        String idToDelete = scanStr.nextLine();

        // Check if the data with the given ID exists
        ResultSet rs = stmt.executeQuery("SELECT * FROM tabel_parkiran WHERE no_parkir = '" + idToDelete + "'");
        if (rs.next()) {
            // Execute delete query
            String deleteSql = "DELETE FROM tabel_parkiran WHERE no_parkir = '" + idToDelete + "'";
            stmt.executeUpdate(deleteSql);

            System.out.println("Data berhasil dihapus.\n");
        } else {
            System.out.println("Data dengan No. Parkir " + idToDelete + " tidak ditemukan.\n");
        }
    }
}
