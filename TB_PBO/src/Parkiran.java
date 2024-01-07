import java.text.SimpleDateFormat;
import java.util.Date;

public class Parkiran extends Kendaraan implements BisaDiParkir {
    private String nama;
    private String noHp;
    private String noParkir;
    private Integer a = 1, b = 1;
    private String tukangParkir;
    Date date = new Date();

    public Parkiran(String noParkir, String jenis, String merek, String warna, String noPlat, String tukangParkir, String nama, String noHp) {
        super(jenis, merek, warna, noPlat);
        this.noParkir = noParkir;
        this.tukangParkir = tukangParkir;
        this.nama = nama;
        this.noHp = noHp;
    }

    public Double getHargaParkir(){
        Double hargaParkir;
        String jenis = getJenis();
        
        if (jenis.equalsIgnoreCase("mobil")) {
            hargaParkir = 5000.0;
        } else {
            hargaParkir = 2000.0;
        }

        return hargaParkir;
    }

    public String getNoParkir() {
        String jenis = getJenis();
    
        if (jenis.equalsIgnoreCase("mobil")) {
            noParkir = "A" + String.format("%03d", a);
            a++;
        } else {
            noParkir = "B" + String.format("%03d", b);
            b++;
        }
    
        return noParkir;
    }
    
    //method untuk mendapatkan data tanggal
    public String getTanggal() {
        SimpleDateFormat tgl = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String tanggal = tgl.format(this.date);
        return tanggal;
    }

    //method untuk mendapatkan data jam
    public String getJam() {
        SimpleDateFormat jam = new SimpleDateFormat("HH:mm:ss zzz");
        String waktu = jam.format(this.date);
        return waktu;
    }

    public void cetakKartuParkir(){
        System.out.println("\nKartu Parkir Parkiran Benni");
        System.out.println("===========================");
        System.out.println("Nomor Parkir\t: " + this.noParkir);
        System.out.println("Hari, Tanggal\t: " + getTanggal());
        System.out.println("Jam Masuk\t: " + getJam());
        System.out.println("Tukang Parkir\t: " + tukangParkir);
        System.out.println("-----------------------");
        System.out.println("Data Pemilik Kendaraan:");
        System.out.println(" - Nama\t\t: " + nama);
        System.out.println(" - No. HP\t: " + noHp);
        System.out.println("Data Kendaraan:");
        System.out.println(" - Jenis\t: " + getJenis());
        System.out.println(" - Merek\t: " + getMerek().toUpperCase());
        System.out.println(" - Warna\t: " + getWarna());
        System.out.println(" - Nomor Plat\t: " + getNomorPlat().toUpperCase());
        System.out.println("Harga Parkir\t: " + getHargaParkir());
        System.out.println("===========================");
        System.out.println("Terima kasih telah menggunakan jasa kami\n");
    }

}
