package zeev.fraiman.backgroundexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final int JOB_ID = 1;
    Button b1, b2, b3, b4;
    Context context;
    TextView viewmenu;
    PopupMenu popupMenu;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=MainActivity.this;

        b1=findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go=new Intent(context, ReadPicByAsyncTask.class);
                startActivity(go);
            }
        });

        b2=findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go=new Intent(context, ReadPicByThread.class);
                startActivity(go);
            }
        });

        b3=findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go=new Intent(context, ShowReadBook.class);
                startActivity(go);
            }
        });

        b4=findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                ComponentName componentName=new ComponentName(context, MyJobService.class);
                JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                        .setPeriodic(15*60*1000)
                        .setPersisted(true)
                        .build();
                jobScheduler.schedule(jobInfo);
            }
        });

        viewmenu=findViewById(R.id.viewmenu);
        viewmenu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                popupMenu = new PopupMenu(context, viewmenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_total_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId()==R.id.exit)
                            finishAffinity();
                        if (item.getItemId()==R.id.back)
                            finish();
                        if (item.getItemId()==R.id.readbook)  {
                            Intent go=new Intent(context, ShowReadBook.class);
                            startActivity(go);
                        }
                        if (item.getItemId()==R.id.readpicAT)  {
                            Intent go=new Intent(context, ReadPicByAsyncTask.class);
                            startActivity(go);
                        }
                        if (item.getItemId()==R.id.readpicT)  {
                            Intent go=new Intent(context, ReadPicByThread.class);
                            startActivity(go);
                        }
                        if (item.getItemId()==R.id.bottomsheet) {
                            MyBottomSheetDialogFragment bottomSheetDialogFragment = new MyBottomSheetDialogFragment();
                            bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                        }

                        return false;
                    }
                });
                return false;
            }
        });

        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "FAB",
                        Toast.LENGTH_SHORT).show();
                Snackbar snackbar=Snackbar.make(v,
                        "Press for MaterialDesign3 site",
                        Snackbar.LENGTH_LONG);
                snackbar.setAction("View Material Components",
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://m3.material.io/components"));
                        startActivity(intent);
                    }
                });
                snackbar.show();
            }
        });
    }
}