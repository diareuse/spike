package spike.lifecycle.viewmodel

import kotlin.IntArray
import spike.factory.InstructionSet

public object ViewModelEntryPoint_InstructionSet : InstructionSet {
  override val memory: IntArray = IntArray(12)

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
    memory[6] = 1
    memory[7] = 1
    memory[8] = 0
    memory[9] = 1
    memory[10] = 2
    memory[11] = 0
  }
}
