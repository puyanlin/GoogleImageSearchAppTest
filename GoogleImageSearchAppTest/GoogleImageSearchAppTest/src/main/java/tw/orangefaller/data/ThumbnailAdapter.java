package tw.orangefaller.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import tw.orangefaller.googleimagesearchapptest.R;

/**
 * Created by puyan on 13/8/17.
 */
public class ThumbnailAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ThumbnailInfomation> mDataSource;

    public ThumbnailAdapter(Context c,ArrayList<ThumbnailInfomation> dataSource) {
        mContext = c;
        mDataSource=dataSource;
    }

    public int getCount() {
        return mDataSource.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout view=null;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            view = (RelativeLayout)inflater.inflate(R.layout.layout_thumbnailinfo,null);
            TextView tvRes=(TextView) view.findViewById(R.id.tv_resolution);
            tvRes.setText(mDataSource.get(position).getResolution());
            ImageView thumb=(ImageView) view.findViewById(R.id.imgvThumb);

            File storagePath = mContext.getExternalFilesDir("cache");
            File bmpFile=new File(storagePath,position+".bmp");
            if(bmpFile.exists()){
                Bitmap bm = BitmapFactory.decodeFile(bmpFile.getAbsolutePath());
                Drawable drawable = new BitmapDrawable(mContext.getResources(),bm);
                thumb.setImageDrawable(drawable);
            }

        } else {
            view = (RelativeLayout)convertView;
        }

        return view;
    }


}
