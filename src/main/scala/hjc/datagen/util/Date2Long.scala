package hjc.datagen.util

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId, ZoneOffset}
import java.util.Date

import org.apache.log4j.Logger

object Date2Long extends   {
  val ymd: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  val ymd_hms: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  def toYMDhms(date: Date): String = {
    val localDateTime: LocalDateTime = LocalDateTime.ofInstant(date.toInstant, ZoneId.systemDefault)
    ymd_hms.format(localDateTime)
  }

  def toYMD(date: Date): String = {
    val localDateTime: LocalDateTime = LocalDateTime.ofInstant(date.toInstant, ZoneId.systemDefault)
    ymd.format(localDateTime)
  }

  def toTs(YmDHms: String): Long = {
    try {
      val datetime: Array[String] = YmDHms.split(" ")
      if (datetime.length != 1) {
        val localDateTime: LocalDateTime = LocalDateTime.parse(YmDHms, ymd_hms)
        val ts: Long = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli
        ts
      } else {
        val localDateTime: LocalDateTime = LocalDateTime.parse(datetime(0) + " 00:00:00", ymd_hms)
        val ts: Long = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli
        ts
      }
    } catch {
      case e: Exception => {
        Logger.getLogger("Date2Long").error(s"${YmDHms} => 日期格式错误,转换失败,将使用默认日期(2021-01-01 00:00:00)")
        1609430400000L
      }
    }
  }
}
