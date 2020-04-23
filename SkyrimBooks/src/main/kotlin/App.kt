import javafx.scene.text.FontWeight
import tornadofx.*

class EReaderApp : App(BookList::class, Styles::class)

/* App Styling */
class Styles : Stylesheet() {
    init {
        Companion.label {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }
}