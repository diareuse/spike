package spike.lifecycle.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSetPointer

public class ViewModelEntryPoint_Factory(
  public val handle: SavedStateHandle,
) : DependencyFactory() {
  override val maxConstructorArgs: Int = 1

  private val holder0: ViewModelEntryPoint_DependencyHolder0 =
      ViewModelEntryPoint_DependencyHolder0(this)

  override val instructionSet: IntArray
    get() = ViewModelEntryPoint_InstructionSet.memory

  override fun getInstructionsPointer(id: DependencyId): InstructionSetPointer? = when (id.id) {
    0 -> InstructionSetPointer(0, 6)
    1 -> InstructionSetPointer(6, 5)
    3 -> InstructionSetPointer(11, 6)
    else -> error("Invalid identifier ${id}")
  }

  override fun instantiate(buffer: Array<Any?>, id: DependencyId): Any = when (id.segment) {
      0 -> holder0.create(buffer, id.position)
      else -> error("Invalid segment")
  }
}
