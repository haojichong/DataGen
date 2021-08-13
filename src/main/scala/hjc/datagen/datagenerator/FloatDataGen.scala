package hjc.datagen.datagenerator

import org.apache.commons.math3.random.RandomDataGenerator
import hjc.datagen._

case class FloatDataGen(override val conf: FloatFieldGen) extends FieldDataGenerator[FloatFieldGen, Float] {
  private lazy val rndGenerator: RandomDataGenerator = new RandomDataGenerator()
  private lazy val upper = if (conf.upper < conf.lower) Float.MaxValue else conf.upper

  // 约束小数位
  override def next(): Float = rndGenerator.nextUniform(conf.lower, upper, true).formatted("%.3f").toFloat
}
