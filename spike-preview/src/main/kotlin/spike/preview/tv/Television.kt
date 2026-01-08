package spike.preview.tv

interface Television {
    var playing: Boolean
    var channel: Int
    var sound: Int
    val currentEpg: String
}