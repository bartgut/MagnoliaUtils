package ru.chocholo.magnolia

import cats.Functor
import cats.implicits._
import magnolia1._

import scala.language.experimental.macros

trait StringModifier[A] {
  def map(current: A, mappingFunc: String => String): A
}

object StringModifier {

  implicit val modifierString: StringModifier[String] = (cv, mappingFunc) => mappingFunc(cv)
  implicit val modifierInt: StringModifier[Int] = (curr, _) => curr
  implicit def wrappedValue[F[_]: Functor, A: StringModifier]: StringModifier[F[A]] =
    (curr, newValue) => curr.map(p => implicitly[StringModifier[A]].map(p, newValue))

  type Typeclass[T] = StringModifier[T]

  def join[T](caseClass: CaseClass[Typeclass, T]): Typeclass[T] = (current, newValue) => {
    val modifiedParams = caseClass.parameters.map { param =>
      param.typeclass.map(param.dereference(current), newValue)
    }
    caseClass.rawConstruct(modifiedParams)
  }

  implicit def gen[T]: Typeclass[T] = macro Magnolia.gen[T]
}

case class AdditionalInfoToModify(
  one: Option[Either[Throwable, String]],
  two: Option[Int]
)

case class UserToModify(
  one: Int,
  two: String,
  three: String,
  four: Option[String],
  five: Either[Throwable, String],
  additionalInfo: AdditionalInfoToModify
)

object StringModifierMain {

  def modify[A](newValue: String => String)(cc: A)(implicit modifier: StringModifier[A]): A = modifier.map(cc, newValue)
  def xModifier[A](cc: A)(implicit modifier: StringModifier[A]): A = modify(_ => "X")(cc)

  def main(args: Array[String]): Unit = {
    println(xModifier(UserToModify(1, "a", "b", Some("c"), Right("d"), AdditionalInfoToModify(Some(Right("e")), None))))
  }
}
