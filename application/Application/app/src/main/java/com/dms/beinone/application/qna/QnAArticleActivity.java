package com.dms.beinone.application.qna;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.beinone.application.JSONParser;
import com.dms.beinone.application.R;
import com.dms.beinone.application.comment.CommentActivity;
import com.dms.beinone.application.dmsview.DMSButton;
import com.dms.boxfox.networking.HttpBox;
import com.dms.boxfox.networking.datamodel.Commands;
import com.dms.boxfox.networking.datamodel.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by BeINone on 2017-01-23.
 */

public class QnAArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_article);

        // display back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final int no = getIntent().getIntExtra(getString(R.string.EXTRA_NO), 0);

        DMSButton commentBtn = (DMSButton) findViewById(R.id.btn_qna_article_comment);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewComment(no);
            }
        });

        new LoadQnATask().execute(no);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    /**
     * sets text of article
     * @param qna QnA object that contains information of article
     */
    private void bind(QnA qna) {
        TextView titleTV = (TextView) findViewById(R.id.tv_qna_article_title);
        TextView writerTV = (TextView) findViewById(R.id.tv_qna_article_writer);
        TextView questionDateTV = (TextView) findViewById(R.id.tv_qna_article_questiondate);
        TextView questionContentTV = (TextView) findViewById(R.id.tv_qna_article_questioncontent);

        titleTV.setText(qna.getTitle());
        writerTV.setText(qna.getWriter());
        questionDateTV.setText(qna.getQuestionDate());
        questionContentTV.setText(qna.getQuestionContent());
    }

    /**
     * start a new activity to display comments
     * @param no index of the article
     */
    private void viewComment(int no) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(getString(R.string.EXTRA_NO), no);
        startActivity(intent);
    }

    private class LoadQnATask extends AsyncTask<Integer, Void, QnA> {

        @Override
        protected QnA doInBackground(Integer... params) {
            QnA qna = null;

            try {
                qna = loadQnA(params[0]);
            } catch (IOException ie) {
                return null;
            } catch (JSONException je) {
                return null;
            }

            return qna;
        }

        @Override
        protected void onPostExecute(QnA qna) {
            super.onPostExecute(qna);

            if (qna == null) {
                Toast.makeText(
                        QnAArticleActivity.this, R.string.qna_article_error, Toast.LENGTH_SHORT)
                        .show();
            } else {
                bind(qna);
            }
        }

        private QnA loadQnA(int no) throws IOException, JSONException {
            JSONObject requestJSONObject = new JSONObject();
            requestJSONObject.put("no", no);
            Response response =
                    HttpBox.post().setCommand(Commands.LOAD_QNA).putBodyData(requestJSONObject).push();

            JSONObject responseJSONObject = response.getJsonObject();

            return JSONParser.parseQnAJSON(responseJSONObject, no);
        }
    }

}
