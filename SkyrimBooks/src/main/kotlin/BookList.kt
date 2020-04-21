import javafx.scene.control.Alert
import tornadofx.*
import javafx.scene.control.ListView
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView

/* View Class */

class BookList() : View("Book List")
{
    init {
        var alert = Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Preload?"
            headerText = "Preload all book contents?"
            contentText = "If you confirm, the program will preload all book contents."
        }

        alert.showAndWait().also {
            if (it.isPresent) {
                if(it.get().buttonData.isCancelButton)
                {
                    placeHolder = "Not Preloading..."
                    preload = false
                }
                else
                {
                    placeHolder = "Preloading may take awhile.\nDo not despair!"
                    preload = true
                }
            }

        }
    }

    var preload : Boolean
    var placeHolder : String

    override val root = getView()


    private fun getView() : TableView<Book>
    {
            return tableview (ArrayList<Book>().observable()) {
                column("Author", Book::author)
                column("Title", Book::title)
                column("Details", Book::capt)
                placeholder = label(placeHolder)

                runAsync {
                    getEReader(preload)
                } ui {
                    this.items = it.observable()
                }
            }
        }

}
