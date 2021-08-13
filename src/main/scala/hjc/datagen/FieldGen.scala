package hjc.datagen

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo.{As, Id}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.fasterxml.jackson.core.`type`.TypeReference

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "name")
@JsonSubTypes(Array(
  new Type(value = classOf[StringFieldGen], name = "string"),
  new Type(value = classOf[BooleanFieldGen], name = "boolean"),
  new Type(value = classOf[ByteFieldGen], name = "byte"),
  new Type(value = classOf[ShortFieldGen], name = "short"),
  new Type(value = classOf[IntFieldGen], name = "int"),
  new Type(value = classOf[LongFieldGen], name = "long"),
  new Type(value = classOf[FloatFieldGen], name = "float"),
  new Type(value = classOf[DoubleFieldGen], name = "double"),
  new Type(value = classOf[DateFieldGen], name = "date"),
  new Type(value = classOf[ArrayFieldGen[_]], name = "array"),
  new Type(value = classOf[MapFieldGen], name = "map"),
  new Type(value = classOf[EnumFieldGen[_]], name = "enum")
))
trait FieldGen extends Serializable {
  def name: String

  def isPrimaryKey: Primary.Type
}

case class StringFieldGen(
                           length: Int = 9,
                           pattern: String = "default",
                           override val isPrimaryKey: Primary.Type = Primary.NO
                         ) extends FieldGen {
  override def name: String = "string"
}

case class BooleanFieldGen(override val isPrimaryKey: Primary.Type = Primary.NO) extends FieldGen {
  override def name: String = "boolean"
}

case class ByteFieldGen(lower: Byte = Byte.MinValue,
                        upper: Byte = Byte.MaxValue,
                        override val isPrimaryKey: Primary.Type = Primary.NO
                       ) extends FieldGen {
  override def name: String = "byte"
}

case class ShortFieldGen(lower: Short = Short.MinValue,
                         upper: Short = Short.MaxValue,
                         override val isPrimaryKey: Primary.Type = Primary.NO
                        ) extends FieldGen {
  override def name: String = "short"
}

case class IntFieldGen(lower: Int = Int.MinValue,
                       upper: Int = Int.MaxValue,
                       distribution: Option[Distribution] = None,
                       step: Option[Int] = None,
                       override val isPrimaryKey: Primary.Type = Primary.NO
                      ) extends FieldGen {
  override def name: String = "int"
}

case class LongFieldGen(
                         lower: Long = Long.MinValue,
                         upper: Long = Long.MaxValue,
                         distribution: Option[Distribution] = None,
                         override val isPrimaryKey: Primary.Type = Primary.NO
                       ) extends FieldGen {
  override def name: String = "long"
}

case class FloatFieldGen(
                          lower: Float = Float.MinValue,
                          upper: Float = Float.MaxValue,
                          override val isPrimaryKey: Primary.Type = Primary.NO
                        ) extends FieldGen {
  override def name: String = "float"
}

case class DoubleFieldGen(lower: Double = Double.MinValue,
                          upper: Double = Double.MaxValue,
                          distribution: Option[Distribution] = None,
                          override val isPrimaryKey: Primary.Type = Primary.NO
                         ) extends FieldGen {
  override def name: String = "double"
}

case class DateFieldGen(startDate: String = "2015-01-01 00:00:00",
                        endDate: String = "2021-12-31 23:59:59",
                        format: String = "default",
                        override val isPrimaryKey: Primary.Type = Primary.NO
                       ) extends FieldGen {
  override def name: String = "date"
}

case class EnumFieldGen[T](
                            data: List[T], // 抽样列表
                            order: OrderRule.Type = OrderRule.RANDOM, //random,CIRCLE
                            override val isPrimaryKey: Primary.Type = Primary.NO
                          ) extends FieldGen {
  override def name: String = "enum"
}

case class ArrayFieldGen[T <: FieldGen](elementType: FieldGen,
                                        min: Int = 0,
                                        max: Int = 16,
                                        rule: GenRule.Type = GenRule.FULL, //RANDOM_SIZE
                                        override val isPrimaryKey: Primary.Type = Primary.NO
                                       ) extends FieldGen {
  override def name: String = "array"
}

case class MapFieldGen(fields: Map[String, FieldGen],
                       min: Int = 0,
                       rule: GenRule.Type = GenRule.FULL, //RANDOM_SIZE
                       override val isPrimaryKey: Primary.Type = Primary.NO
                      ) extends FieldGen {
  override def name: String = "map"
}

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "name")
@JsonSubTypes(Array(
  new Type(value = classOf[GammaDistribution], name = "gamma"),
  new Type(value = classOf[WeibullDistribution], name = "weibull"),
  new Type(value = classOf[CauchyDistribution], name = "cauchy"),
  new Type(value = classOf[BetaDistribution], name = "beta"),
  new Type(value = classOf[GaussianDistribution], name = "gaussian"),
  new Type(value = classOf[BinomialDistribution], name = "binomial"),
  new Type(value = classOf[ChiSquareDistribution], name = "chi-square"),
  new Type(value = classOf[PoissonDistribution], name = "poisson"),
  new Type(value = classOf[PascalDistribution], name = "pascal"),
  new Type(value = classOf[ExponentialDistribution], name = "exponential")
))
trait Distribution {
  def name: String = this.getClass.getSimpleName.toLowerCase.replace("distribution", "")
}

case class GammaDistribution(shape: Double = 4, scale: Double = 64) extends Distribution {
  override def name: String = "gamma"
}

case class WeibullDistribution(shape: Double = 4, scale: Double = 64) extends Distribution {
  override def name: String = "weibull"
}

case class CauchyDistribution(median: Double = 84, scale: Double = 24) extends Distribution {
  override def name: String = "cauchy"
}

case class BetaDistribution(alpha: Double = 4, beta: Double = 64) extends Distribution {
  override def name: String = "beta"
}

case class GaussianDistribution(mu: Double = 4, sigma: Double = 64) extends Distribution {
  override def name: String = "gaussian"
}

case class BinomialDistribution(frequency: Int = 2000, probabilityOfSuccess: Double = 0.37) extends Distribution {
  override def name: String = "binomial"
}

case class ChiSquareDistribution(df: Double = 128) extends Distribution {
  override def name: String = "chi-square"
}

case class PoissonDistribution(mean: Double = 256) extends Distribution {
  override def name: String = "poisson"
}

case class ExponentialDistribution(mean: Double = 256) extends Distribution {
  override def name: String = "exponential"
}

case class PascalDistribution(frequency: Int = 2000, probabilityOfSuccess: Double = 0.37) extends Distribution {
  override def name: String = "pascal"
}

class OrderRuleTrf extends TypeReference[OrderRule.type]

object OrderRule extends Enumeration {
  type Type = Value
  final val RANDOM = Value("random")
  final val CIRCLE = Value("circle")
}

class GenRuleTrf extends TypeReference[GenRule.type]

object GenRule extends Enumeration {
  type Type = Value
  final val RANDOM_SIZE = Value("random_size")
  final val FULL = Value("full")
}

class FlagTrf extends TypeReference[Primary.type]

/* 主键标识 */
object Primary extends Enumeration {
  type Type = Value
  final val YES = Value("yes")
  final val NO = Value("no")
}