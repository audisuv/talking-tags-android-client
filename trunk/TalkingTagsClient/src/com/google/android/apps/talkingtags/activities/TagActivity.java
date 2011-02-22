package com.google.android.apps.talkingtags.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.TextView;

import com.google.android.apps.talkingtags.Config;
import com.google.android.apps.talkingtags.Controller;
import com.google.android.apps.talkingtags.R;
import com.google.android.apps.talkingtags.TalkingTagsApplication;
import com.google.android.apps.talkingtags.model.Tag;

public class TagActivity extends Activity implements OnInitListener {

    public static final String EXTRA_TITLE = "com.google.apps.android.talkingtags.TITLE";
    public static final String EXTRA_BODY = "com.google.apps.android.talkingtags.BODY";
    public static final String EXTRA_ID = "com.google.apps.android.talkingtags.ID";

    private static final int CHECK_CODE = 0;

    private TextToSpeech tts;

    private TextView titleText;
    private TextView bodyText;

    private String title;
    private String body;
    private String id;
    private Controller ctrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaytag);

        titleText = (TextView) findViewById(R.id.title);
        bodyText = (TextView) findViewById(R.id.body);

        ctrl = ((TalkingTagsApplication) getApplication()).getController();

        // Check text-to-speech.
        Config.log("onCreate, configure tts...");
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, CHECK_CODE);
    }

    @Override
    protected void onResume() {
      super.onStart();

      Intent i = getIntent();
      title = i.getStringExtra(EXTRA_TITLE);
      body = i.getStringExtra(EXTRA_BODY);
      id = i.getStringExtra(EXTRA_ID);

      if (id != null) {
        Tag tag = ctrl.getTagStore().read(id);
        if (tag != null) {
          title = tag.title;
          body = tag.body;
        } else {
          ctrl.invalidTag();
          finish();
          return;
        }
      }

      Config.log("Got id: " + id + " and found tag: " + title);

      // Fill in some test information.
      // TODO(adamconnors): Kick off a server request to fetch this tag.
      if (title == null) {
        title = getString(R.string.no_content);
      }

      titleText.setText(title);

      if (body != null) {
        bodyText.setText(body.trim());
      }

      Config.log("onResume, starting utterances: " + tts);
      if (tts != null) {
        startUtterances();
      }
    }

    @Override
    protected void onPause() {
      super.onPause();
      stopUtterances();
    }

    @Override
    protected void onDestroy() {
      super.onDestroy();
      if (tts != null) {
        tts.shutdown();
        tts = null;
      }
    }

    /**
     * Speak the currently displayed
     */
    private void startUtterances() {
      tts.speak(title, TextToSpeech.QUEUE_FLUSH, null);

      if (body != null) {
        tts.speak(body, TextToSpeech.QUEUE_ADD, null);
      }
    }

    /**
     * Halt the current speech.
     */
    private void stopUtterances() {
      if (tts != null) {
        tts.stop();
      }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == CHECK_CODE) {
        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
            // success, create the TTS instance
            Config.log("onActivityResult, tts configured, instantiating...");
            tts = new TextToSpeech(this, this);
        } else {
            // missing data, install it
            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installIntent);
        }
      }
    }

    /**
     * Init listener for T-T-S.
     */
    @Override
    public void onInit(int status) {
      Config.log("onInit: " + status);
      startUtterances();
    }
}