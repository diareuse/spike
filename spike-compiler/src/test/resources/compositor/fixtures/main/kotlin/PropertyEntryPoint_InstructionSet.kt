@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.IntArray
import kotlin.Suppress
import spike.factory.InstructionSet

public object PropertyEntryPoint_InstructionSet : InstructionSet {
  override val memory: IntArray = IntArray(15)

  init {
    init0()
  }

  public fun init0() {
    memory[0] = 4
    memory[1] = 1
    memory[2] = 0
    memory[3] = 2
    memory[4] = 1
    memory[5] = 0
    memory[6] = 3
    memory[7] = 2
    memory[8] = 1
    memory[9] = 0
    memory[10] = 0
    memory[11] = 3
    memory[12] = 0
    memory[13] = 1
    memory[14] = 2
  }
}
