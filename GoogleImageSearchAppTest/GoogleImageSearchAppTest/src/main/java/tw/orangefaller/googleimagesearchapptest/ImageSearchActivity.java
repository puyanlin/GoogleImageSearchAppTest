package tw.orangefaller.googleimagesearchapptest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import tw.orangefaller.data.ThumbnailAdapter;
import tw.orangefaller.data.ThumbnailInfomation;

public class ImageSearchActivity extends Activity {
    private EditText etSearch;
    private GridView gridviewResult;
    private ArrayList<ThumbnailInfomation> thumbnailInfomationArrayList=new ArrayList<ThumbnailInfomation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_search);

        initGUI();
    }

    private void initGUI(){
        Button btnSearch=(Button) findViewById(R.id.btnSearch);
        etSearch=(EditText) findViewById(R.id.etSearch);
        gridviewResult=(GridView) findViewById(R.id.gridviewResult);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etSearch.getText().toString().equals("")){
                    Toast.makeText(ImageSearchActivity.this,"type text to search",Toast.LENGTH_SHORT).show();
                }else{
                    SearchImageTask task=new SearchImageTask();
                    task.execute(etSearch.getText().toString());
                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_search, menu);
        return true;
    }

    class SearchImageTask extends AsyncTask<String,Void,Void>{
        ProgressDialog searchProgressDialog;
        boolean failResult=false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchProgressDialog=new ProgressDialog(ImageSearchActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
            searchProgressDialog.setMessage("search...");
            searchProgressDialog.setCancelable(false);
            searchProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String target=strings[0].replaceAll(" ","%20");

                HttpClient hc = new DefaultHttpClient();
                String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+target;

                if (!url.matches("^((http[s]?):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$")) {
                    return null;
                }
                HttpGet httpget = new HttpGet(url);

                final HttpContext context = new BasicHttpContext();
                HttpResponse response;
                try {
                    response = hc.execute(httpget, context);

                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        BufferedReader br= new BufferedReader(new InputStreamReader(entity.getContent()));

                        JSONObject json = new JSONObject(br.readLine());
                        JSONObject result=json.getJSONObject("responseData");
                        JSONArray resultArray=result.getJSONArray("results");

                        if(resultArray.length()>0){
                            for(int i=0;i<resultArray.length();i++){
                                JSONObject js=(JSONObject)resultArray.get(i);


                                ThumbnailInfomation infomation=new ThumbnailInfomation(js.getInt("width"),js.getInt("height"),js.getString("tbUrl"));
                                if(infomation.isValid()){
                                    thumbnailInfomationArrayList.add(infomation);
                                }

                            }
                            int idx=0;
                            for(ThumbnailInfomation infomation : thumbnailInfomationArrayList){
                                Log.e("Joseph","res:"+infomation.getTbUrl());
                                loadPic(infomation.getTbUrl(),idx++);
                            }



                        }else{
                            failResult=true;
                        }

                    }else{
                        failResult=true;
                    }

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    failResult=true;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    failResult=true;
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    failResult=true;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    failResult=true;
                }


            }catch (Exception e){
                e.printStackTrace();
                failResult=true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(searchProgressDialog!=null&&searchProgressDialog.isShowing()){
                searchProgressDialog.dismiss();
                gridviewResult.setAdapter(new ThumbnailAdapter(ImageSearchActivity.this,thumbnailInfomationArrayList));
            }
            if(failResult){

            }else if(thumbnailInfomationArrayList.size()==0){

            }

        }

        private void loadPic(String mUrl,int idx){

            try {
                URL url = new URL (mUrl);
                InputStream input = url.openStream();
                //The sdcard directory e.g. '/sdcard' can be used directly, or
                //more safely abstracted with getExternalStorageDirectory()
                File storagePath = getExternalFilesDir("cache");
                OutputStream output = new FileOutputStream(new File(storagePath,idx+".bmp"));
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                        output.write(buffer, 0, bytesRead);
                    }
                } finally {
                    output.close();
                }
                input.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}
