import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        
        Connection conn = null;
        Statement stmt = null;

        try {
            // Cek koneksi ke database
            System.out.print("\nStatus Koneksi: ");
            conn = Config.buatKoneksi();
            stmt = conn.createStatement(); // Membuat Statement
            Config.buatTabel(stmt); // Membuat Tabel

            // Membuat scanner
            Scanner scanStr = new Scanner(System.in);
            Scanner scanInt = new Scanner(System.in);

            System.out.println("Selamat Datang di Parkiran Benni");
            System.out.println("================================");
            System.out.println("Tanggal: " + new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date()));

            // Input nama tukang parkir
            System.out.print("\nInput Nama tukang parkir: ");
            String tukangParkir = scanStr.nextLine();
            System.out.println("-------------------");
            
            //variabel pilihan
            Integer choice;

            do {
                System.out.println("Menu:");
                System.out.println("1. Memarkir kendaraan");
                System.out.println("2. Menampilkan data dari tabel parkiran");
                System.out.println("3. Mengedit data");
                System.out.println("4. Menghapus data");
                System.out.println("0. Keluar dari Program");
    
                System.out.print("Pilih menu (0-4): ");
                choice = scanInt.nextInt();
    
                switch (choice) {
                    case 1:
                        Config.createData(stmt, scanStr, tukangParkir);
                        break;
                    case 2:
                        Config.showTable(stmt);
                        break;
                    case 3:
                        Config.editData(stmt, scanStr, tukangParkir);
                        break;
                    case 4:
                        Config.deleteData(stmt, scanStr);
                        break;
                    case 0:
                        System.out.println("Berikut rekap data kendaraan yang parkir");
                        rekapDataParkiran(stmt, tukangParkir);
                        System.out.println("Keluar dari Program. Terima kasih!");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
            } while (choice != 0);

            scanStr.close();
            scanInt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void rekapDataParkiran(Statement stmt, String tukangParkir) throws SQLException {
        // Execute select query
        String sql = "SELECT * FROM tabel_parkiran";
        ResultSet rs = stmt.executeQuery(sql);

        // Map untuk menyimpan data kendaraan berdasarkan jenis
        Map<String, Map<String, String>> dataMobil = new LinkedHashMap<>();
        Map<String, Map<String, String>> dataMotor = new LinkedHashMap<>();

        // Display results
        while (rs.next()) {
            String no_parkir = rs.getString("no_parkir");
            String jenis_kendaraan = rs.getString("jenis_kendaraan");
            String merek = rs.getString("merek");
            String warna = rs.getString("warna");
            String no_plat = rs.getString("no_plat");
            String nama_pemilik = rs.getString("nama_pemilik");
            String no_hp = rs.getString("no_hp");
            Double harga = rs.getDouble("harga_parkir");

            // Membuat map untuk kendaraan
            Map<String, String> kendaraan = new LinkedHashMap<>();
            kendaraan.put("No. Parkir", no_parkir);
            kendaraan.put("Merek", merek);
            kendaraan.put("Warna", warna);
            kendaraan.put("No. Plat", no_plat);
            kendaraan.put("Nama Pemilik", nama_pemilik);
            kendaraan.put("No. HP", no_hp);
            kendaraan.put("Harga Parkir", Double.toString(harga));

            // Menyimpan data kendaraan dalam map berdasarkan jenis
            if ("mobil".equalsIgnoreCase(jenis_kendaraan)) {
                dataMobil.put(no_parkir, kendaraan);
            } else if ("motor".equalsIgnoreCase(jenis_kendaraan)) {
                dataMotor.put(no_parkir, kendaraan);
            }
        }

        // Menampilkan hasil rekap
        System.out.println("\nRekap Data Parkiran Benni");
        System.out.println("=========================");
        System.out.println("Hari, Tanggal: " + new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date()));
        System.out.println("Tukang Parkir: " + tukangParkir);
        System.out.println("--------------------------------");

        // Menampilkan data mobil
        System.out.println("Data mobil yang diparkir:");
        tampilkanDataKendaraan(dataMobil);

        // Menampilkan data motor
        System.out.println("Data motor yang diparkir:");
        tampilkanDataKendaraan(dataMotor);

        // Menampilkan total pemasukan
        int totalPemasukan = hitungTotalPemasukan(dataMobil) + hitungTotalPemasukan(dataMotor);
        System.out.println("Total Pemasukan : Rp. " + totalPemasukan + ",-");
        System.out.println();
    }

    private static void tampilkanDataKendaraan(Map<String, Map<String, String>> dataKendaraan) {
        for (Map.Entry<String, Map<String, String>> entry : dataKendaraan.entrySet()) {
            String noParkir = entry.getKey();
            Map<String, String> kendaraan = entry.getValue();

            System.out.println("No. Parkir: " + noParkir + " | Merek: " + kendaraan.get("Merek") +
                    " | Warna: " + kendaraan.get("Warna") + " | No. Plat: " + kendaraan.get("No. Plat") +
                    " | Nama Pemilik: " + kendaraan.get("Nama Pemilik") + " | No. HP: " + kendaraan.get("No. HP"));
        }

        // Menampilkan jumlah kendaraan dan total pemasukan
        int jumlahKendaraan = dataKendaraan.size();
        int totalPemasukan = hitungTotalPemasukan(dataKendaraan);

        System.out.println("\nJumlah kendaraan yang parkir: " + jumlahKendaraan +
                ", Pemasukan : Rp. " + totalPemasukan + ",-");
        System.out.println("------------------------------------------------------------------------");
    }

    private static int hitungTotalPemasukan(Map<String, Map<String, String>> dataKendaraan) {
        int totalPemasukan = 0;
        for (Map<String, String> kendaraan : dataKendaraan.values()) {
            double hargaParkir = Double.parseDouble(kendaraan.get("Harga Parkir"));
            totalPemasukan += hargaParkir;
        }
        return totalPemasukan;
    }
}
