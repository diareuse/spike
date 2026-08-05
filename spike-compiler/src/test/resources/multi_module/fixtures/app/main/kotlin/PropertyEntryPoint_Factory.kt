@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import kotlin.Suppress
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSetPointer
import spike.generated.ExportedImpl

public class PropertyEntryPoint_Factory() : DependencyFactory() {
  override val maxConstructorArgs: Int = 0

  private val holder0: PropertyEntryPoint_DependencyHolder0 =
      PropertyEntryPoint_DependencyHolder0(this)

  override val instructionSet: IntArray
    get() = PropertyEntryPoint_InstructionSet.memory

  public val exportedImpl: ExportedImpl = ExportedImpl(string = get(DependencyId(1)))

  override fun getInstructionsPointer(id: DependencyId): InstructionSetPointer? = when (id.id) {
    0 -> InstructionSetPointer(0, 3)
    1 -> InstructionSetPointer(3, 3)
    2 -> InstructionSetPointer(6, 3)
    else -> error("Invalid identifier $id")
  }

  override fun instantiate(buffer: Array<Any?>, id: DependencyId): Any = when (id.segment) {
      0 -> holder0.create(buffer, id.position)
      else -> error("Invalid segment")
  }
}
