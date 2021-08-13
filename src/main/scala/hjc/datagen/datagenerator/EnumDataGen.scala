package hjc.datagen.datagenerator

import java.util.concurrent.atomic.AtomicInteger

import scala.util.Random
import hjc.datagen._
// 采样/抽样
case class EnumDataGen[T](override val conf: EnumFieldGen[T]) extends FieldDataGenerator[EnumFieldGen[T], T] {
  // 随机/周期性(有序)
  private lazy val exec: () => T = conf.order match {
    case OrderRule.RANDOM =>
      val len = conf.data.size
      () => {
        conf.data((Random.nextDouble() * len).toInt)
      }
    case OrderRule.CIRCLE =>
      val len = conf.data.size
      val index = new AtomicInteger(0)

      () => {
        index.compareAndSet(len, 0)
        conf.data(index.getAndIncrement())
      }
  }

  override def next(): T = exec()
}