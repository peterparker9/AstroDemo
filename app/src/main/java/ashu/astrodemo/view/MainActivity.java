package ashu.astrodemo.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import ashu.astrodemo.Adapter;
import ashu.astrodemo.R;
import ashu.astrodemo.model.ItemDTO;
import ashu.astrodemo.presenter.MainPresenter;
import ashu.astrodemo.utils.GetNumOfColumns;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView{

    @BindView(R.id.recycleImages)
    RecyclerView recyclerViewImages;

    @BindView(R.id.progressBottom)
    ProgressBar progressBar;

    MainPresenter presenter;

    private int visibleItem = 0;
    private int currentItem = 0;
    boolean flag = false;


    Adapter adapter;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        presenter = new MainPresenter(MainActivity.this, this);

        presenter.fetchImages();

        recyclerViewImages.setLayoutManager(new GridLayoutManager(MainActivity.this,
                GetNumOfColumns.calculateNoOfColumns(MainActivity.this)));



    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void populateImages(final Adapter adapter) {
        recyclerViewImages.setAdapter(adapter);


        recyclerViewImages.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                GridLayoutManager gm = (GridLayoutManager) recyclerViewImages.getLayoutManager();

                visibleItem = gm.findLastVisibleItemPosition() + 1;

                if(flag == true)
                    progressBar.setVisibility(View.GONE);
                if(visibleItem == adapter.getItemCount()){
                    flag = true;
                }
                else if(currentItem != 0 && currentItem == visibleItem &&
                        visibleItem < adapter.getItemCount() && !flag){
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                }
                currentItem = visibleItem;

            }

        });
    }
}
