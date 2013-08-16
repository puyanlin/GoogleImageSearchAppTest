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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ImageSearchActivity extends Activity {
    private EditText etSearch;
    private GridView gridviewResult;

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
                        //Log.e("Joseph","Result1:" + result.toString());
                        //Log.e("Joseph","Result2:"+result.getJSONArray("results").get(0).toString());
                        for(int i=0;i<resultArray.length();i++){
                            JSONObject js=(JSONObject)resultArray.get(i);
                            Log.e("Joseph","Result2:"+js.toString());
                        }

                    }

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
