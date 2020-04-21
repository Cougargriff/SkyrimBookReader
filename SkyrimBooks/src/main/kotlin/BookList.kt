import tornadofx.*
import javafx.scene.control.ListView
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView

/* View Class */

class BookList() : View("Book List")
{
    override val root = getView()

    private fun getView() : TableView<Book>
    {
        getEReader().also {
            return tableview (ArrayList<Book>().observable()) {
//                runAsync {
//                    getEReader()
//                } ui {
//                    this.items = it.observable()
//                }
            }
        }
    }
}
