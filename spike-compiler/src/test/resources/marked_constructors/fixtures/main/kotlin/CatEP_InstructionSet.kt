@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.IntArray
import kotlin.Suppress
import spike.factory.InstructionSet

public object CatEP_InstructionSet : InstructionSet {
  override val memory: IntArray = IntArray(8)

  init {
    init0()
  }

  public fun init0() {
    memory[0] = 2
    memory[1] = 1
    memory[2] = 0
    memory[3] = 0
    memory[4] = 3
    memory[5] = 0
    memory[6] = 0
    memory[7] = 0
  }
}
