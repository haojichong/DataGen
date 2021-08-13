package hjc.datagen.datagenerator

import org.apache.commons.math3.random.RandomDataGenerator
import hjc.datagen._


case class DoubleDataGen(override val conf: DoubleFieldGen) extends FieldDataGenerator[DoubleFieldGen, Double] {
  private lazy val rndGenerator: RandomDataGenerator = new RandomDataGenerator()
  private lazy val upper = if (conf.upper < conf.lower) Double.MaxValue else conf.upper

  private lazy val exec = {
    conf.distribution match {
      case Some(value) =>
        value match {
          // 函数分布
          case BetaDistribution(alpha, beta) => () => rndGenerator.nextBeta(alpha, beta)
          case CauchyDistribution(scale, median) => () => rndGenerator.nextCauchy(median, scale)
          case ChiSquareDistribution(df) => () => rndGenerator.nextChiSquare(df)
          case ExponentialDistribution(mean) => () => rndGenerator.nextExponential(mean)
          case GaussianDistribution(mu, sigma) => () => rndGenerator.nextGaussian(mu, sigma)
          case GammaDistribution(shape, scale) => () => rndGenerator.nextGamma(shape, scale)
          case WeibullDistribution(shape, scale) => () => rndGenerator.nextWeibull(shape, scale)
          case _ => () => rndGenerator.nextUniform(conf.lower, upper)
        }
      case None => () => rndGenerator.nextUniform(conf.lower, upper)
    }
  }

  override def next(): Double = exec().formatted("%.3f").toDouble
}

