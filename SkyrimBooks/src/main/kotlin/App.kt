import javafx.scene.text.FontWeight
import tornadofx.*

/* Hello World sample for TornadoFX

class HelloWorld : View()
{
    override val root = hbox {
        label("hello world")
    }
}
*/

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