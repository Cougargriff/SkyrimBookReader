import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/* GLOBAL Endpoints */
private val BASE_URL = "https://en.uesp.net"
private val ALL_BOOKS = "/wiki/Skyrim:Books"

data class Book(val author : String, val capt : String, val title : String, val URL : String)

fun main(args : Array<String>)
{
    /* Callback to print books after Async scrape call */
    var cb = fun (books : ArrayList<Book>) {
        /* Print Out All Book Names */
        System.out.println("********** " + books.size + " of Skyrim's Written Works **********")
        var i = 0
        for(book in books)
        {
            var line = String.format("%1$-40s %2$10d", book.title, i)
            System.out.println(line)
            i++
        }
    }

    scrape_books(cb)
}

private fun scrape_books(cb : (ArrayList<Book>) -> Unit) : ArrayList<Book>
{
    var books = ArrayList<Book>()
    val html = Jsoup.connect(BASE_URL + ALL_BOOKS).get().run {
        var cells = ArrayList<Element>()
        /* filter for html table cells we want */
        select("table.wikitable.sortable.collapsible.striped > tbody").select("tbody").first().children()
            .forEachIndexed { index, element ->
                if(element.tagName().compareTo("tr") == 0)
                {
                    cells.add(element)
                }
            }
        /* Build our book list */
        cells.removeAt(0)
        for(cell in cells)
        {
            val eles = cell.children()
            val capt = eles[4].text()
            val title = eles[1].select("i").select("b").text()
            val url = eles[1].select("a").attr("href")
            val author = eles[3].select("a").text()
            books.add(Book(author, capt, title, url))
        }
    }.also {
        cb(books)
        return books
    }
}