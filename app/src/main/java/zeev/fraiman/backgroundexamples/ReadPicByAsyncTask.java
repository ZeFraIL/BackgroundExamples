package zeev.fraiman.backgroundexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadPicByAsyncTask extends AppCompatActivity {


    Button btn;
    TextView tv;
    ImageView iv;
    ProgressBar pb;
    URL[] picsurl;
    int j=0;
    RVWpicFromNet goat;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pic_by_async_task);

        context=ReadPicByAsyncTask.this;
        tv=  findViewById(R.id.tv);
        iv=  findViewById(R.id.iv);
        pb=  findViewById(R.id.pb);
        btn=  findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (j==picsurl.length)  {
                    btn.setText("No more pics");
                    return;
                }
                Toast.makeText(context, ""+picsurl[j], Toast.LENGTH_LONG).show();
                goat=new RVWpicFromNet();
                goat.execute(picsurl[j]);
                j++;
            }
        });

        buildArr();
    }

    private void buildArr() {
        InputStream is=this.getResources().openRawResource(R.raw.picsfromnet);
        InputStreamReader isr=new InputStreamReader(is);
        BufferedReader br=new BufferedReader(isr);
        String t="";
        int count=0;
        try {
            while ((t=br.readLine())!=null) {
                count++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        picsurl=new URL[count];

        is=this.getResources().openRawResource(R.raw.picsfromnet);
        isr=new InputStreamReader(is);
        br=new BufferedReader(isr);
        t="";
        int i=0;
        try {
            while ((t=br.readLine())!=null) {
                picsurl[i]=new URL(t);
                i++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class RVWpicFromNet extends AsyncTask<URL, Void, Bitmap> {

        private static final String DIR = "PicsFromNet";
        Bitmap bm=null;
        String sturl;
        File sdPath;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btn.setText("Downloaded pic");
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(URL... urls) {
            HttpURLConnection conn= null;
            try {
                conn = (HttpURLConnection) urls[0].openConnection();
                conn.connect();
                InputStream is=conn.getInputStream();
                BufferedInputStream bis=new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
                sturl=urls[0].toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iv.setImageBitmap(bitmap);
            //super.onPostExecute(bitmap);
            pb.setVisibility(View.INVISIBLE);
            btn.setText("Click for new pic");
            tv.setText(sturl);
        }
    }
}
