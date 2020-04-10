import org.jsoup.Jsoup
import org.jsoup.nodes.Element

fun main(args : Array<String>)
{
    val skBooks = "https://en.uesp.net/wiki/Skyrim:Books"

    val html = Jsoup.connect(skBooks).get().run {
        var cells = ArrayList<Element>()
        select("table.wikitable.sortable.collapsible.striped").select("tbody").first().children()
            .forEachIndexed { index, element ->
                if(element.tagName().compareTo("tr") == 0)
                    {
                        cells.add(element)
                    }
        }
        System.out.println("Hello world")

    }

}