package gq.jared.pisaygwa.subj

import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import gq.jared.pisaygwa.R
import gq.jared.pisaygwa.activity.Main

class SubjectViewHolder(presenter: SubjectPresenter, rowView: View):
        RecyclerView.ViewHolder(rowView), Contract.View {

    private val nameView: TextView = rowView.findViewById(R.id.subjectDesc)
    private val gradeView: TextView = rowView.findViewById(R.id.subjectGrade)
    private val plusButton: ImageView = rowView.findViewById(R.id.subjectGradePlus)
    private val minusButton: ImageView = rowView.findViewById(R.id.subjectGradeMinus)
    private lateinit var name: String
    private val ctx = rowView.context

    init {
        plusButton.setOnClickListener {
            presenter.onPlusClickedAtPos(adapterPosition, this)
        }
        minusButton.setOnClickListener {
            presenter.onMinusClickedAtPos(adapterPosition, this)
        }
        gradeView.setOnClickListener {
            // Create dialog
            val builder = AlertDialog.Builder(ctx)
            builder.setTitle(String.format(ctx.resources.getString(R.string.grade_prompt), name))

            // Set dialog view
            val activity = ctx as Main
            val dialogView = LayoutInflater.from(ctx)
                    .inflate(R.layout.grade_prompt, activity.findViewById(R.id.content), false)
            val inputView = dialogView.findViewById<EditText>(R.id.input)
            builder.setView(dialogView)

            // Setup buttons
            val viewHeld = this
            builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                presenter.onGradeSpecified(adapterPosition, viewHeld, inputView.text.toString())
            }
            builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }

            // Show dialog
            builder.show()
        }
    }

    override fun setName(name: String) {
        this.name = name
        nameView.text = name
    }

    override fun setGrade(grade: String) {
        gradeView.text = grade.toString()
    }

    override fun onInvalidGrade(grade: String) {
        val toast = Toast.makeText(ctx,
                String.format(ctx.resources.getString(R.string.grade_invalid), grade),
                Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun setAverage(average: String) {
        val activity = ctx as Main
        val avgView = activity.findViewById<TextView>(R.id.result)
        avgView.text = average
    }

}