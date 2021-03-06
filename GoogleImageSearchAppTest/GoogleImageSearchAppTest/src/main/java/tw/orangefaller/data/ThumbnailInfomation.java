package tw.orangefaller.data;

/**
 * Created by puyan on 13/8/17.
 */
public class ThumbnailInfomation {
    private int mWidth=0,mHeight=0;
    private String mTbUrl="";
    private String mOringinalUrl;
    public ThumbnailInfomation(int width,int height,String tbUrl,String url){
        mWidth=width;
        mHeight=height;
        mTbUrl=tbUrl;
        mOringinalUrl=url;
    }
    public String getResolution(){
        return mWidth+" x "+mHeight;
    }
    public int getWidth(){
        return mWidth;
    }
    public int getHeight(){
        return mHeight;
    }
    public String getTbUrl(){
        return mTbUrl;
    }
    public String getUrl(){
        return mOringinalUrl;
    }
    public boolean isValid(){
        return mWidth!=0&&mHeight!=0&&!mTbUrl.equalsIgnoreCase("");
    }
}
