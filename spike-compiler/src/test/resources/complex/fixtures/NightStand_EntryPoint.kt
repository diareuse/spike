private object NightStand_EntryPoint : NightStand {
  override val television: Television
    get() = NightStand_Factory.get(spike.factory.DependencyId(0))

  override val remote: Remote
    get() = NightStand_Factory.get(spike.factory.DependencyId(5))
}

public operator fun NightStand.Companion.invoke(): NightStand = NightStand_EntryPoint
