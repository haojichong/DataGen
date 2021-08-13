package hjc.datagen.datagenerator

import org.apache.commons.math3.random.RandomDataGenerator
import hjc.datagen._
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.runtime.state.FunctionInitializationContext

case class IntDataGen(override val conf: IntFieldGen) extends FieldDataGenerator[IntFieldGen, Int] {
  private lazy val rndGenerator: RandomDataGenerator = new RandomDataGenerator()
  private lazy val upper = if (conf.upper > conf.lower) conf.upper else Int.MaxValue

  private lazy val exec = {
    conf.distribution match {
      case Some(value) =>
        value match {
          // 泊松分布/伯努利二项分布
          case BinomialDistribution(frequency, probabilityOfSuccess) => () => rndGenerator.nextBinomial(frequency, probabilityOfSuccess)
          case PascalDistribution(frequency, probabilityOfSuccess) => () => rndGenerator.nextPascal(frequency, probabilityOfSuccess)
          case _ => () => rndGenerator.nextInt(conf.lower, upper)
        }
      case None => () => rndGenerator.nextSecureInt(conf.lower, upper)
    }
  }

  override def next(): Int = exec()
}

// TODO -  有序INT生成器 - 代码模板
case class SequenceIntDataGenerator(override val conf: IntFieldGen) extends FieldDataGenerator[IntFieldGen, Int] {
  private lazy val lower = conf.lower // 下限
  private var upper = conf.upper // 上限
  private var step = conf.step.getOrElse(1) // 步长值
  private var sum: Int = lower // 累加器

  override def open(name: String, context: FunctionInitializationContext, runtimeContext: RuntimeContext): Unit = {
  }

  if (step < 1) step = 1 // 防止步长值 小于零
  if (lower > upper) upper = Int.MaxValue // 防止 下限高与上限

  // 是否继续生成条件,当到 上限时就停止生成了, 又或者在 next方法里进行一层判断,到上限值不再累加,而是只生成 上限值
  override def hasNext: Boolean = sum <= upper

  // override def hasNext: Boolean = true

  override def next(): Int = {
    if (sum < lower || sum >= upper) {
      sum = upper
      upper
    } else {
      sum = sum + step
      sum - step
    }
  }
}