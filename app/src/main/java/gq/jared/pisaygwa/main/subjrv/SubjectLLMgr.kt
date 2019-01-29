package gq.jared.pisaygwa.main.subjrv

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class SubjectLLMgr(ctx: Context, orientation: Int, reverseLayout: Boolean):
        LinearLayoutManager(ctx, orientation, reverseLayout) {

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

}