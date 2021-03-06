package com.dms.beinone.application.stayapply;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ListView;
import android.widget.Toast;

import com.dms.beinone.application.R;
import com.dms.boxfox.networking.HttpBox;
import com.dms.boxfox.networking.datamodel.Commands;
import com.dms.boxfox.networking.datamodel.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by BeINone on 2017-01-31.
 */

public class ChangeDefaultStatusDialogFragment extends DialogFragment {

    private Context mContext;
    private ChangeDefaultStatusListener mListener;

    private SharedPreferences mAccountPrefs;

    public static ChangeDefaultStatusDialogFragment newInstance(
            Context context, int defaultStatus, ChangeDefaultStatusListener listener) {

        Bundle args = new Bundle();
        args.putInt(context.getString(R.string.ARGS_DEFAULTSTATUS), defaultStatus);

        ChangeDefaultStatusDialogFragment fragment = new ChangeDefaultStatusDialogFragment();
        fragment.setArguments(args);
        fragment.mContext = context;
        fragment.mListener = listener;

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mAccountPrefs = mContext.getSharedPreferences(
                getString(R.string.PREFS_ACCOUNT), Context.MODE_PRIVATE);

        final String id = mAccountPrefs.getString(getString(R.string.PREFS_ACCOUNT_ID), "");
        final int defStatus = getArguments().getInt(getString(R.string.ARGS_DEFAULTSTATUS), -1);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.stayapply_dialog_title)
                .setSingleChoiceItems(R.array.change_default_status, defStatus - 1, null)
                .setNegativeButton(R.string.stayapply_dialog_negative,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .setPositiveButton(R.string.stayapply_dialog_positive,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ListView listView = ((AlertDialog) dialog).getListView();
                                int value = listView.getCheckedItemPosition() + 1;

                                new ChangeDefaultStatusTask().execute(id, value);
                            }
                        })
                .create();
    }

    private class ChangeDefaultStatusTask extends AsyncTask<Object, Void, int[]> {

        @Override
        protected int[] doInBackground(Object... params) {
            int[] ints = null;

            try {
                String id = params[0].toString();
                int value = (int) params[1];
                ints = changeDefaultStatus(id, value);
            } catch (IOException e) {
                return null;
            } catch (JSONException e) {
                return null;
            }

            return ints;
        }

        @Override
        protected void onPostExecute(int[] ints) {
            super.onPostExecute(ints);

            int code = ints[0];
            int value = ints[1];

            if (code == 200) {
                /* success */
                Toast.makeText(mContext, R.string.stayapply_dialog_success, Toast.LENGTH_SHORT)
                        .show();

                // notify the StayApplyFragment of changed default status
                mListener.onChangeDefaultStatus(value);
            } else if (code == 500) {
                /* failure */
                Toast.makeText(mContext, R.string.stayapply_dialog_failure, Toast.LENGTH_SHORT)
                        .show();
            } else {
                /* error */
                Toast.makeText(mContext, R.string.stayapply_dialog_error, Toast.LENGTH_SHORT)
                        .show();
            }

            dismiss();
        }

        private int[] changeDefaultStatus(String id, int value) throws IOException, JSONException {
            JSONObject requestJSONObject = new JSONObject();
            requestJSONObject.put("id", id);
            requestJSONObject.put("value", value);
            Response response = HttpBox.post()
                    .setCommand(Commands.MODIFY_STAY_DEFAULT)
                    .putBodyData(requestJSONObject)
                    .push();

            return new int[] { response.getCode(), value };
        }
    }

    public interface ChangeDefaultStatusListener {

        void onChangeDefaultStatus(int defaultStatus);
    }

}
