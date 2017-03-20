package kim.harry.com.text2voice;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TextEditActivity extends AppCompatActivity {


    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textedit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = ((EditText) findViewById(R.id.edit_query));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editText.getText().toString();
                Snackbar.make(view, "Request message", Snackbar.LENGTH_SHORT)
                        .setAction("Remove", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText.getEditableText().clear();
                            }
                        }).show();
                if (!TextUtils.isEmpty(message)) {
                    requestNaverApi(message);
                }
            }
        });
    }

    /**
     * > POST /v1/labs/translate.json HTTP/1.1
     * > Host: openapi.naver.com
     * > User-Agent: curl/7.43.0
     * > Accept: *
     * > Content-Type: application/json
     * > X-Naver-Client-Id: YOUR_CLIENT_ID
     * > X-Naver-Client-Secret: YOUR_CLIENT_SECRET"
     *
     * @param message
     */
    private void requestNaverApi(String message) {

        final File mpFile = new File(getExternalFilesDir(null), "demo_" + message.length() + ".mp3");
        Ion.with(this)
                .load("https://openapi.naver.com/v1/voice/tts.bin")
                .setHeader("X-Naver-Client-Id", "5uw46EbLkRN_sqJpaksZ")
                .setHeader("X-Naver-Client-Secret", "I1EkW9k2Hu")
                .setBodyParameter("speaker", "matt")
                .setBodyParameter("speed", "0")
                .setBodyParameter("text", message)
                .write(mpFile)
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, final File file) {
                        if (e != null) {
                            Log.e("Eerrrr", "ee", e);
                            return;
                        }
                        if (file != null) {
                            Snackbar.make(TextEditActivity.this.getCurrentFocus(), "Compelte", Snackbar.LENGTH_LONG)
                                    .setAction("play", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            MediaPlayer mp = new MediaPlayer();
                                            try {
                                                mp.setDataSource(file.getAbsolutePath());
                                                mp.prepare();
                                                mp.start();
                                            } catch (IOException e1) {
                                                Log.e("ERROR", "ee", e1);
                                            }
                                        }
                                    }).show();
                        }
                    }
                });

    }

}