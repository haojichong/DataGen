package hjc.datagen.datagenerator

import org.apache.commons.math3.random.RandomDataGenerator
import hjc.datagen._

case class LongDataGen(override val conf: LongFieldGen) extends FieldDataGenerator[LongFieldGen, Long] {
  private lazy val rndGenerator: RandomDataGenerator = new RandomDataGenerator()
  private lazy val upper = if (conf.upper > conf.lower) conf.upper else Long.MaxValue

  private lazy val exec: () => Long = {
    conf.distribution match {
      case Some(value) =>
        value match {
          case PoissonDistribution(mean) => () => rndGenerator.nextPoisson(mean)
          case _ => () => rndGenerator.nextSecureLong(conf.lower, upper)
        }
      case None => () => rndGenerator.nextSecureLong(conf.lower, upper)
    }
  }

  override def next(): Long = exec()
}
