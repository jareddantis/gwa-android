package gq.jared.pisaygwa.subj

interface Contract {

    interface Model {
        // Subject
        fun incrementGrade()
        fun decrementGrade()
        fun saveGrade()
    }

    interface View {
        // Subject view row
        fun setName(name: String)
        fun setGrade(grade: String)
        fun setAverage(average: String)
        fun onInvalidGrade(grade: String)
    }

    interface Presenter {
        // RecyclerView presenter
        fun getRowCount(): Int
        fun onDestroy()
        fun onBindRowAtPos(pos: Int, holder: View)
        fun onPlusClickedAtPos(pos: Int, view: View)
        fun onMinusClickedAtPos(pos: Int, view: View)
        fun onGradeSpecified(pos: Int, view: View, grade: String)
        fun validateGrade(grade: Double): Boolean
    }

}