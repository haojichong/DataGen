package hjc.datagen.datagenerator

import org.apache.commons.math3.random.RandomDataGenerator
import hjc.datagen._

case class ShortDataGen(override val conf: ShortFieldGen) extends FieldDataGenerator[ShortFieldGen, Short] {
  private lazy val rndGenerator: RandomDataGenerator = new RandomDataGenerator()
  private lazy val upper = if (conf.lower > conf.upper) Short.MaxValue else conf.upper

  override def next(): Short = rndGenerator.nextSecureInt(conf.lower,upper).toShort
}