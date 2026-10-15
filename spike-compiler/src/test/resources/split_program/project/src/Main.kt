fun main() {
    val graph = SplitProgram()
    val first = graph.root
    val second = graph.root
    val deepest = first.chain.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next.next
    check(first.single === deepest.single)
    check(first.single === second.single)
    check(first.shared is Shared && deepest.shared is Shared)
    check(first.greeter is EnglishGreeter && deepest.greeter is EnglishGreeter)
    check(first.names.keys == setOf("ada", "grace"))
    check(deepest.names.values.all { it is Name })
}

@spike.EntryPoint
interface SplitProgram {
    val root: Root

    companion object
}

@spike.Include
class Root(
    val chain: Link0,
    val shared: Shared,
    val greeter: Greeter,
    val single: Single,
    val names: Map<String, Name>,
)

@spike.Include
class Shared

interface Greeter

@spike.Include
class EnglishGreeter : Greeter

@spike.Include
val EnglishGreeter.asGreeter: Greeter get() = this

@spike.Singleton
@spike.Include
class Single

@spike.Key
annotation class NameKey(val value: String)

class Name(val value: String)

@spike.Include(bindTo = spike.BindTarget.Map)
@NameKey("ada")
fun ada() = Name("ada")

@spike.Include(bindTo = spike.BindTarget.Map)
@NameKey("grace")
fun grace() = Name("grace")

@spike.Include
class Link0(val next: Link1)

@spike.Include
class Link1(val next: Link2)

@spike.Include
class Link2(val next: Link3)

@spike.Include
class Link3(val next: Link4)

@spike.Include
class Link4(val next: Link5)

@spike.Include
class Link5(val next: Link6)

@spike.Include
class Link6(val next: Link7)

@spike.Include
class Link7(val next: Link8)

@spike.Include
class Link8(val next: Link9)

@spike.Include
class Link9(val next: Link10)

@spike.Include
class Link10(val next: Link11)

@spike.Include
class Link11(val next: Link12)

@spike.Include
class Link12(val next: Link13)

@spike.Include
class Link13(val next: Link14)

@spike.Include
class Link14(val next: Link15)

@spike.Include
class Link15(val next: Link16)

@spike.Include
class Link16(val next: Link17)

@spike.Include
class Link17(val next: Link18)

@spike.Include
class Link18(val next: Link19)

@spike.Include
class Link19(val next: Link20)

@spike.Include
class Link20(val next: Link21)

@spike.Include
class Link21(val next: Link22)

@spike.Include
class Link22(val next: Link23)

@spike.Include
class Link23(val next: Link24)

@spike.Include
class Link24(val next: Link25)

@spike.Include
class Link25(val next: Link26)

@spike.Include
class Link26(val next: Link27)

@spike.Include
class Link27(val next: Link28)

@spike.Include
class Link28(val next: Link29)

@spike.Include
class Link29(val next: Link30)

@spike.Include
class Link30(val next: Link31)

@spike.Include
class Link31(val next: Link32)

@spike.Include
class Link32(val next: Link33)

@spike.Include
class Link33(val next: Link34)

@spike.Include
class Link34(val next: Link35)

@spike.Include
class Link35(val next: Link36)

@spike.Include
class Link36(val next: Link37)

@spike.Include
class Link37(val next: Link38)

@spike.Include
class Link38(val next: Link39)

@spike.Include
class Link39(val next: Link40)

@spike.Include
class Link40(val next: Link41)

@spike.Include
class Link41(val next: Link42)

@spike.Include
class Link42(val next: Link43)

@spike.Include
class Link43(val next: Link44)

@spike.Include
class Link44(val next: Link45)

@spike.Include
class Link45(val next: Link46)

@spike.Include
class Link46(val next: Link47)

@spike.Include
class Link47(val next: Link48)

@spike.Include
class Link48(val next: Link49)

@spike.Include
class Link49(val next: Link50)

@spike.Include
class Link50(val next: Link51)

@spike.Include
class Link51(val next: Link52)

@spike.Include
class Link52(val next: Link53)

@spike.Include
class Link53(val next: Link54)

@spike.Include
class Link54(val next: Link55)

@spike.Include
class Link55(val next: Link56)

@spike.Include
class Link56(val next: Link57)

@spike.Include
class Link57(val next: Link58)

@spike.Include
class Link58(val next: Link59)

@spike.Include
class Link59(val next: Link60)

@spike.Include
class Link60(val next: Link61)

@spike.Include
class Link61(val next: Link62)

@spike.Include
class Link62(val next: Link63)

@spike.Include
class Link63(val next: Link64)

@spike.Include
class Link64(val next: Link65)

@spike.Include
class Link65(val next: Link66)

@spike.Include
class Link66(val next: Link67)

@spike.Include
class Link67(val next: Link68)

@spike.Include
class Link68(val next: Link69)

@spike.Include
class Link69(val next: Link70)

@spike.Include
class Link70(val next: Link71)

@spike.Include
class Link71(val next: Link72)

@spike.Include
class Link72(val next: Link73)

@spike.Include
class Link73(val next: Link74)

@spike.Include
class Link74(val next: Link75)

@spike.Include
class Link75(val next: Link76)

@spike.Include
class Link76(val next: Link77)

@spike.Include
class Link77(val next: Link78)

@spike.Include
class Link78(val next: Link79)

@spike.Include
class Link79(val next: Link80)

@spike.Include
class Link80(val next: Link81)

@spike.Include
class Link81(val next: Link82)

@spike.Include
class Link82(val next: Link83)

@spike.Include
class Link83(val next: Link84)

@spike.Include
class Link84(val next: Link85)

@spike.Include
class Link85(val next: Link86)

@spike.Include
class Link86(val next: Link87)

@spike.Include
class Link87(val next: Link88)

@spike.Include
class Link88(val next: Link89)

@spike.Include
class Link89(val next: Link90)

@spike.Include
class Link90(val next: Link91)

@spike.Include
class Link91(val next: Link92)

@spike.Include
class Link92(val next: Link93)

@spike.Include
class Link93(val next: Link94)

@spike.Include
class Link94(val next: Link95)

@spike.Include
class Link95(val next: Link96)

@spike.Include
class Link96(val next: Link97)

@spike.Include
class Link97(val next: Link98)

@spike.Include
class Link98(val next: Link99)

@spike.Include
class Link99(val next: Link100)

@spike.Include
class Link100(val next: Link101)

@spike.Include
class Link101(val next: Link102)

@spike.Include
class Link102(val next: Link103)

@spike.Include
class Link103(val next: Link104)

@spike.Include
class Link104(val next: Link105)

@spike.Include
class Link105(val next: Link106)

@spike.Include
class Link106(val next: Link107)

@spike.Include
class Link107(val next: Link108)

@spike.Include
class Link108(val next: Link109)

@spike.Include
class Link109(val next: Link110)

@spike.Include
class Link110(val next: Link111)

@spike.Include
class Link111(val next: Link112)

@spike.Include
class Link112(val next: Link113)

@spike.Include
class Link113(val next: Link114)

@spike.Include
class Link114(val next: Link115)

@spike.Include
class Link115(val next: Link116)

@spike.Include
class Link116(val next: Link117)

@spike.Include
class Link117(val next: Link118)

@spike.Include
class Link118(val next: Link119)

@spike.Include
class Link119(val next: Link120)

@spike.Include
class Link120(val next: Link121)

@spike.Include
class Link121(val next: Link122)

@spike.Include
class Link122(val next: Link123)

@spike.Include
class Link123(val next: Link124)

@spike.Include
class Link124(val next: Link125)

@spike.Include
class Link125(val next: Link126)

@spike.Include
class Link126(val next: Link127)

@spike.Include
class Link127(val next: Link128)

@spike.Include
class Link128(val next: Link129)

@spike.Include
class Link129(val next: Link130)

@spike.Include
class Link130(val next: Link131)

@spike.Include
class Link131(val next: Link132)

@spike.Include
class Link132(val next: Link133)

@spike.Include
class Link133(val next: Link134)

@spike.Include
class Link134(val next: Link135)

@spike.Include
class Link135(val next: Link136)

@spike.Include
class Link136(val next: Link137)

@spike.Include
class Link137(val next: Link138)

@spike.Include
class Link138(val next: Link139)

@spike.Include
class Link139(val next: Link140)

@spike.Include
class Link140(val next: Link141)

@spike.Include
class Link141(val next: Link142)

@spike.Include
class Link142(val next: Link143)

@spike.Include
class Link143(val next: Link144)

@spike.Include
class Link144(val next: Link145)

@spike.Include
class Link145(val next: Link146)

@spike.Include
class Link146(val next: Link147)

@spike.Include
class Link147(val next: Link148)

@spike.Include
class Link148(val next: Link149)

@spike.Include
class Link149(val next: Link150)

@spike.Include
class Link150(val next: Link151)

@spike.Include
class Link151(val next: Link152)

@spike.Include
class Link152(val next: Link153)

@spike.Include
class Link153(val next: Link154)

@spike.Include
class Link154(val next: Link155)

@spike.Include
class Link155(val next: Link156)

@spike.Include
class Link156(val next: Link157)

@spike.Include
class Link157(val next: Link158)

@spike.Include
class Link158(val next: Link159)

@spike.Include
class Link159(val next: Link160)

@spike.Include
class Link160(val next: Link161)

@spike.Include
class Link161(val next: Link162)

@spike.Include
class Link162(val next: Link163)

@spike.Include
class Link163(val next: Link164)

@spike.Include
class Link164(val next: Link165)

@spike.Include
class Link165(val next: Link166)

@spike.Include
class Link166(val next: Link167)

@spike.Include
class Link167(val next: Link168)

@spike.Include
class Link168(val next: Link169)

@spike.Include
class Link169(val next: Link170)

@spike.Include
class Link170(val next: Link171)

@spike.Include
class Link171(val next: Link172)

@spike.Include
class Link172(val next: Link173)

@spike.Include
class Link173(val next: Link174)

@spike.Include
class Link174(val next: Link175)

@spike.Include
class Link175(val next: Link176)

@spike.Include
class Link176(val next: Link177)

@spike.Include
class Link177(val next: Link178)

@spike.Include
class Link178(val next: Link179)

@spike.Include
class Link179(val next: Link180)

@spike.Include
class Link180(val next: Link181)

@spike.Include
class Link181(val next: Link182)

@spike.Include
class Link182(val next: Link183)

@spike.Include
class Link183(val next: Link184)

@spike.Include
class Link184(val next: Link185)

@spike.Include
class Link185(val next: Link186)

@spike.Include
class Link186(val next: Link187)

@spike.Include
class Link187(val next: Link188)

@spike.Include
class Link188(val next: Link189)

@spike.Include
class Link189(val next: Link190)

@spike.Include
class Link190(val next: Link191)

@spike.Include
class Link191(val next: Link192)

@spike.Include
class Link192(val next: Link193)

@spike.Include
class Link193(val next: Link194)

@spike.Include
class Link194(val next: Link195)

@spike.Include
class Link195(val next: Link196)

@spike.Include
class Link196(val next: Link197)

@spike.Include
class Link197(val next: Link198)

@spike.Include
class Link198(val next: Link199)

@spike.Include
class Link199(
    val shared: Shared,
    val greeter: Greeter,
    val single: Single,
    val names: Map<String, Name>,
)
