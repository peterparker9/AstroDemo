package ashu.astrodemo.network;

import ashu.astrodemo.model.ResponseDTO;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NetworkService {

    @GET("services/feeds/photos_public.gne?tags=cat&format=json&nojsoncallback=1")
    Call<ResponseDTO> getListOfImages();
}
