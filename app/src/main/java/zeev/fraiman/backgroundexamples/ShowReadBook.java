package zeev.fraiman.backgroundexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class ShowReadBook extends AppCompatActivity {

    TextView tvBookName2, tvText2;
    String lang="", filename="",all="", forSpeack="";
    ArrayList<Integer> points;
    int position=0;
    TextToSpeech tts;
    int langspech, speech, count=0, posstart, posend;
    SeekBar sbTextSize2;
    LinearLayout LL2;
    ScrollView scrollView;
    private SpannableStringBuilder ssb;
    Thread t;
    Button bRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_read_book);

        bRead=findViewById(R.id.bRead);
        bRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak_all();
            }
        });

        scrollView=findViewById(R.id.sv);
        tvText2=findViewById(R.id.tvText2);
        tvBookName2=findViewById(R.id.tvText1);

        readStory();

        buildPoints('.');

        tts = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            langspech = tts.setLanguage(Locale.getDefault());
                        }
                    }
                });
        sbTextSize2=findViewById(R.id.sbTextSize2);
        sbTextSize2.setMax(10);
        sbTextSize2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvText2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15+i*3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        LL2=findViewById(R.id.LL2);
        LL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count%2==0)  {
                    LL2.setBackgroundColor(Color.BLACK);
                    tvText2.setBackgroundColor(Color.BLACK);
                    tvText2.setTextColor(Color.WHITE);
                }
                else  {
                    LL2.setBackgroundColor(Color.WHITE);
                    tvText2.setBackgroundColor(Color.WHITE);
                    tvText2.setTextColor(Color.BLACK);
                }
                count++;
            }
        });
    }

    private void speak_all() {
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                posstart=0;
                posend=4000;
                if (posend>all.length()) {
                    posend = all.length();
                    forSpeack=all.substring(posstart, posend);
                    tts.speak(forSpeack, TextToSpeech.QUEUE_FLUSH, null,"sonu");
                    return;
                }
                else
                    forSpeack=all.substring(posstart, posend);
                tts.speak(forSpeack, TextToSpeech.QUEUE_FLUSH, null,"sonu");
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {

                    }

                    @Override
                    public void onDone(String s) {
                        posstart+=4001;
                        posend+=4001;
                        if (posend>all.length()) {
                            posend = all.length();
                            forSpeack=all.substring(posstart, posend);
                            tts.speak(forSpeack, TextToSpeech.QUEUE_FLUSH, null,"sonu");
                            return;
                        }
                        else
                            forSpeack=all.substring(posstart, posend);
                        tts.speak(forSpeack, TextToSpeech.QUEUE_FLUSH, null,"sonu");
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
            }
        });
        t.start();
    }

    private void buildPoints(char s) {
        points=new ArrayList<>();
        points.add(0);
        for (int i = 0; i < all.length(); i++) {
            if (all.charAt(i)==s)
                points.add(i);
        }
    }

    private void readStory() {
        String st1="";
        try {
            InputStream is = getResources().openRawResource(R.raw.holmes);
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            all="";
            while ((st1=br.readLine())!=null) {
                all+=st1+"\n";
            }
            br.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error: can't show help.", Toast.LENGTH_SHORT).show();
        }
        tvText2.setText(all);
    }

    @Override
    protected void onStop() {
        super.onStop();
        tts.speak("", TextToSpeech.QUEUE_FLUSH, null,"sonu");
        tts.stop();
        finish();
    }
}