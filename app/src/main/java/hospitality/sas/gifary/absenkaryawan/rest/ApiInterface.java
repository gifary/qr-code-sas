package hospitality.sas.gifary.absenkaryawan.rest;

import hospitality.sas.gifary.absenkaryawan.Constants;
import hospitality.sas.gifary.absenkaryawan.model.KaryawanReponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * Created by gifary on 10/10/17.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("user/login")
    @Headers({ "Accept: application/json","Authorization: "+ Constants.KEY })
    Call<KaryawanReponse> login(@Field("email") String email, @Field("password") String password);

}
