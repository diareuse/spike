fun main() {
    val graph = SharedLattice()
    check(graph.root.right.left.right.left is Left4)
}

@spike.EntryPoint
interface SharedLattice {
    val root: Left0

    companion object
}

@spike.Include
class Left0(val left: Left1, val right: Right1)

@spike.Include
class Right0(val left: Left1, val right: Right1)

@spike.Include
class Left1(val left: Left2, val right: Right2)

@spike.Include
class Right1(val left: Left2, val right: Right2)

@spike.Include
class Left2(val left: Left3, val right: Right3)

@spike.Include
class Right2(val left: Left3, val right: Right3)

@spike.Include
class Left3(val left: Left4, val right: Right4)

@spike.Include
class Right3(val left: Left4, val right: Right4)

@spike.Include
class Left4(val left: Left5, val right: Right5)

@spike.Include
class Right4(val left: Left5, val right: Right5)

@spike.Include
class Left5(val left: Left6, val right: Right6)

@spike.Include
class Right5(val left: Left6, val right: Right6)

@spike.Include
class Left6(val left: Left7, val right: Right7)

@spike.Include
class Right6(val left: Left7, val right: Right7)

@spike.Include
class Left7(val left: Left8, val right: Right8)

@spike.Include
class Right7(val left: Left8, val right: Right8)

@spike.Include
class Left8(val left: Left9, val right: Right9)

@spike.Include
class Right8(val left: Left9, val right: Right9)

@spike.Include
class Left9(val left: Left10, val right: Right10)

@spike.Include
class Right9(val left: Left10, val right: Right10)

@spike.Include
class Left10(val left: Left11, val right: Right11)

@spike.Include
class Right10(val left: Left11, val right: Right11)

@spike.Include
class Left11(val left: Left12, val right: Right12)

@spike.Include
class Right11(val left: Left12, val right: Right12)

@spike.Include
class Left12(val left: Left13, val right: Right13)

@spike.Include
class Right12(val left: Left13, val right: Right13)

@spike.Include
class Left13(val left: Left14, val right: Right14)

@spike.Include
class Right13(val left: Left14, val right: Right14)

@spike.Include
class Left14(val left: Left15, val right: Right15)

@spike.Include
class Right14(val left: Left15, val right: Right15)

@spike.Include
class Left15(val left: Left16, val right: Right16)

@spike.Include
class Right15(val left: Left16, val right: Right16)

@spike.Include
class Left16(val left: Left17, val right: Right17)

@spike.Include
class Right16(val left: Left17, val right: Right17)

@spike.Include
class Left17(val left: Left18, val right: Right18)

@spike.Include
class Right17(val left: Left18, val right: Right18)

@spike.Include
class Left18(val left: Left19, val right: Right19)

@spike.Include
class Right18(val left: Left19, val right: Right19)

@spike.Include
class Left19(val left: Left20, val right: Right20)

@spike.Include
class Right19(val left: Left20, val right: Right20)

@spike.Include
class Left20(val left: Left21, val right: Right21)

@spike.Include
class Right20(val left: Left21, val right: Right21)

@spike.Include
class Left21(val left: Left22, val right: Right22)

@spike.Include
class Right21(val left: Left22, val right: Right22)

@spike.Include
class Left22(val left: Left23, val right: Right23)

@spike.Include
class Right22(val left: Left23, val right: Right23)

@spike.Include
class Left23(val left: Left24, val right: Right24)

@spike.Include
class Right23(val left: Left24, val right: Right24)

@spike.Include
class Left24

@spike.Include
class Right24
