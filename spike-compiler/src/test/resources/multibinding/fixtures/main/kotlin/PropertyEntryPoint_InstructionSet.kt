import kotlin.IntArray
import spike.factory.InstructionSet

public object PropertyEntryPoint_InstructionSet : InstructionSet {
  override val memory: IntArray = IntArray(45)

  init {
    init0()
  }

  public fun init0() {
    memory[0] = 3
    memory[1] = 1
    memory[2] = 0
    memory[3] = 2
    memory[4] = 0
    memory[5] = 0
    memory[6] = 2
    memory[7] = 1
    memory[8] = 0
    memory[9] = 1
    memory[10] = 2
    memory[11] = 0
    memory[12] = 1
    memory[13] = 1
    memory[14] = 0
    memory[15] = 3
    memory[16] = 4
    memory[17] = 0
    memory[18] = 5
    memory[19] = 0
    memory[20] = 3
    memory[21] = 2
    memory[22] = 1
    memory[23] = 0
    memory[24] = 1
    memory[25] = 5
    memory[26] = 0
    memory[27] = 1
    memory[28] = 4
    memory[29] = 0
    memory[30] = 3
    memory[31] = 7
    memory[32] = 0
    memory[33] = 8
    memory[34] = 0
    memory[35] = 6
    memory[36] = 2
    memory[37] = 1
    memory[38] = 0
    memory[39] = 1
    memory[40] = 8
    memory[41] = 0
    memory[42] = 1
    memory[43] = 7
    memory[44] = 0
  }
}
