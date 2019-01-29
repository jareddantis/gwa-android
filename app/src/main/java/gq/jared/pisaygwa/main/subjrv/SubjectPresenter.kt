package gq.jared.pisaygwa.main.subjrv

import java.math.RoundingMode
import java.text.DecimalFormat

class SubjectPresenter: Contract.Presenter {

    var subjects = ArrayList<SubjectImplPH>()

    override fun onDestroy() {
        subjects.clear()
    }

    override fun onBindRowAtPos(pos: Int, holder: Contract.View) {
        val subject = subjects[pos]
        holder.setGrade(toTwoDecimals(subject.grade))
        holder.setName(subject.description)
    }

    override fun getRowCount(): Int {
        return subjects.size
    }

    override fun onPlusClickedAtPos(pos: Int, view: Contract.View) {
        subjects[pos].incrementGrade()
        updateGrade(pos, view)
    }

    override fun onMinusClickedAtPos(pos: Int, view: Contract.View) {
        subjects[pos].decrementGrade()
        updateGrade(pos, view)
    }

    override fun onGradeSpecified(pos: Int, view: Contract.View, grade: String) {
        val subject = subjects[pos]
        if (validateGrade(grade.toDouble())) {
            subject.grade = grade.toDouble()
            updateGrade(pos, view)
        } else {
            view.onInvalidGrade(grade)
        }
    }

    override fun validateGrade(grade: Double): Boolean {
        return !((grade < 1 || grade > 5)
                || grade < 3 && (grade % 0.25 != 0.0)
                || (grade > 3 && grade < 4)
                || (grade > 4 && grade < 5))
    }

    private fun toTwoDecimals(grade: Double): String {
        val dfGrade = DecimalFormat("#.00")
        return dfGrade.format(grade)
    }

    private fun updateGrade(pos: Int, view: Contract.View) {
        // Update visible grade
        view.setGrade(toTwoDecimals(subjects[pos].grade))

        // Compute totals
        var total = 0.0
        var units = 0.0
        for (subject in subjects) {
            total += subject.grade * subject.units
            units += subject.units
        }

        // Compute & display average
        val dfAvg = DecimalFormat("#.000")
        dfAvg.roundingMode = RoundingMode.DOWN
        view.setAverage(dfAvg.format(total / units))
    }

}