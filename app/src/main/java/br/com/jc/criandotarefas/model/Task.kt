package br.com.jc.criandotarefas.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String,
    val hour: String,
    val date: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
