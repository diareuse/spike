fun main() {
    val cats = Cats().cats
    check(cats.size == 600) { "had ${cats.size} cats" }
}

@spike.EntryPoint
interface Cats {
    val cats: Map<String, Cat>

    companion object
}

data class Cat(val name: String)

@spike.Key
annotation class Name(val value: String)

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat0")
fun cat0() = Cat("cat0")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat1")
fun cat1() = Cat("cat1")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat2")
fun cat2() = Cat("cat2")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat3")
fun cat3() = Cat("cat3")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat4")
fun cat4() = Cat("cat4")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat5")
fun cat5() = Cat("cat5")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat6")
fun cat6() = Cat("cat6")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat7")
fun cat7() = Cat("cat7")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat8")
fun cat8() = Cat("cat8")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat9")
fun cat9() = Cat("cat9")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat10")
fun cat10() = Cat("cat10")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat11")
fun cat11() = Cat("cat11")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat12")
fun cat12() = Cat("cat12")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat13")
fun cat13() = Cat("cat13")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat14")
fun cat14() = Cat("cat14")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat15")
fun cat15() = Cat("cat15")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat16")
fun cat16() = Cat("cat16")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat17")
fun cat17() = Cat("cat17")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat18")
fun cat18() = Cat("cat18")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat19")
fun cat19() = Cat("cat19")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat20")
fun cat20() = Cat("cat20")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat21")
fun cat21() = Cat("cat21")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat22")
fun cat22() = Cat("cat22")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat23")
fun cat23() = Cat("cat23")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat24")
fun cat24() = Cat("cat24")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat25")
fun cat25() = Cat("cat25")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat26")
fun cat26() = Cat("cat26")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat27")
fun cat27() = Cat("cat27")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat28")
fun cat28() = Cat("cat28")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat29")
fun cat29() = Cat("cat29")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat30")
fun cat30() = Cat("cat30")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat31")
fun cat31() = Cat("cat31")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat32")
fun cat32() = Cat("cat32")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat33")
fun cat33() = Cat("cat33")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat34")
fun cat34() = Cat("cat34")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat35")
fun cat35() = Cat("cat35")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat36")
fun cat36() = Cat("cat36")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat37")
fun cat37() = Cat("cat37")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat38")
fun cat38() = Cat("cat38")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat39")
fun cat39() = Cat("cat39")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat40")
fun cat40() = Cat("cat40")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat41")
fun cat41() = Cat("cat41")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat42")
fun cat42() = Cat("cat42")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat43")
fun cat43() = Cat("cat43")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat44")
fun cat44() = Cat("cat44")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat45")
fun cat45() = Cat("cat45")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat46")
fun cat46() = Cat("cat46")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat47")
fun cat47() = Cat("cat47")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat48")
fun cat48() = Cat("cat48")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat49")
fun cat49() = Cat("cat49")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat50")
fun cat50() = Cat("cat50")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat51")
fun cat51() = Cat("cat51")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat52")
fun cat52() = Cat("cat52")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat53")
fun cat53() = Cat("cat53")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat54")
fun cat54() = Cat("cat54")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat55")
fun cat55() = Cat("cat55")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat56")
fun cat56() = Cat("cat56")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat57")
fun cat57() = Cat("cat57")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat58")
fun cat58() = Cat("cat58")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat59")
fun cat59() = Cat("cat59")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat60")
fun cat60() = Cat("cat60")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat61")
fun cat61() = Cat("cat61")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat62")
fun cat62() = Cat("cat62")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat63")
fun cat63() = Cat("cat63")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat64")
fun cat64() = Cat("cat64")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat65")
fun cat65() = Cat("cat65")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat66")
fun cat66() = Cat("cat66")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat67")
fun cat67() = Cat("cat67")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat68")
fun cat68() = Cat("cat68")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat69")
fun cat69() = Cat("cat69")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat70")
fun cat70() = Cat("cat70")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat71")
fun cat71() = Cat("cat71")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat72")
fun cat72() = Cat("cat72")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat73")
fun cat73() = Cat("cat73")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat74")
fun cat74() = Cat("cat74")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat75")
fun cat75() = Cat("cat75")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat76")
fun cat76() = Cat("cat76")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat77")
fun cat77() = Cat("cat77")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat78")
fun cat78() = Cat("cat78")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat79")
fun cat79() = Cat("cat79")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat80")
fun cat80() = Cat("cat80")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat81")
fun cat81() = Cat("cat81")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat82")
fun cat82() = Cat("cat82")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat83")
fun cat83() = Cat("cat83")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat84")
fun cat84() = Cat("cat84")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat85")
fun cat85() = Cat("cat85")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat86")
fun cat86() = Cat("cat86")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat87")
fun cat87() = Cat("cat87")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat88")
fun cat88() = Cat("cat88")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat89")
fun cat89() = Cat("cat89")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat90")
fun cat90() = Cat("cat90")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat91")
fun cat91() = Cat("cat91")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat92")
fun cat92() = Cat("cat92")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat93")
fun cat93() = Cat("cat93")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat94")
fun cat94() = Cat("cat94")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat95")
fun cat95() = Cat("cat95")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat96")
fun cat96() = Cat("cat96")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat97")
fun cat97() = Cat("cat97")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat98")
fun cat98() = Cat("cat98")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat99")
fun cat99() = Cat("cat99")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat100")
fun cat100() = Cat("cat100")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat101")
fun cat101() = Cat("cat101")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat102")
fun cat102() = Cat("cat102")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat103")
fun cat103() = Cat("cat103")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat104")
fun cat104() = Cat("cat104")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat105")
fun cat105() = Cat("cat105")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat106")
fun cat106() = Cat("cat106")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat107")
fun cat107() = Cat("cat107")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat108")
fun cat108() = Cat("cat108")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat109")
fun cat109() = Cat("cat109")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat110")
fun cat110() = Cat("cat110")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat111")
fun cat111() = Cat("cat111")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat112")
fun cat112() = Cat("cat112")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat113")
fun cat113() = Cat("cat113")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat114")
fun cat114() = Cat("cat114")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat115")
fun cat115() = Cat("cat115")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat116")
fun cat116() = Cat("cat116")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat117")
fun cat117() = Cat("cat117")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat118")
fun cat118() = Cat("cat118")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat119")
fun cat119() = Cat("cat119")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat120")
fun cat120() = Cat("cat120")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat121")
fun cat121() = Cat("cat121")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat122")
fun cat122() = Cat("cat122")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat123")
fun cat123() = Cat("cat123")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat124")
fun cat124() = Cat("cat124")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat125")
fun cat125() = Cat("cat125")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat126")
fun cat126() = Cat("cat126")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat127")
fun cat127() = Cat("cat127")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat128")
fun cat128() = Cat("cat128")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat129")
fun cat129() = Cat("cat129")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat130")
fun cat130() = Cat("cat130")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat131")
fun cat131() = Cat("cat131")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat132")
fun cat132() = Cat("cat132")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat133")
fun cat133() = Cat("cat133")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat134")
fun cat134() = Cat("cat134")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat135")
fun cat135() = Cat("cat135")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat136")
fun cat136() = Cat("cat136")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat137")
fun cat137() = Cat("cat137")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat138")
fun cat138() = Cat("cat138")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat139")
fun cat139() = Cat("cat139")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat140")
fun cat140() = Cat("cat140")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat141")
fun cat141() = Cat("cat141")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat142")
fun cat142() = Cat("cat142")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat143")
fun cat143() = Cat("cat143")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat144")
fun cat144() = Cat("cat144")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat145")
fun cat145() = Cat("cat145")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat146")
fun cat146() = Cat("cat146")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat147")
fun cat147() = Cat("cat147")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat148")
fun cat148() = Cat("cat148")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat149")
fun cat149() = Cat("cat149")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat150")
fun cat150() = Cat("cat150")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat151")
fun cat151() = Cat("cat151")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat152")
fun cat152() = Cat("cat152")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat153")
fun cat153() = Cat("cat153")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat154")
fun cat154() = Cat("cat154")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat155")
fun cat155() = Cat("cat155")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat156")
fun cat156() = Cat("cat156")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat157")
fun cat157() = Cat("cat157")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat158")
fun cat158() = Cat("cat158")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat159")
fun cat159() = Cat("cat159")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat160")
fun cat160() = Cat("cat160")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat161")
fun cat161() = Cat("cat161")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat162")
fun cat162() = Cat("cat162")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat163")
fun cat163() = Cat("cat163")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat164")
fun cat164() = Cat("cat164")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat165")
fun cat165() = Cat("cat165")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat166")
fun cat166() = Cat("cat166")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat167")
fun cat167() = Cat("cat167")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat168")
fun cat168() = Cat("cat168")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat169")
fun cat169() = Cat("cat169")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat170")
fun cat170() = Cat("cat170")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat171")
fun cat171() = Cat("cat171")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat172")
fun cat172() = Cat("cat172")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat173")
fun cat173() = Cat("cat173")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat174")
fun cat174() = Cat("cat174")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat175")
fun cat175() = Cat("cat175")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat176")
fun cat176() = Cat("cat176")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat177")
fun cat177() = Cat("cat177")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat178")
fun cat178() = Cat("cat178")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat179")
fun cat179() = Cat("cat179")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat180")
fun cat180() = Cat("cat180")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat181")
fun cat181() = Cat("cat181")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat182")
fun cat182() = Cat("cat182")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat183")
fun cat183() = Cat("cat183")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat184")
fun cat184() = Cat("cat184")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat185")
fun cat185() = Cat("cat185")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat186")
fun cat186() = Cat("cat186")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat187")
fun cat187() = Cat("cat187")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat188")
fun cat188() = Cat("cat188")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat189")
fun cat189() = Cat("cat189")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat190")
fun cat190() = Cat("cat190")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat191")
fun cat191() = Cat("cat191")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat192")
fun cat192() = Cat("cat192")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat193")
fun cat193() = Cat("cat193")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat194")
fun cat194() = Cat("cat194")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat195")
fun cat195() = Cat("cat195")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat196")
fun cat196() = Cat("cat196")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat197")
fun cat197() = Cat("cat197")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat198")
fun cat198() = Cat("cat198")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat199")
fun cat199() = Cat("cat199")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat200")
fun cat200() = Cat("cat200")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat201")
fun cat201() = Cat("cat201")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat202")
fun cat202() = Cat("cat202")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat203")
fun cat203() = Cat("cat203")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat204")
fun cat204() = Cat("cat204")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat205")
fun cat205() = Cat("cat205")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat206")
fun cat206() = Cat("cat206")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat207")
fun cat207() = Cat("cat207")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat208")
fun cat208() = Cat("cat208")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat209")
fun cat209() = Cat("cat209")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat210")
fun cat210() = Cat("cat210")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat211")
fun cat211() = Cat("cat211")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat212")
fun cat212() = Cat("cat212")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat213")
fun cat213() = Cat("cat213")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat214")
fun cat214() = Cat("cat214")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat215")
fun cat215() = Cat("cat215")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat216")
fun cat216() = Cat("cat216")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat217")
fun cat217() = Cat("cat217")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat218")
fun cat218() = Cat("cat218")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat219")
fun cat219() = Cat("cat219")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat220")
fun cat220() = Cat("cat220")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat221")
fun cat221() = Cat("cat221")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat222")
fun cat222() = Cat("cat222")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat223")
fun cat223() = Cat("cat223")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat224")
fun cat224() = Cat("cat224")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat225")
fun cat225() = Cat("cat225")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat226")
fun cat226() = Cat("cat226")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat227")
fun cat227() = Cat("cat227")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat228")
fun cat228() = Cat("cat228")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat229")
fun cat229() = Cat("cat229")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat230")
fun cat230() = Cat("cat230")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat231")
fun cat231() = Cat("cat231")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat232")
fun cat232() = Cat("cat232")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat233")
fun cat233() = Cat("cat233")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat234")
fun cat234() = Cat("cat234")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat235")
fun cat235() = Cat("cat235")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat236")
fun cat236() = Cat("cat236")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat237")
fun cat237() = Cat("cat237")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat238")
fun cat238() = Cat("cat238")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat239")
fun cat239() = Cat("cat239")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat240")
fun cat240() = Cat("cat240")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat241")
fun cat241() = Cat("cat241")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat242")
fun cat242() = Cat("cat242")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat243")
fun cat243() = Cat("cat243")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat244")
fun cat244() = Cat("cat244")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat245")
fun cat245() = Cat("cat245")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat246")
fun cat246() = Cat("cat246")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat247")
fun cat247() = Cat("cat247")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat248")
fun cat248() = Cat("cat248")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat249")
fun cat249() = Cat("cat249")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat250")
fun cat250() = Cat("cat250")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat251")
fun cat251() = Cat("cat251")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat252")
fun cat252() = Cat("cat252")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat253")
fun cat253() = Cat("cat253")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat254")
fun cat254() = Cat("cat254")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat255")
fun cat255() = Cat("cat255")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat256")
fun cat256() = Cat("cat256")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat257")
fun cat257() = Cat("cat257")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat258")
fun cat258() = Cat("cat258")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat259")
fun cat259() = Cat("cat259")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat260")
fun cat260() = Cat("cat260")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat261")
fun cat261() = Cat("cat261")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat262")
fun cat262() = Cat("cat262")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat263")
fun cat263() = Cat("cat263")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat264")
fun cat264() = Cat("cat264")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat265")
fun cat265() = Cat("cat265")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat266")
fun cat266() = Cat("cat266")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat267")
fun cat267() = Cat("cat267")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat268")
fun cat268() = Cat("cat268")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat269")
fun cat269() = Cat("cat269")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat270")
fun cat270() = Cat("cat270")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat271")
fun cat271() = Cat("cat271")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat272")
fun cat272() = Cat("cat272")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat273")
fun cat273() = Cat("cat273")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat274")
fun cat274() = Cat("cat274")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat275")
fun cat275() = Cat("cat275")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat276")
fun cat276() = Cat("cat276")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat277")
fun cat277() = Cat("cat277")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat278")
fun cat278() = Cat("cat278")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat279")
fun cat279() = Cat("cat279")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat280")
fun cat280() = Cat("cat280")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat281")
fun cat281() = Cat("cat281")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat282")
fun cat282() = Cat("cat282")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat283")
fun cat283() = Cat("cat283")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat284")
fun cat284() = Cat("cat284")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat285")
fun cat285() = Cat("cat285")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat286")
fun cat286() = Cat("cat286")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat287")
fun cat287() = Cat("cat287")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat288")
fun cat288() = Cat("cat288")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat289")
fun cat289() = Cat("cat289")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat290")
fun cat290() = Cat("cat290")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat291")
fun cat291() = Cat("cat291")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat292")
fun cat292() = Cat("cat292")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat293")
fun cat293() = Cat("cat293")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat294")
fun cat294() = Cat("cat294")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat295")
fun cat295() = Cat("cat295")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat296")
fun cat296() = Cat("cat296")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat297")
fun cat297() = Cat("cat297")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat298")
fun cat298() = Cat("cat298")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat299")
fun cat299() = Cat("cat299")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat300")
fun cat300() = Cat("cat300")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat301")
fun cat301() = Cat("cat301")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat302")
fun cat302() = Cat("cat302")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat303")
fun cat303() = Cat("cat303")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat304")
fun cat304() = Cat("cat304")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat305")
fun cat305() = Cat("cat305")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat306")
fun cat306() = Cat("cat306")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat307")
fun cat307() = Cat("cat307")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat308")
fun cat308() = Cat("cat308")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat309")
fun cat309() = Cat("cat309")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat310")
fun cat310() = Cat("cat310")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat311")
fun cat311() = Cat("cat311")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat312")
fun cat312() = Cat("cat312")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat313")
fun cat313() = Cat("cat313")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat314")
fun cat314() = Cat("cat314")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat315")
fun cat315() = Cat("cat315")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat316")
fun cat316() = Cat("cat316")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat317")
fun cat317() = Cat("cat317")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat318")
fun cat318() = Cat("cat318")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat319")
fun cat319() = Cat("cat319")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat320")
fun cat320() = Cat("cat320")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat321")
fun cat321() = Cat("cat321")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat322")
fun cat322() = Cat("cat322")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat323")
fun cat323() = Cat("cat323")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat324")
fun cat324() = Cat("cat324")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat325")
fun cat325() = Cat("cat325")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat326")
fun cat326() = Cat("cat326")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat327")
fun cat327() = Cat("cat327")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat328")
fun cat328() = Cat("cat328")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat329")
fun cat329() = Cat("cat329")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat330")
fun cat330() = Cat("cat330")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat331")
fun cat331() = Cat("cat331")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat332")
fun cat332() = Cat("cat332")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat333")
fun cat333() = Cat("cat333")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat334")
fun cat334() = Cat("cat334")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat335")
fun cat335() = Cat("cat335")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat336")
fun cat336() = Cat("cat336")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat337")
fun cat337() = Cat("cat337")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat338")
fun cat338() = Cat("cat338")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat339")
fun cat339() = Cat("cat339")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat340")
fun cat340() = Cat("cat340")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat341")
fun cat341() = Cat("cat341")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat342")
fun cat342() = Cat("cat342")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat343")
fun cat343() = Cat("cat343")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat344")
fun cat344() = Cat("cat344")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat345")
fun cat345() = Cat("cat345")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat346")
fun cat346() = Cat("cat346")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat347")
fun cat347() = Cat("cat347")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat348")
fun cat348() = Cat("cat348")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat349")
fun cat349() = Cat("cat349")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat350")
fun cat350() = Cat("cat350")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat351")
fun cat351() = Cat("cat351")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat352")
fun cat352() = Cat("cat352")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat353")
fun cat353() = Cat("cat353")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat354")
fun cat354() = Cat("cat354")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat355")
fun cat355() = Cat("cat355")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat356")
fun cat356() = Cat("cat356")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat357")
fun cat357() = Cat("cat357")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat358")
fun cat358() = Cat("cat358")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat359")
fun cat359() = Cat("cat359")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat360")
fun cat360() = Cat("cat360")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat361")
fun cat361() = Cat("cat361")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat362")
fun cat362() = Cat("cat362")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat363")
fun cat363() = Cat("cat363")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat364")
fun cat364() = Cat("cat364")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat365")
fun cat365() = Cat("cat365")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat366")
fun cat366() = Cat("cat366")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat367")
fun cat367() = Cat("cat367")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat368")
fun cat368() = Cat("cat368")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat369")
fun cat369() = Cat("cat369")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat370")
fun cat370() = Cat("cat370")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat371")
fun cat371() = Cat("cat371")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat372")
fun cat372() = Cat("cat372")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat373")
fun cat373() = Cat("cat373")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat374")
fun cat374() = Cat("cat374")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat375")
fun cat375() = Cat("cat375")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat376")
fun cat376() = Cat("cat376")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat377")
fun cat377() = Cat("cat377")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat378")
fun cat378() = Cat("cat378")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat379")
fun cat379() = Cat("cat379")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat380")
fun cat380() = Cat("cat380")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat381")
fun cat381() = Cat("cat381")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat382")
fun cat382() = Cat("cat382")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat383")
fun cat383() = Cat("cat383")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat384")
fun cat384() = Cat("cat384")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat385")
fun cat385() = Cat("cat385")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat386")
fun cat386() = Cat("cat386")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat387")
fun cat387() = Cat("cat387")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat388")
fun cat388() = Cat("cat388")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat389")
fun cat389() = Cat("cat389")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat390")
fun cat390() = Cat("cat390")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat391")
fun cat391() = Cat("cat391")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat392")
fun cat392() = Cat("cat392")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat393")
fun cat393() = Cat("cat393")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat394")
fun cat394() = Cat("cat394")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat395")
fun cat395() = Cat("cat395")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat396")
fun cat396() = Cat("cat396")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat397")
fun cat397() = Cat("cat397")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat398")
fun cat398() = Cat("cat398")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat399")
fun cat399() = Cat("cat399")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat400")
fun cat400() = Cat("cat400")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat401")
fun cat401() = Cat("cat401")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat402")
fun cat402() = Cat("cat402")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat403")
fun cat403() = Cat("cat403")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat404")
fun cat404() = Cat("cat404")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat405")
fun cat405() = Cat("cat405")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat406")
fun cat406() = Cat("cat406")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat407")
fun cat407() = Cat("cat407")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat408")
fun cat408() = Cat("cat408")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat409")
fun cat409() = Cat("cat409")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat410")
fun cat410() = Cat("cat410")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat411")
fun cat411() = Cat("cat411")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat412")
fun cat412() = Cat("cat412")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat413")
fun cat413() = Cat("cat413")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat414")
fun cat414() = Cat("cat414")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat415")
fun cat415() = Cat("cat415")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat416")
fun cat416() = Cat("cat416")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat417")
fun cat417() = Cat("cat417")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat418")
fun cat418() = Cat("cat418")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat419")
fun cat419() = Cat("cat419")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat420")
fun cat420() = Cat("cat420")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat421")
fun cat421() = Cat("cat421")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat422")
fun cat422() = Cat("cat422")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat423")
fun cat423() = Cat("cat423")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat424")
fun cat424() = Cat("cat424")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat425")
fun cat425() = Cat("cat425")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat426")
fun cat426() = Cat("cat426")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat427")
fun cat427() = Cat("cat427")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat428")
fun cat428() = Cat("cat428")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat429")
fun cat429() = Cat("cat429")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat430")
fun cat430() = Cat("cat430")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat431")
fun cat431() = Cat("cat431")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat432")
fun cat432() = Cat("cat432")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat433")
fun cat433() = Cat("cat433")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat434")
fun cat434() = Cat("cat434")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat435")
fun cat435() = Cat("cat435")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat436")
fun cat436() = Cat("cat436")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat437")
fun cat437() = Cat("cat437")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat438")
fun cat438() = Cat("cat438")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat439")
fun cat439() = Cat("cat439")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat440")
fun cat440() = Cat("cat440")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat441")
fun cat441() = Cat("cat441")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat442")
fun cat442() = Cat("cat442")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat443")
fun cat443() = Cat("cat443")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat444")
fun cat444() = Cat("cat444")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat445")
fun cat445() = Cat("cat445")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat446")
fun cat446() = Cat("cat446")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat447")
fun cat447() = Cat("cat447")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat448")
fun cat448() = Cat("cat448")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat449")
fun cat449() = Cat("cat449")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat450")
fun cat450() = Cat("cat450")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat451")
fun cat451() = Cat("cat451")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat452")
fun cat452() = Cat("cat452")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat453")
fun cat453() = Cat("cat453")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat454")
fun cat454() = Cat("cat454")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat455")
fun cat455() = Cat("cat455")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat456")
fun cat456() = Cat("cat456")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat457")
fun cat457() = Cat("cat457")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat458")
fun cat458() = Cat("cat458")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat459")
fun cat459() = Cat("cat459")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat460")
fun cat460() = Cat("cat460")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat461")
fun cat461() = Cat("cat461")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat462")
fun cat462() = Cat("cat462")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat463")
fun cat463() = Cat("cat463")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat464")
fun cat464() = Cat("cat464")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat465")
fun cat465() = Cat("cat465")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat466")
fun cat466() = Cat("cat466")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat467")
fun cat467() = Cat("cat467")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat468")
fun cat468() = Cat("cat468")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat469")
fun cat469() = Cat("cat469")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat470")
fun cat470() = Cat("cat470")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat471")
fun cat471() = Cat("cat471")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat472")
fun cat472() = Cat("cat472")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat473")
fun cat473() = Cat("cat473")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat474")
fun cat474() = Cat("cat474")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat475")
fun cat475() = Cat("cat475")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat476")
fun cat476() = Cat("cat476")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat477")
fun cat477() = Cat("cat477")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat478")
fun cat478() = Cat("cat478")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat479")
fun cat479() = Cat("cat479")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat480")
fun cat480() = Cat("cat480")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat481")
fun cat481() = Cat("cat481")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat482")
fun cat482() = Cat("cat482")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat483")
fun cat483() = Cat("cat483")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat484")
fun cat484() = Cat("cat484")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat485")
fun cat485() = Cat("cat485")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat486")
fun cat486() = Cat("cat486")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat487")
fun cat487() = Cat("cat487")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat488")
fun cat488() = Cat("cat488")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat489")
fun cat489() = Cat("cat489")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat490")
fun cat490() = Cat("cat490")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat491")
fun cat491() = Cat("cat491")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat492")
fun cat492() = Cat("cat492")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat493")
fun cat493() = Cat("cat493")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat494")
fun cat494() = Cat("cat494")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat495")
fun cat495() = Cat("cat495")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat496")
fun cat496() = Cat("cat496")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat497")
fun cat497() = Cat("cat497")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat498")
fun cat498() = Cat("cat498")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat499")
fun cat499() = Cat("cat499")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat500")
fun cat500() = Cat("cat500")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat501")
fun cat501() = Cat("cat501")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat502")
fun cat502() = Cat("cat502")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat503")
fun cat503() = Cat("cat503")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat504")
fun cat504() = Cat("cat504")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat505")
fun cat505() = Cat("cat505")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat506")
fun cat506() = Cat("cat506")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat507")
fun cat507() = Cat("cat507")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat508")
fun cat508() = Cat("cat508")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat509")
fun cat509() = Cat("cat509")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat510")
fun cat510() = Cat("cat510")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat511")
fun cat511() = Cat("cat511")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat512")
fun cat512() = Cat("cat512")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat513")
fun cat513() = Cat("cat513")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat514")
fun cat514() = Cat("cat514")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat515")
fun cat515() = Cat("cat515")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat516")
fun cat516() = Cat("cat516")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat517")
fun cat517() = Cat("cat517")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat518")
fun cat518() = Cat("cat518")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat519")
fun cat519() = Cat("cat519")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat520")
fun cat520() = Cat("cat520")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat521")
fun cat521() = Cat("cat521")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat522")
fun cat522() = Cat("cat522")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat523")
fun cat523() = Cat("cat523")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat524")
fun cat524() = Cat("cat524")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat525")
fun cat525() = Cat("cat525")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat526")
fun cat526() = Cat("cat526")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat527")
fun cat527() = Cat("cat527")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat528")
fun cat528() = Cat("cat528")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat529")
fun cat529() = Cat("cat529")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat530")
fun cat530() = Cat("cat530")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat531")
fun cat531() = Cat("cat531")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat532")
fun cat532() = Cat("cat532")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat533")
fun cat533() = Cat("cat533")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat534")
fun cat534() = Cat("cat534")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat535")
fun cat535() = Cat("cat535")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat536")
fun cat536() = Cat("cat536")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat537")
fun cat537() = Cat("cat537")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat538")
fun cat538() = Cat("cat538")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat539")
fun cat539() = Cat("cat539")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat540")
fun cat540() = Cat("cat540")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat541")
fun cat541() = Cat("cat541")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat542")
fun cat542() = Cat("cat542")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat543")
fun cat543() = Cat("cat543")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat544")
fun cat544() = Cat("cat544")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat545")
fun cat545() = Cat("cat545")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat546")
fun cat546() = Cat("cat546")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat547")
fun cat547() = Cat("cat547")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat548")
fun cat548() = Cat("cat548")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat549")
fun cat549() = Cat("cat549")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat550")
fun cat550() = Cat("cat550")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat551")
fun cat551() = Cat("cat551")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat552")
fun cat552() = Cat("cat552")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat553")
fun cat553() = Cat("cat553")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat554")
fun cat554() = Cat("cat554")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat555")
fun cat555() = Cat("cat555")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat556")
fun cat556() = Cat("cat556")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat557")
fun cat557() = Cat("cat557")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat558")
fun cat558() = Cat("cat558")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat559")
fun cat559() = Cat("cat559")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat560")
fun cat560() = Cat("cat560")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat561")
fun cat561() = Cat("cat561")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat562")
fun cat562() = Cat("cat562")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat563")
fun cat563() = Cat("cat563")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat564")
fun cat564() = Cat("cat564")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat565")
fun cat565() = Cat("cat565")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat566")
fun cat566() = Cat("cat566")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat567")
fun cat567() = Cat("cat567")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat568")
fun cat568() = Cat("cat568")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat569")
fun cat569() = Cat("cat569")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat570")
fun cat570() = Cat("cat570")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat571")
fun cat571() = Cat("cat571")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat572")
fun cat572() = Cat("cat572")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat573")
fun cat573() = Cat("cat573")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat574")
fun cat574() = Cat("cat574")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat575")
fun cat575() = Cat("cat575")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat576")
fun cat576() = Cat("cat576")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat577")
fun cat577() = Cat("cat577")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat578")
fun cat578() = Cat("cat578")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat579")
fun cat579() = Cat("cat579")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat580")
fun cat580() = Cat("cat580")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat581")
fun cat581() = Cat("cat581")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat582")
fun cat582() = Cat("cat582")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat583")
fun cat583() = Cat("cat583")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat584")
fun cat584() = Cat("cat584")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat585")
fun cat585() = Cat("cat585")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat586")
fun cat586() = Cat("cat586")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat587")
fun cat587() = Cat("cat587")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat588")
fun cat588() = Cat("cat588")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat589")
fun cat589() = Cat("cat589")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat590")
fun cat590() = Cat("cat590")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat591")
fun cat591() = Cat("cat591")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat592")
fun cat592() = Cat("cat592")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat593")
fun cat593() = Cat("cat593")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat594")
fun cat594() = Cat("cat594")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat595")
fun cat595() = Cat("cat595")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat596")
fun cat596() = Cat("cat596")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat597")
fun cat597() = Cat("cat597")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat598")
fun cat598() = Cat("cat598")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("cat599")
fun cat599() = Cat("cat599")
