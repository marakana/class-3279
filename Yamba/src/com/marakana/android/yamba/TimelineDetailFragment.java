/* $Id: $
   Copyright 2013, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.marakana.android.yamba;

import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TimelineDetailFragment extends Fragment {

    public static TimelineDetailFragment newInstance() {
        Bundle args = new Bundle();
        args.putString(YambaContract.Timeline.Columns.STATUS, "  ...");
        return newInstance(args);
    }

    public static TimelineDetailFragment newInstance(Bundle args) {
        TimelineDetailFragment frag = new TimelineDetailFragment();

        frag.setArguments(args);

        return frag;
    }

    private View details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle state) {
        details = inflater.inflate(R.layout.timeline_detail_fragment, root, false);

        setDetails(getArguments());
        return details;
    }

    public void setDetails(Bundle args) {
        if ((null == args) || (null == details)) { return; }

        ((TextView) details.findViewById(R.id.timeline_detail_timestamp))
            .setText(DateUtils.getRelativeTimeSpanString(
                    args.getLong(YambaContract.Timeline.Columns.CREATED_AT, 0L)));
        ((TextView) details.findViewById(R.id.timeline_detail_user)).setText(
                args.getString(YambaContract.Timeline.Columns.USER));
        ((TextView) details.findViewById(R.id.timeline_detail_status)).setText(
                args.getString(YambaContract.Timeline.Columns.STATUS));
    }
}
