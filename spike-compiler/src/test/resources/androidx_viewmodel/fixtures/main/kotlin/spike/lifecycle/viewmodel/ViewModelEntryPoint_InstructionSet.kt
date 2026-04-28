@file:Suppress("ClassName", "RedundantVisibilityModifier")

package spike.lifecycle.viewmodel

import kotlin.IntArray
import kotlin.Suppress
import spike.factory.InstructionSet

public object ViewModelEntryPoint_InstructionSet : InstructionSet {
  override val memory: IntArray = IntArray(17)

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
    memory[7] = 2
    memory[8] = 0
    memory[9] = 1
    memory[10] = 0
    memory[11] = 2
    memory[12] = 2
    memory[13] = 0
    memory[14] = 3
    memory[15] = 1
    memory[16] = 0
  }
}
