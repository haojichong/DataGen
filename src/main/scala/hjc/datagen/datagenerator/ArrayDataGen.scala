package hjc.datagen.datagenerator

import hjc.datagen._

import scala.util.Random

case class ArrayDataGen(override val conf: ArrayFieldGen[FieldGen]) extends FieldDataGenerator[ArrayFieldGen[FieldGen], List[Any]] {
  private lazy val generator: FieldDataGenerator[_, _] = FieldDataGeneratorHelper.generator(conf.elementType)

  // 随机长度/Full长度 规则
  private lazy val exec = conf.rule match {
    case GenRule.RANDOM_SIZE =>
      () => {
        val max = (conf.min + (Random.nextDouble() * (conf.max - conf.min)).toInt)
        gen(max)
      }
    case GenRule.FULL => () => gen(conf.max)
  }

  def gen(max: Int): List[Any] = (0 until max).map(i => generator.next()).toList

  override def next(): List[Any] = exec()
}
