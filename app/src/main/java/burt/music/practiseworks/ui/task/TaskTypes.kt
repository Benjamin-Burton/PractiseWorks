package burt.music.practiseworks.ui.task


/**
 * Enum class for the different types of tasks that can exist in the app.
 */
enum class TaskTypes(val string: String) {
    WARMUP("warmup"),
    EXERCISE("exercise"),
    PIECE("piece"),
    OTHER("other")
}