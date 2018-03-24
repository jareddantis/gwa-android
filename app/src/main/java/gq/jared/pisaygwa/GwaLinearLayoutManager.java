package gq.jared.pisaygwa;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class GwaLinearLayoutManager extends LinearLayoutManager {

    public GwaLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }
}
