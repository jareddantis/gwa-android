package gq.jared.pisaygwa.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import gq.jared.pisaygwa.R
import gq.jared.pisaygwa.subj.Subjects
import gq.jared.pisaygwa.subj.SubjectAdapter
import gq.jared.pisaygwa.subj.SubjectPresenter

class Main: AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mSpinner: AppCompatSpinner
    private var subjectAdapter = SubjectAdapter(SubjectPresenter())

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MainTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        // Action bar
        mDrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.menu)
        }

        // Spinner
        val mNavView = findViewById<NavigationView>(R.id.sidebar)
        val mNavHead = mNavView.getHeaderView(0)
        mSpinner = mNavHead.findViewById(R.id.gradelevel_chooser)
        val mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.levels, android.R.layout.simple_spinner_dropdown_item)
        mSpinner.adapter = mSpinnerAdapter

        // Setup view
        val lm = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        val sView = findViewById<RecyclerView>(R.id.subj_list)
        sView.layoutManager = lm
        sView.itemAnimator = DefaultItemAnimator()
        sView.isNestedScrollingEnabled = false
        sView.adapter = subjectAdapter
        subjectAdapter.updateSubjects(Subjects.Syp().subjects)
    }

    override fun onDestroy() {
        super.onDestroy()
        subjectAdapter.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}