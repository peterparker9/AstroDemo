package ashu.astrodemo.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by apple on 29/04/18.
 */

public class GetNumOfColumns {

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
