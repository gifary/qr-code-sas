package hospitality.sas.gifary.absenkaryawan.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gifary on 10/10/17.
 */

public class Karyawan {

    @SerializedName("p_karyawan_id")
    private int pKaryawanId;
    @SerializedName("p_recruitment_id")
    private String pRecruitmetnId;
    @SerializedName("nik")
    private String nik;
    @SerializedName("nama")
    private String nama;
    @SerializedName("no_hp2")
    private String noHp;
    @SerializedName("email_perusahaan")
    private String emailPerusahaan;
    @SerializedName("user_id")
    private String userId;

    public Karyawan(int pKaryawanId, String pRecruitmetnId, String nik, String nama, String noHp, String emailPerusahaan, String userId) {
        this.pKaryawanId = pKaryawanId;
        this.pRecruitmetnId = pRecruitmetnId;
        this.nik = nik;
        this.nama = nama;
        this.noHp = noHp;
        this.emailPerusahaan = emailPerusahaan;
        this.userId = userId;
    }

    public int getpKaryawanId() {
        return pKaryawanId;
    }

    public void setpKaryawanId(int pKaryawanId) {
        this.pKaryawanId = pKaryawanId;
    }

    public String getpRecruitmetnId() {
        return pRecruitmetnId;
    }

    public void setpRecruitmetnId(String pRecruitmetnId) {
        this.pRecruitmetnId = pRecruitmetnId;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getEmailPerusahaan() {
        return emailPerusahaan;
    }

    public void setEmailPerusahaan(String emailPerusahaan) {
        this.emailPerusahaan = emailPerusahaan;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
