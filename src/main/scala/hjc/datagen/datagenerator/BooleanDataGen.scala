package hjc.datagen.datagenerator
import hjc.datagen._

import org.apache.commons.math3.random.RandomDataGenerator

case class BooleanDataGen(override val conf: BooleanFieldGen) extends FieldDataGenerator[BooleanFieldGen, Boolean] {
  private lazy val rndGenerator: RandomDataGenerator = new RandomDataGenerator()

  override def next(): Boolean = rndGenerator.nextInt (0, 1) == 1
}

