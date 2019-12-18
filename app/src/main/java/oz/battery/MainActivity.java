package oz.battery;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
{
    private Context context;

    private SeekBar fullBar;
    private TextView fullBarText;
    private int fullProgressValue = 0;
    private Switch fullSwitch;

    private SeekBar lowBar;
    private TextView lowBarText;
    private int lowProgressValue = 0;
    private Switch lowSwitch;

    private Switch voiceSwitch;
    private Switch vibrateSwitch;

    //private TextView alarmText;
    private TextView soundText;
    private TextView vibrateText;

    private int lowCounter = 1;
    private int highCounter = 1;
    private int check = 0;
    private int switchCheker = 0;

    private Ringtone ringtone;
    private Vibrator vibrator;

    //private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        fullBar = findViewById(R.id.full_Bar);
        fullBarText = findViewById(R.id.full_Bar_Text);
        fullSwitch = findViewById(R.id.switch_Full);

        lowBar = findViewById(R.id.low_Bar);
        lowBarText = findViewById(R.id.low_Bar_Text);
        lowSwitch = findViewById(R.id.switch_Low);

        voiceSwitch = findViewById(R.id.alert_Voice);
        vibrateSwitch = findViewById(R.id.alert_Vibrate);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //alarmText = (TextView) findViewById(R.id.alert_Text);
        soundText = findViewById(R.id.soundText);
        vibrateText = findViewById(R.id.vibrateText);

        fullBar.setEnabled(false);
        lowBar.setEnabled(false);
        fullSwitch.setOnCheckedChangeListener(this);
        lowSwitch.setOnCheckedChangeListener(this);
        voiceSwitch.setOnCheckedChangeListener(this);
        vibrateSwitch.setOnCheckedChangeListener(this);

        voiceSwitch.setChecked(true);

        fullBarText.setText(this.getResources().getString(R.string.biggerr));
        lowBarText.setText(this.getResources().getString(R.string.smallerr));
        soundText.setText(this.getResources().getString(R.string.voicee));
        vibrateText.setText(this.getResources().getString(R.string.vibratee));

        fullBarText.setEnabled(false);
        lowBarText.setEnabled(false);
        soundText.setEnabled(true);
        vibrateText.setEnabled(false);

        //mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // Initialize a new IntentFilter instance
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // Get the application context
        context = getApplicationContext();
        // Register the broadcast receiver
        context.registerReceiver(mBroadcastReceiver,iFilter);
    }

    public void fullBarM()
    {
        fullBar.setProgress(50);
        fullBarText.setText((int)(((float)fullBar.getProgress()/(float)fullBar.getMax())*100) + "%");
        fullBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                   @Override
                   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                   {
                       fullProgressValue = progress;
                       fullBarText.setText((int)(((float)progress/(float)fullBar.getMax())*100) + "%");
                   }
                   @Override
                   public void onStartTrackingTouch(SeekBar seekBar) {}
                   @Override
                   public void onStopTrackingTouch(SeekBar seekBar) {highCounter = 0;}
        }
        );
    }

    public void lowBarM()
    {
        lowBar.setProgress(50);
        lowBarText.setText((int)(((float)lowBar.getProgress()/(float)lowBar.getMax())*100) + "%");
        lowBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                    {
                        lowProgressValue = progress;
                        lowBarText.setText((int)(((float)progress/(float)lowBar.getMax())*100) + "%");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {lowCounter = 0;}
                }
        );
    }

    // Alarm baslar...
    private void mediaPlayerStart()
    {
        try
        {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if(alert == null)
            {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                if(alert == null)
                {
                    alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                }
            }
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), alert);
            ringtone.setStreamType(AudioManager.STREAM_ALARM);
            ringtone.play();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        //mMediaPlayer = new MediaPlayer();
        //mMediaPlayer = MediaPlayer.create(this, R.raw.my);
        //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mMediaPlayer.setLooping(true);
        //mMediaPlayer.start();
    }

    private void mediaPlayerStop()
    {
        ringtone.stop();
        //mMediaPlayer.stop();
    }

    private void vibrateStart()
    {
        long[] pattern = {0, 100, 1000};
        //vibrator.vibrate(1000);
        vibrator.vibrate(pattern, 0);
    }

    private void vibrateStop()
    {
        vibrator.cancel();
    }

    // alerdialog gösterir.
    private void alertDialogM(String title, String message)
    {
        AlertDialog.Builder builder;
        check = 1;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setNeutralButton(this.getResources().getString(R.string.closee), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                if(voiceSwitch.isChecked())
                {
                    mediaPlayerStop();
                }
                if(vibrateSwitch.isChecked())
                {
                    vibrateStop();
                }
                //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak
                check = 0;
                dialog.dismiss();
            }
        });
        builder.show();

        if(check == 1)
        {
            new Handler().postDelayed(new Runnable() { @Override public void run() { new BackgroundTask().execute(); } }, 10000);
        }
    }

    public class BackgroundTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if(voiceSwitch.isChecked())
            {
                mediaPlayerStop();
            }
            if(vibrateSwitch.isChecked())
            {
                vibrateStop();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        float percentage;
        int scale;
        int level;

        @Override
        public void onReceive(Context context, Intent intent)
        {
            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            percentage = level/ (float) scale;

            //Log.i("XXX" ,voiceSwitch.isChecked() + " " + vibrateSwitch.isChecked());
            if (fullProgressValue <= percentage * 100 && highCounter == 0 && fullSwitch.isChecked() && check == 0)
            {
                if(voiceSwitch.isChecked() || vibrateSwitch.isChecked())
                {
                    //alarmText.setText("Alarm");
                    alertDialogM(context.getResources().getString(R.string.bateryFull), context.getResources().getString(R.string.bateryFullMessage));
                    if(voiceSwitch.isChecked())
                    {
                        mediaPlayerStart();
                    }
                    if(vibrateSwitch.isChecked())
                    {
                        vibrateStart();
                    }
                    highCounter = 1;
                }
            }

            if(lowProgressValue >= percentage*100 && lowCounter == 0 && lowSwitch.isChecked() && check == 0)
            {
                if(voiceSwitch.isChecked() || vibrateSwitch.isChecked())
                {
                    //alarmText.setText("Alarm");
                    alertDialogM(context.getResources().getString(R.string.bateryLow), context.getResources().getString(R.string.bateryLowMessage));
                    if(voiceSwitch.isChecked())
                    {
                        mediaPlayerStart();
                    }
                    if(vibrateSwitch.isChecked())
                    {
                        vibrateStart();
                    }
                    lowCounter = 1;
                }
            }
        }
    };

    //Switch'lere göre bar'ları aktif eder.
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (switchCheker == 0)
        {
            lowBarM();
            fullBarM();
            switchCheker = 1;
        }

        if (fullSwitch.isChecked())
        {
            fullBar.setEnabled(true);
            fullBarText.setEnabled(true);
        }
        else
        {
            fullBar.setEnabled(false);
            fullBarText.setEnabled(false);
        }

        if (lowSwitch.isChecked())
        {
            lowBar.setEnabled(true);
            lowBarText.setEnabled(true);
        }
        else
        {
            lowBar.setEnabled(false);
            lowBarText.setEnabled(false);
        }

        if (voiceSwitch.isChecked())
        {
            soundText.setEnabled(true);
        }
        else
        {
            soundText.setEnabled(false);
        }

        if (vibrateSwitch.isChecked())
        {
            vibrateText.setEnabled(true);
        }
        else
        {
            vibrateText.setEnabled(false);
        }

        if (!voiceSwitch.isChecked() && !vibrateSwitch.isChecked())
        {
            Toast.makeText(this, context.getResources().getString(R.string.alarm), Toast.LENGTH_LONG).show();
        }
    }
}