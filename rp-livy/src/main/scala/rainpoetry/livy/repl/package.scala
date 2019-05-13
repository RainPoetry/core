package rainpoetry.livy

import org.json4s.JField

/*
 * User: chenchong
 * Date: 2019/5/8
 * description:
 */

package object repl {
  type MimeTypeMap = List[JField]

  val APPLICATION_JSON = "application/json"
  val APPLICATION_LIVY_TABLE_JSON = "application/vnd.livy.table.v1+json"
  val IMAGE_PNG = "image/png"
  val TEXT_PLAIN = "text/plain"
}
