package gq.jared.pisaygwa.subj

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gq.jared.pisaygwa.R

class SubjectAdapter(private val presenter: SubjectPresenter):
        RecyclerView.Adapter<SubjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder(presenter, LayoutInflater.from(parent.context)
                .inflate(R.layout.subject_row, parent, false))
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        presenter.onBindRowAtPos(position, holder)
    }

    override fun getItemCount(): Int {
        return presenter.getRowCount()
    }

    fun onDestroy() {
        presenter.onDestroy()
    }

    fun updateSubjects(newSubjects: ArrayList<SubjectImplPH>) {
        if (itemCount == 0) {
            presenter.subjects = newSubjects
            notifyDataSetChanged()
        } else {
            presenter.subjects.forEachIndexed { index, subject ->
                if (!newSubjects.contains(subject)) {
                    // Remove subject unique to current set
                    presenter.subjects.removeAt(index)
                    notifyItemRemoved(index)
                } else {
                    // Remove common subject
                    newSubjects.remove(subject)
                }
            }

            // Add subjects unique to new set
            val beforeCount = itemCount
            presenter.subjects.addAll(newSubjects)
            val afterCount = itemCount
            if (afterCount > beforeCount) {
                notifyItemRangeInserted(beforeCount, afterCount)
            }
        }
    }

}