public class Kendaraan {
    private String jenis;
    private String merek;
    private String warna;
    private String nomorPlat;

    public Kendaraan(String jenis, String merek, String warna, String nomorPlat) {
        this.jenis = jenis;
        this.merek = merek;
        this.warna = warna;
        this.nomorPlat = nomorPlat;
    }

    public String getJenis() {
        return jenis;
    }

    public String getMerek() {
        return merek;
    }

    public String getWarna() {
        return warna;
    }

    public String getNomorPlat() {
        return nomorPlat;
    }
}
