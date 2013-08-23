package tw.orangefaller.googleimagesearchapptest;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.TextView;

public class OriginalViewerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_originalviewer);
        String url=getIntent().getStringExtra("url");
        WebView wbvImage=(WebView) findViewById(R.id.wbv_image);
        wbvImage.loadUrl(url);

        TextView tvUrl=(TextView) findViewById(R.id.tv_url);
        tvUrl.setText(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.original_viewer, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
