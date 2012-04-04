
object Test {
  def main(args: Array[String]): Unit = {
    val a = (100 until 100000).toArray
    val sum = a.map(_ * 2).zipWithIndex.map(p => p._1 * p._2).sum
    println("sum = " + sum)
  }
}
