package gq.jared.pisaygwa.main

import android.app.Activity
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import gq.jared.pisaygwa.R
import gq.jared.pisaygwa.main.subjrv.SubjectAdapter
import gq.jared.pisaygwa.main.subjrv.SubjectLLMgr
import gq.jared.pisaygwa.main.subjrv.SubjectPresenter
import gq.jared.pisaygwa.main.subjrv.SubjectsPH

class MainPresenter(private val ctx: Activity): Contract.Presenter {

    private val sPresenter = SubjectPresenter()
    private val sAdapter = SubjectAdapter(sPresenter)

    fun onDestroy() {
        sPresenter.onDestroy()
    }

    override fun setupSubjectRecyclerView() {
        val lm = SubjectLLMgr(ctx, RecyclerView.VERTICAL, false)
        val sView: RecyclerView = ctx.findViewById(R.id.subj_list)
        sView.layoutManager = lm
        sView.itemAnimator = DefaultItemAnimator()
        sView.isNestedScrollingEnabled = false
        sView.adapter = sAdapter
        sAdapter.updateSubjects(SubjectsPH.Syp().subjects)
    }

    override fun setupSubjectSpinner() {
        val mNavView = ctx.findViewById<NavigationView>(R.id.sidebar)
        val mNavHead = mNavView.getHeaderView(0)
        val mSpinner: Spinner = mNavHead.findViewById(R.id.gradelevel_chooser)
        val mSpinnerAdapter = ArrayAdapter.createFromResource(ctx,
                R.array.levels, android.R.layout.simple_spinner_dropdown_item)
        mSpinner.adapter = mSpinnerAdapter
    }

}