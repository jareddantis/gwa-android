package gq.jared.pisaygwa.subj

class Subjects {

    open class Level {
        val subjects = ArrayList<Subject>()
    }

    class First: Level() {

        init {
            this.subjects.add(Subject("Integrated Science", 1.7))
            this.subjects.add(Subject("Mathematics", 1.7))
            this.subjects.add(Subject("Computer Science", 1.0))
            this.subjects.add(Subject("English", 1.3))
            this.subjects.add(Subject("Filipino", 1.0))
            this.subjects.add(Subject("Social Science", 1.0))
            this.subjects.add(Subject("PEHM", 1.0))
            this.subjects.add(Subject("Values Education", 0.7))
            this.subjects.add(Subject("AdTech", 1.0))
        }

    }

    class Second: Level() {

        init {
            this.subjects.add(Subject("Integrated Science", 2.0))
            this.subjects.add(Subject("Mathematics", 1.7))
            this.subjects.add(Subject("Computer Science", 1.0))
            this.subjects.add(Subject("English", 1.3))
            this.subjects.add(Subject("Filipino", 1.0))
            this.subjects.add(Subject("Social Science", 1.0))
            this.subjects.add(Subject("PEHM", 1.0))
            this.subjects.add(Subject("Values Education", 0.7))
            this.subjects.add(Subject("AdTech", 1.0))
            this.subjects.add(Subject("Earth Science", 0.7))
        }

    }

    class Third: Level() {

        init {
            this.subjects.add(Subject("Biology", 1.0))
            this.subjects.add(Subject("Chemistry", 1.0))
            this.subjects.add(Subject("Physics", 1.0))
            this.subjects.add(Subject("Mathematics", 1.0))
            this.subjects.add(Subject("English", 1.0))
            this.subjects.add(Subject("Filipino", 1.0))
            this.subjects.add(Subject("Social Science", 1.0))
            this.subjects.add(Subject("PEHM", 1.0))
            this.subjects.add(Subject("Statistics", 1.0))
            this.subjects.add(Subject("Computer Science", 1.0))
        }

    }

    class Fourth: Level() {

        init {
            this.subjects.add(Subject("Biology", 1.0))
            this.subjects.add(Subject("Chemistry", 1.0))
            this.subjects.add(Subject("Physics", 1.0))
            this.subjects.add(Subject("Mathematics", 1.3))
            this.subjects.add(Subject("English", 1.0))
            this.subjects.add(Subject("Filipino", 1.0))
            this.subjects.add(Subject("Social Science", 1.0))
            this.subjects.add(Subject("PEHM", 1.0))
            this.subjects.add(Subject("Research", 1.0))
            this.subjects.add(Subject("Computer Science", 1.0))
            this.subjects.add(Subject("Elective", 1.0))
        }

    }

    class Syp: Level() {

        init {
            this.subjects.add(Subject("Research", 2.0))
            this.subjects.add(Subject("Core", 1.7))
            this.subjects.add(Subject("Elective", 1.7))
            this.subjects.add(Subject("Mathematics", 1.0))
            this.subjects.add(Subject("English", 1.0))
            this.subjects.add(Subject("Filipino", 1.0))
            this.subjects.add(Subject("Social Science", 1.0))
        }

    }

}
