import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.TableCell
import tornadofx.*
import javafx.scene.control.TableView

/* View Class */

class BookList : View("Book List")
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

                onDoubleClick {
                    val selectedBook = this.selectedItem!!
                    System.out.println("Opening {" + selectedBook.title + "} in new window...")
                    find<BookView>(mapOf(
                        "selection" to selectedBook,
                        "preload" to preload
                    )).apply {

                    }.openModal()
                }

                runAsync {
                    Scraper.getEReader(preload)
                } ui {
                    this.items = it.observable()
                }
            }
        }
}

class BookView : Fragment()
{
    val selection : Book by param()
    val preload : Boolean by param()

    lateinit var bookText : String

    override val root =  scrollpane {
        label {
            this.text = "Book View"

            runAsync {
                if(preload) {
                    bookText = selection.details!!.text
                }
                else {
                    bookText = Scraper.getText(selection.URL)!!.text
                }
            } ui {
                this.text = bookText
            }
        }
        
    }

}
