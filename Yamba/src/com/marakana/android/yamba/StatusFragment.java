package com.marakana.android.yamba;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.marakana.android.yamba.svc.YambaService;


public class StatusFragment extends Fragment {
    private static final String TAG = "STATUS";

    private int maxStatusLen;
    private int warnMax;
    private int errMax;
    private int okColor;
    private int warnColor;
    private int errColor;
    private EditText viewStatus;
    private TextView viewCount;
    private Button buttonSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       if (BuildConfig.DEBUG) { Log.d(TAG, "called onCreateView: " + this); }

        Resources rez = getResources();
        maxStatusLen = rez.getInteger(R.integer.status_max_len);
        warnMax = rez.getInteger(R.integer.warn_max);
        errMax = rez.getInteger(R.integer.err_max);

        okColor = rez.getColor(R.color.status_ok);
        warnColor = rez.getColor(R.color.status_warn);
        errColor = rez.getColor(R.color.status_err);

        View v = inflater.inflate(R.layout.fragment_status, container, false);

        viewCount = (TextView) v.findViewById(R.id.status_count);

        buttonSubmit = (Button) v.findViewById(R.id.status_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v1) { post(); }
        });

        viewStatus = (EditText) v.findViewById(R.id.status_status);
        viewStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { updateCount(); }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        } );

        return v;
    }

    void updateCount() {
        int n = viewStatus.getText().length();

        buttonSubmit.setEnabled(checkStatusLen(n));

        n = maxStatusLen - n;

        int c;
        if (n > warnMax) { c = okColor; }
        else if (n > errMax) { c = warnColor; }
        else { c = errColor; }

        viewCount.setText(String.valueOf(n));
        viewCount.setTextColor(c);
    }

    void post() {
//        if (null != poster) { return; }

        String status = viewStatus.getText().toString();
        if (BuildConfig.DEBUG) { Log.d(TAG, "Posting: " + status); }

        if (!checkStatusLen(status.length())) { return; }

        viewStatus.setText("");

        YambaService.post(getActivity(), status);

//        poster = new Poster(getApplicationContext());
//        poster.execute(status);
    }

    private boolean checkStatusLen(int n) {
        return (0 < n) && (maxStatusLen >= n);
    }

}
