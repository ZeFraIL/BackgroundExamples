package zeev.fraiman.backgroundexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadPicByThread extends AppCompatActivity {

    Button btnT;
    TextView tvT;
    ImageView ivT;
    ProgressBar pbT;
    String[] picsurl;
    int j=0;
    //ReadPicByAsyncTask.RVWpicFromNet goat;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pic_by_thread);

        context=ReadPicByThread.this;

        buildArr();

        tvT=  findViewById(R.id.tvT);
        ivT=  findViewById(R.id.ivT);
        pbT=  findViewById(R.id.pbT);
        btnT=  findViewById(R.id.btnT);
        btnT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (j==picsurl.length)  {
                    btnT.setText("No more pics");
                    return;
                }
                Toast.makeText(context, ""+picsurl[j], Toast.LENGTH_LONG).show();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = getBitmapFromUrl(picsurl[j]);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivT.setImageBitmap(bitmap);
                                j++;
                            }
                        });
                    }
                });
                thread.start();
            }
        });
    }

    private Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

        picsurl=new String[count];

        is=this.getResources().openRawResource(R.raw.picsfromnet);
        isr=new InputStreamReader(is);
        br=new BufferedReader(isr);
        t="";
        int i=0;
        try {
            while ((t=br.readLine())!=null) {
                picsurl[i]=t;
                i++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}