package rainpoetry.livy.repl

/*
 * User: chenchong
 * Date: 2019/5/9
 * description:
 */


sealed abstract class Kind(val name: String) {
  override def toString: String = name
}

object Spark extends Kind("spark")

object SQL extends Kind("sql")

object Kind {

  def apply(kind: String): Kind = kind match {
    case "spark" | "scala" => Spark
    case "sql" => SQL
    case other => throw new IllegalArgumentException(s"Invalid kind: $other")
  }
}
