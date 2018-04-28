package ashu.astrodemo.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;

import ashu.astrodemo.Adapter;
import ashu.astrodemo.R;
import ashu.astrodemo.model.ItemDTO;
import ashu.astrodemo.model.ResponseDTO;
import ashu.astrodemo.network.NetworkClass;
import ashu.astrodemo.network.NetworkService;
import ashu.astrodemo.view.MainView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by apple on 28/04/18.
 */

public class MainPresenter{

    Context context;
    MainView mainView;

    ProgressDialog pd;

    Adapter adapter;

    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    public MainPresenter(Context context, MainView mainView){
        this.context = context;
        this.mainView = mainView;
    }

    public void onDestroy(){
        this.mainView = null;
    }

    public void fetchImages(){
        NetworkClass networkClass = new NetworkClass();
        Retrofit retrofit = networkClass.start();

        pd = new ProgressDialog(context);

        pd.setCancelable(false);
        pd.setMessage("Fetching Images ...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setProgress(0);
        pd.setMax(100);
        pd.show();
        NetworkService networkInterface = retrofit.create(NetworkService.class);
        Call<ResponseDTO> resultDTOCall = networkInterface.getListOfImages();
        new NetworkBackgroundCall().execute(resultDTOCall);
    }


    class NetworkBackgroundCall extends AsyncTask<Call, Void, ResponseDTO> implements Adapter.ItemClickListener{
        Response<ResponseDTO> responseDTO;

        @Override
        protected ResponseDTO doInBackground(Call... params) {

            try {
                Call<ResponseDTO> call = params[0];
                responseDTO = call.execute();
                return responseDTO.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResponseDTO responseDTO) {
            if(mainView != null) {
                pd.dismiss();
                adapter = new Adapter(context,responseDTO.getItems(), this);
                mainView.populateImages(adapter);
            }
        }

        @Override
        public void onItemClick(View view, int pos) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();

            int DeviceTotalWidth = metrics.widthPixels;
            int DeviceTotalHeight = metrics.heightPixels;

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.layout_dialog);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.imgLoad);
            TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
            TextView txtAuthor = (TextView) dialog.findViewById(R.id.txtAuthor);
            Button btnClose = (Button) dialog.findViewById(R.id.button_close);

            ItemDTO itemDTO = responseDTO.body().getItems().get(pos);

            Glide.with(context)
                    .load(itemDTO.getMedia().getM())
                    .centerCrop()
                    .override(getWidth(),getHeight() /2)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);

            PhotoViewAttacher pAttacher;
            pAttacher = new PhotoViewAttacher(imageView);
            pAttacher.update();
            txtTitle.setText(itemDTO.getTitle());
            txtAuthor.setText(itemDTO.getAuthor());
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setLayout(DeviceTotalWidth ,DeviceTotalHeight);

            if(!dialog.isShowing())
                dialog.show();

        }

        private int getWidth(){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            return width;
        }

        private int getHeight(){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;

            return height;
        }


    }
}
