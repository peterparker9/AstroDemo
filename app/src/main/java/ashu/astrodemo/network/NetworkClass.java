package ashu.astrodemo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;

import ashu.astrodemo.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by apple on 26/04/18.
 */

public class NetworkClass {

    public Retrofit start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;

    }
}
