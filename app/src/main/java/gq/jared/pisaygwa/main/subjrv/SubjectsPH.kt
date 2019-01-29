package gq.jared.pisaygwa.subj

class SubjectsPH {

    open class Level {
        val subjects = ArrayList<SubjectImplPH>()
    }

    class First: Level() {

        init {
            this.subjects.add(SubjectImplPH("Integrated Science", 1.7))
            this.subjects.add(SubjectImplPH("Mathematics", 1.7))
            this.subjects.add(SubjectImplPH("Computer Science", 1.0))
            this.subjects.add(SubjectImplPH("English", 1.3))
            this.subjects.add(SubjectImplPH("Filipino", 1.0))
            this.subjects.add(SubjectImplPH("Social Science", 1.0))
            this.subjects.add(SubjectImplPH("PEHM", 1.0))
            this.subjects.add(SubjectImplPH("Values Education", 0.7))
            this.subjects.add(SubjectImplPH("AdTech", 1.0))
        }

    }

    class Second: Level() {

        init {
            this.subjects.add(SubjectImplPH("Integrated Science", 2.0))
            this.subjects.add(SubjectImplPH("Mathematics", 1.7))
            this.subjects.add(SubjectImplPH("Computer Science", 1.0))
            this.subjects.add(SubjectImplPH("English", 1.3))
            this.subjects.add(SubjectImplPH("Filipino", 1.0))
            this.subjects.add(SubjectImplPH("Social Science", 1.0))
            this.subjects.add(SubjectImplPH("PEHM", 1.0))
            this.subjects.add(SubjectImplPH("Values Education", 0.7))
            this.subjects.add(SubjectImplPH("AdTech", 1.0))
            this.subjects.add(SubjectImplPH("Earth Science", 0.7))
        }

    }

    class Third: Level() {

        init {
            this.subjects.add(SubjectImplPH("Biology", 1.0))
            this.subjects.add(SubjectImplPH("Chemistry", 1.0))
            this.subjects.add(SubjectImplPH("Physics", 1.0))
            this.subjects.add(SubjectImplPH("Mathematics", 1.0))
            this.subjects.add(SubjectImplPH("English", 1.0))
            this.subjects.add(SubjectImplPH("Filipino", 1.0))
            this.subjects.add(SubjectImplPH("Social Science", 1.0))
            this.subjects.add(SubjectImplPH("PEHM", 1.0))
            this.subjects.add(SubjectImplPH("Statistics", 1.0))
            this.subjects.add(SubjectImplPH("Computer Science", 1.0))
        }

    }

    class Fourth: Level() {

        init {
            this.subjects.add(SubjectImplPH("Biology", 1.0))
            this.subjects.add(SubjectImplPH("Chemistry", 1.0))
            this.subjects.add(SubjectImplPH("Physics", 1.0))
            this.subjects.add(SubjectImplPH("Mathematics", 1.3))
            this.subjects.add(SubjectImplPH("English", 1.0))
            this.subjects.add(SubjectImplPH("Filipino", 1.0))
            this.subjects.add(SubjectImplPH("Social Science", 1.0))
            this.subjects.add(SubjectImplPH("PEHM", 1.0))
            this.subjects.add(SubjectImplPH("Research", 1.0))
            this.subjects.add(SubjectImplPH("Computer Science", 1.0))
            this.subjects.add(SubjectImplPH("Elective", 1.0))
        }

    }

    class Syp: Level() {

        init {
            this.subjects.add(SubjectImplPH("Research", 2.0))
            this.subjects.add(SubjectImplPH("Core", 1.7))
            this.subjects.add(SubjectImplPH("Elective", 1.7))
            this.subjects.add(SubjectImplPH("Mathematics", 1.0))
            this.subjects.add(SubjectImplPH("English", 1.0))
            this.subjects.add(SubjectImplPH("Filipino", 1.0))
            this.subjects.add(SubjectImplPH("Social Science", 1.0))
        }

    }

}
