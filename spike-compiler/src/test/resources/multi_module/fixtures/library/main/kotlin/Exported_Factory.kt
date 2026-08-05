@file:Suppress("ClassName", "RedundantVisibilityModifier")

import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.Suppress
import spike.Provider
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSetPointer

public class Exported_Factory(
  private val _string: Provider<String>,
) : DependencyFactory() {
  override val maxConstructorArgs: Int = 1

  private val holder0: Exported_DependencyHolder0 = Exported_DependencyHolder0(this)

  override val instructionSet: IntArray
    get() = Exported_InstructionSet.memory

  public val string: String
    get() = _string.get()

  override fun getInstructionsPointer(id: DependencyId): InstructionSetPointer? = when (id.id) {
    0 -> InstructionSetPointer(0, 6)
    else -> error("Invalid identifier $id")
  }

  override fun instantiate(buffer: Array<Any?>, id: DependencyId): Any = when (id.segment) {
      0 -> holder0.create(buffer, id.position)
      else -> error("Invalid segment")
  }
}
