@file:OptIn(InternalSpikeApi::class)
@file:Suppress("ClassName")

package spike.aggregated

import kotlin.OptIn
import kotlin.Suppress
import spike.InternalSpikeApi
import spike.factory.IncludedDeclarations

@IncludedDeclarations(names = ["c:Box", "c:Cat", "c:ComplexObject", "c:TeleportedCat"])
public object PropertyEntryPoint
