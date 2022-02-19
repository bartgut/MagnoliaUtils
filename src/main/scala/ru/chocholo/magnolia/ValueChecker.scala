package ru.chocholo.magnolia

import magnolia1._

import scala.language.experimental.macros

case class MaskingChecker(
  field1: String,
  field2: String,
  field3: Int
)

trait Checker[A] {
  def check(current: A): Either[String, A]
}

object Checker {
  implicit val checkString: Checker[String] = p => if (p == "X") Right(p) else Left("String Error")
  implicit val checkInteger: Checker[Int] = p => if (p == 0) Right(p) else Left("Integer Error")

  type Typeclass[T] = Checker[T]

  def join[T](caseClass: CaseClass[Typeclass, T]): Typeclass[T] = (current: T) => {
    caseClass.constructMonadic{ elem =>
      elem.typeclass.check(elem.dereference(current))
    }
  }

  implicit def gen[T]: Typeclass[T] = macro Magnolia.gen[T]
}

object CheckerMain {

  def check[A: Checker](data: A): Either[String, A] = implicitly[Checker[A]].check(data)

  def main(args: Array[String]): Unit = {
    println(check(MaskingChecker("One", "X", 0)))
  }
}
