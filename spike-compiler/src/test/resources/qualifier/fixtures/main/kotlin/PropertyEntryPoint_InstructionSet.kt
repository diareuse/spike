@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.IntArray
import kotlin.Suppress
import spike.factory.InstructionSet

public object PropertyEntryPoint_InstructionSet : InstructionSet {
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
    memory[6] = 2
    memory[7] = 3
    memory[8] = 0
    memory[9] = 2
    memory[10] = 1
    memory[11] = 0
  }
}
