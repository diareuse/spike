import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSetPointer

public class NightStand_Factory() : DependencyFactory() {
  override val maxConstructorArgs: Int = 2

  private val holder0: NightStand_DependencyHolder0 = NightStand_DependencyHolder0(this)

  override val instructionSet: IntArray
    get() = NightStand_InstructionSet.memory

  override fun getInstructionsPointer(id: DependencyId): InstructionSetPointer? = when (id.id) {
    0 -> InstructionSetPointer(0, 9)
    3 -> InstructionSetPointer(9, 6)
    5 -> InstructionSetPointer(15, 12)
    else -> error("Invalid identifier ${id}")
  }

  override fun instantiate(buffer: Array<Any?>, id: DependencyId): Any = when (id.segment) {
      0 -> holder0.create(buffer, id.position)
      else -> error("Invalid segment")
  }
}
