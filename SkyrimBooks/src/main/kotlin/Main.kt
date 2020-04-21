import javafx.scene.Parent
import javafx.scene.text.FontWeight
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import tornadofx.*
import java.lang.StringBuilder

/* Constants */
private val NEW_LINE = "\n"

/* GLOBAL Endpoints */
private val BASE_URL = "https://en.uesp.net"
private val ALL_BOOKS = "/wiki/Skyrim:Books"
private val FILE_PRE = "/wiki/File:"

/*
    TODO decompose main, local storage, onClick for cell, styling
 */


class Book(author: String, capt: String, title: String, val URL: String, var details: Content?) {
    var author by property<String>(author)
    fun authorProperty() = getProperty(Book::author)

    var capt by property(capt)
    fun captProperty() = getProperty(Book::capt)

    var title by property(title)
    fun titleProperty() = getProperty(Book::title)
}

/* data class Book(val author : String, val capt : String, val title : String, val URL : String, var details : Content?) */
data class Content(val text: String, val img: String)

fun main(args: Array<String>) {
    launch<EReaderApp>(args)
    System.out.println("hello world")
}

fun getEReader(preload : Boolean): ArrayList<Book> {
    /* Callback to print books after Async scrape call */
    var cb = fun(books: ArrayList<Book>) {
        /* Print Out All Book Names */
        System.out.println("********************  " + books.size + " of Skyrim's Written Works ********************")
        books.forEachIndexed { index, book ->
            var line = String.format("%1\$-40s %2$10d", book.title, index)
            System.out.printf("%1\$-45s" + " - " + line + "\n", book.author)
        }
    }
    lateinit var books : ArrayList<Book>
    /* Added preload for toggle load on child view if false... */
    if(preload) {
        var books = scrape_books(cb).also {
            getAllTexts(it)
        }
    }
    else {
        var books = scrape_books(cb)
    }

    return books

}

private fun getAllTexts(books: ArrayList<Book>) {
    for (book in books) {
        book.details = getText(book.URL)
    }
}

private fun getImg(url: String) {
    /* TODO use contents to retrieve and display img in tornadoFX */
}

private fun getText(url: String): Content? {
    var cells = ArrayList<String>()
    var contents: Content?
    var text = StringBuilder()
    var img = ""
    Jsoup.connect(BASE_URL + url).get().run {
        select("div.book").run {
            if(this.first() != null) {
                this.first().children().forEachIndexed { index, element ->
                    if (element.tagName().compareTo("p") == 0 || element.tagName().compareTo("dl") == 0) {
                        if (index == 0) {
                            cells.add(
                                element.select("img").attr("title").toString()
                                        + element.text()
                            )
                        } else {
                            cells.add(element.text())
                        }
                    }
                }

            }
        }
        select("#mw-content-text > table > tbody > tr:nth-child(1) > th > div:nth-child(1) > a > img")
            .forEachIndexed { index, element ->
                img = element.attr("alt").toString()
            }
    }.also {
        /* construct content class from text cells */
        for (line in cells) {
            text.append(line)
            text.append(NEW_LINE)
        }

        if (text.toString().isBlank() || img.isBlank()) {
            return null
        }

        return Content(text.toString(), img)
    }
}

private fun scrape_books(cb: (ArrayList<Book>) -> Unit): ArrayList<Book> {
    var books = ArrayList<Book>()
    val html = Jsoup.connect(BASE_URL + ALL_BOOKS).get().run {
        var cells = ArrayList<Element>()
        /* filter for html table cells we want */
        select("table.wikitable.sortable.collapsible.striped > tbody").select("tbody").first().children()
            .forEachIndexed { index, element ->
                if (element.tagName().compareTo("tr") == 0) {
                    cells.add(element)
                }
            }
        /* Build our book list */
        cells.removeAt(0)
        for (cell in cells) {
            val eles = cell.children()
            val capt = eles[4].text()
            val title = eles[1].select("i").select("b").text()
            val url = eles[1].select("a").attr("href")
            val author = eles[3].select("a").text()
            books.add(Book(author, capt, title, url, null))
        }
    }.also {
        cb(books)
        return books
    }
}