package com.dms.beinone.application.qna;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.beinone.application.EditTextUtils;
import com.dms.beinone.application.R;
import com.dms.boxfox.networking.HttpBox;
import com.dms.boxfox.networking.datamodel.Commands;
import com.dms.boxfox.networking.datamodel.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by BeINone on 2017-02-13.
 */

public class QnAWriteActivity extends AppCompatActivity {

    private TextView mTitleTV;
    private EditText mTitleET;
    private TextView mContentTV;
    private EditText mContentET;
    private SwitchCompat mPrivacySwitch;
    private Button mSubmitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_write);

        // display back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs =
                getSharedPreferences(getString(R.string.PREFS_ACCOUNT), MODE_PRIVATE);
        final String writer = prefs.getString(getString(R.string.PREFS_ACCOUNT_ID), "");

        mTitleTV = (TextView) findViewById(R.id.tv_qna_write_title);
        mTitleET = (EditText) findViewById(R.id.et_qna_write_title);
        mTitleET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mTitleTV.setTextColor(
                            ContextCompat.getColor(QnAWriteActivity.this, R.color.colorPrimary));
                } else {
                    mTitleTV.setTextColor(
                            ContextCompat.getColor(QnAWriteActivity.this, android.R.color.primary_text_light));
                    // hide the soft keyboard when touch outside
                    EditTextUtils.hideKeyboard(QnAWriteActivity.this, (EditText) v);
                }
            }
        });

        mContentTV = (TextView) findViewById(R.id.tv_qna_write_content);
        mContentET = (EditText) findViewById(R.id.et_qna_write_content);
        mContentET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mContentTV.setTextColor(
                            ContextCompat.getColor(QnAWriteActivity.this, R.color.colorPrimary));
                } else {
                    mContentTV.setTextColor(
                            ContextCompat.getColor(QnAWriteActivity.this, android.R.color.primary_text_light));
                    // hide the soft keyboard when touch outside
                    EditTextUtils.hideKeyboard(QnAWriteActivity.this, (EditText) v);
                }
            }
        });

        mPrivacySwitch = (SwitchCompat) findViewById(R.id.switch_qna_write_privacy);

        Button submitBtn = (Button) findViewById(R.id.btn_qna_write_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleET.getText().toString().trim();
                String content = mContentET.getText().toString().trim();
                boolean privacy = mPrivacySwitch.isChecked();

                new UploadQnATask().execute(new QnA(title, content, writer, privacy));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return false;
    }

    private class UploadQnATask extends AsyncTask<QnA, Void, Integer> {

        @Override
        protected Integer doInBackground(QnA... params) {
            int code = -1;

            try {
                code = uploadQnA(params[0]);
            } catch (IOException e) {
                return -1;
            } catch (JSONException e) {
                return -1;
            }

            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);

            if (code == 200) {
                // success
                Toast.makeText(QnAWriteActivity.this, R.string.qna_write_success, Toast.LENGTH_SHORT)
                        .show();
                finish();
            } else if (code == 500) {
                // failure
                Toast.makeText(QnAWriteActivity.this, R.string.qna_write_failure, Toast.LENGTH_SHORT)
                        .show();
            } else {
                // error
                Toast.makeText(QnAWriteActivity.this, R.string.qna_write_error, Toast.LENGTH_SHORT)
                        .show();
            }
        }

        private int uploadQnA(QnA qna) throws IOException, JSONException {
            JSONObject requestJSONObject = new JSONObject();
            requestJSONObject.put("title", qna.getTitle());
            requestJSONObject.put("content", qna.getQuestionContent());
            requestJSONObject.put("writer", qna.getWriter());
            requestJSONObject.put("privacy", qna.isPrivacy());

            Response response = HttpBox.post()
                    .setCommand(Commands.UPLOAD_REPORT_FACILITY)
                    .putBodyData(requestJSONObject)
                    .push();

            return response.getCode();
        }
    }

}
