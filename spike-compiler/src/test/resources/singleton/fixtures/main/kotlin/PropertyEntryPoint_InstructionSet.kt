@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.IntArray
import kotlin.Suppress
import spike.factory.InstructionSet

public object PropertyEntryPoint_InstructionSet : InstructionSet {
  override val memory: IntArray = IntArray(19)

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
    memory[7] = 2
    memory[8] = 0
    memory[9] = 2
    memory[10] = 1
    memory[11] = 0
    memory[12] = 3
    memory[13] = 0
    memory[14] = 2
    memory[15] = 1
    memory[16] = 0
    memory[17] = 4
    memory[18] = 0
  }
}
