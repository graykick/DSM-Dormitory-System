package com.dms.beinone.application.faq;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dms.beinone.application.EmptySupportedRecyclerView;
import com.dms.beinone.application.JSONParser;
import com.dms.beinone.application.R;
import com.dms.beinone.application.RecyclerViewUtils;
import com.dms.boxfox.networking.HttpBox;
import com.dms.boxfox.networking.datamodel.Commands;
import com.dms.boxfox.networking.datamodel.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BeINone on 2017-01-19.
 */

public class FAQFragment extends Fragment {

    private EmptySupportedRecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        init(view);

        return view;
    }

    /**
     * 초기화, RecyclerView 세팅
     * @param rootView 필요한 뷰를 찾을 최상위 뷰
     */
    private void init(View rootView) {
        getActivity().setTitle(R.string.nav_faq);

        mRecyclerView = (EmptySupportedRecyclerView) rootView.findViewById(R.id.rv_faq);

        View emptyView = rootView.findViewById(R.id.view_faq_empty);
        RecyclerViewUtils.setupRecyclerView(mRecyclerView, getContext(), emptyView);

        new LoadFAQListTask().execute();
    }

    private class LoadFAQListTask extends AsyncTask<Void, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Void... params) {
            Object[] results = null;

            try {
                results = loadFAQList();
            } catch (IOException e) {
                return null;
            } catch (JSONException e) {
                return null;
            }

            return results;
        }

        @Override
        protected void onPostExecute(Object[] results) {
            super.onPostExecute(results);

            if (results != null) {
                int code = (int) results[0];
                List<FAQ> faqList = (ArrayList<FAQ>) results[1];

                if (code == 200) {
                    // success
                    mRecyclerView.setAdapter(new FAQAdapter(getContext(), faqList));
                } else if (code == 204) {
                    // no data
                    Toast.makeText(getContext(), R.string.faq_nodata, Toast.LENGTH_SHORT).show();
                } else {
                    // error
                    Toast.makeText(getContext(), R.string.faq_error, Toast.LENGTH_SHORT).show();
                }
            } else {
                // error
                Toast.makeText(getContext(), R.string.faq_error, Toast.LENGTH_SHORT).show();
            }
        }

        private Object[] loadFAQList() throws IOException, JSONException {
            Response response = HttpBox.post().setCommand(Commands.LOAD_FAQ).putBodyData().push();
            JSONObject responseJSONObject = response.getJsonObject();

            int code = response.getCode();

            List<FAQ> faqList = null;
            if (code == 200) {
                faqList = JSONParser.parseFAQJSON(responseJSONObject);
            }

            return new Object[] { code, faqList };
        }
    }

}
