import kotlin.IntArray
import spike.factory.InstructionSet

public object PropertyEntryPoint_InstructionSet : InstructionSet {
  override val memory: IntArray = IntArray(3)

  init {
    init0()
  }

  public fun init0() {
    memory[0] = 1
    memory[1] = 0
    memory[2] = 0
  }
}
