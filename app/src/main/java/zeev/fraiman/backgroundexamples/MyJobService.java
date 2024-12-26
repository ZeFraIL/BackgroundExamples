package zeev.fraiman.backgroundexamples;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.MediaPlayer;
import android.widget.Toast;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        MediaPlayer mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.gong);
        mediaPlayer.start();
        Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
