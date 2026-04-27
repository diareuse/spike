import kotlin.IntArray
import spike.factory.InstructionSet

public object PropertyEntryPoint_InstructionSet : InstructionSet {
  override val memory: IntArray = IntArray(6)

  init {
    init0()
  }

  public fun init0() {
    memory[0] = 2
    memory[1] = 1
    memory[2] = 0
    memory[3] = 0
    memory[4] = 1
    memory[5] = 0
  }
}
