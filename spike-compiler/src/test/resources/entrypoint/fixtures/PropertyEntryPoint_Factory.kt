import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSetPointer

public object PropertyEntryPoint_Factory : DependencyFactory() {
  override val maxConstructorArgs: Int = 0

  override val instructionSet: IntArray
    get() = PropertyEntryPoint_InstructionSet.memory

  override fun getInstructionsPointer(id: DependencyId): InstructionSetPointer? = when (id.id) {
    0 -> InstructionSetPointer(0, 3)
    else -> error("Invalid identifier")
  }

  override fun instantiate(buffer: Array<Any?>, id: DependencyId): Any = when (id.segment) {
      0 -> PropertyEntryPoint_DependencyHolder0.create(buffer, id.position)
      else -> error("Invalid segment")
  }
}
