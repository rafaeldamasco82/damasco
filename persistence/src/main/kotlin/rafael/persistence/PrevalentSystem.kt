package rafael.persistence

data class SystemState(val state: List<String> = ArrayList())

data class SystemChanged<DOMAIN>(val value: DOMAIN, val systemState: SystemState)

interface Command<DOMAIN> {
    fun execute(system: SystemState): SystemChanged<DOMAIN>
}

interface Query<RETURN> {
    fun execute(systemState: SystemState): RETURN?
}

interface PrevalentSystem {
    fun <DOMAIN> execute(command: Command<DOMAIN>): SystemChanged<DOMAIN>
    fun <RETURN> execute(query: Query<RETURN>): RETURN?
}


data class AddNewString(val value: String) : Command<String> {
    override fun execute(systemState: SystemState): SystemChanged<String> {
        val newSystemState = systemState.copy(state = systemState.state.plus(value))
        return SystemChanged(value, newSystemState)
    }
}

data class RemoveNewString(val value: String) : Command<String> {
    override fun execute(systemState: SystemState): SystemChanged<String> {
        val newSystemState = systemState.copy(state = systemState.state.minus(value))
        return SystemChanged(value, newSystemState)
    }
}

class StringNamesSystem: PrevalentSystem {
    var systemState = SystemState()

    override fun <DOMAIN> execute(command: Command<DOMAIN>): SystemChanged<DOMAIN> {
        val systemChanged = command.execute(systemState)
        systemState = systemChanged.systemState
        return systemChanged
    }

    override fun <RETURN> execute(query: Query<RETURN>): RETURN? {
        return null
    }
}

fun main() {
    val prevalentSystem = StringNamesSystem()

    val changed01 = prevalentSystem.execute(AddNewString("Agnaldo"))
    val changed02 = prevalentSystem.execute(AddNewString("Daniel"))
    val changed03 = prevalentSystem.execute(AddNewString("Rafael"))
    val changed04 = prevalentSystem.execute(AddNewString("Solange"))

    val changed05 = prevalentSystem.execute(RemoveNewString("Agnaldo"))

    println(prevalentSystem.systemState)
}