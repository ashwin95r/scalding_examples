package scalding.examples

import scala.util.matching.Regex
import com.twitter.scalding._

class WordCountJob(args : Args) extends Job(args) {

  //val extractor = """(http|https)://(|[^.]+\.)([^.]+\.[^/]+)/|.+""".r
  //val extractor = """(http|https)://([^.]+\.[^/]|)([^.]+\.[^/]+)/|.+""".r
  val extractor = """(http|https)://([^.]+[w|1]\.|)([^.]+\.[^/]+)/|.+""".r


     TextLine( args("input") )
    .map('line -> 'word) {line : String => {extractor.findAllIn(line).matchData.toList(0).group(3)}}
    .groupBy('word) { gp: GroupBuilder => gp.size }
    .filter('size){size: Int => size > 4}
    .write(Tsv(args("output")))



  // split a piece of text into individual words
  /*def tokenize(text : String) : Array[String] = {
    text.toLowerCase.split(" ")

  }
  */
}

object WordCountJob extends App {
  val progargs: Array[String] = List(
    "-Dmapred.map.tasks=200",
    "scalding.examples.WordCountJob",
    "--input", "/home/ashwin/ashwin/indix/assignments/scalding_examples/src/main/resources/sampleurls.txt",
    "--output", "/home/ashwin/ashwin/indix/assignments/scalding_examples/src/main/resources/output",
    "--hdfs"
  ).toArray
  Tool.main(progargs)
}