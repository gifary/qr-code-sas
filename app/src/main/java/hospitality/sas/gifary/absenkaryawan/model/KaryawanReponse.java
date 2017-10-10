package hospitality.sas.gifary.absenkaryawan.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gifary on 10/10/17.
 */

public class KaryawanReponse {
    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private Karyawan data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Karyawan getData() {
        return data;
    }
}
