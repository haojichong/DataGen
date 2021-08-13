package hjc.datagen.datagenerator

import org.apache.commons.math3.random.RandomDataGenerator
import hjc.datagen._

case class ByteDataGen(override val conf: ByteFieldGen) extends FieldDataGenerator[ByteFieldGen, Byte] {
  private lazy val rndGenerator: RandomDataGenerator = new RandomDataGenerator()
  private lazy val upper = if (conf.upper < conf.lower) Byte.MaxValue else conf.upper

  override def next(): Byte = rndGenerator.nextSecureInt(conf.lower, upper).toByte
}
