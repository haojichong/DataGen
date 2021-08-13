package hjc.datagen.datagenerator

import java.util.Date

import hjc.datagen._
import hjc.datagen.util.Date2Long
import org.apache.commons.math3.random.RandomDataGenerator

case class DateDataGen(override val conf: DateFieldGen) extends FieldDataGenerator[DateFieldGen, Any] {
  private lazy val rndGenerator: RandomDataGenerator = new RandomDataGenerator()
  private lazy val format: String = conf.format.toLowerCase
  private lazy val startDate: String = if (format.endsWith("date")) conf.startDate.split(" ")(0) else conf.startDate
  private lazy val endDate: String = if (format.endsWith("date")) conf.endDate.split(" ")(0) else conf.endDate
  private lazy val startDT: Long = Date2Long.toTs(startDate)
  private lazy val endDT: Long = if (Date2Long.toTs(endDate) < startDT) startDT + 2592000000L else Date2Long.toTs(endDate) // startDT < endDT ? endDT=startDT + 30天

  override def next(): Any = {
    format match {
      case "default" => new Date(rndGenerator.nextLong(startDT, endDT))
      case "date" => Date2Long.toYMD(new Date(rndGenerator.nextLong(startDT, endDT)))
      case "datetime" => Date2Long.toYMDhms(new Date(rndGenerator.nextLong(startDT, endDT)))
      case _ => new Date(rndGenerator.nextLong(startDT, endDT))
    }
  }
}
