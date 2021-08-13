package hjc.datagen.util

import java.sql._

import org.apache.flink.connector.jdbc.{JdbcConnectionOptions, JdbcExecutionOptions, JdbcSink, JdbcStatementBuilder}
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.log4j.Logger
import ru.yandex.clickhouse.except.ClickHouseException

import scala.collection.mutable.{ListBuffer, Map}

object ClickHouseUtil extends Logger("ClickHouseUtil") {
  private var conn: Connection = null
  private var statement: Statement = null

  def queryList(sql: String, url: String) = {
    init(url)
    var rs: ResultSet = null
    val rsList: ListBuffer[Map[String, Any]] = ListBuffer[Map[String, Any]]()
    try {
      rs = statement.executeQuery(sql)
      val meta: ResultSetMetaData = rs.getMetaData
      while (rs.next()) {
        val temp: Map[String, Any] = Map[String, Any]()
        for (i <- 1 to meta.getColumnCount) {
          temp += meta.getColumnName(i) -> rs.getObject(i)
        }
        rsList += temp
      }
    } catch {
      case e: ClickHouseException => Logger.getLogger("").error("query statement error!")
      case e: Exception => Logger.getLogger("").error("query failed")
    } finally {
      if (rs != null) {
        try {
          rs.close()
        } catch {
          case e: Exception => Logger.getLogger("").error("resultSet close failed!")
        }
      }
      close()
    }
    rsList.toList
  }

  def dataStreamInsertData[T](sql: String, tableName: String, url: String): SinkFunction[T] = {
    init(url)
    import scala.collection.immutable.Map
    val columnName: List[String] = getColName(tableName)
    JdbcSink.sink[T](
      sql,
      new JdbcStatementBuilder[T] {
        override def accept(ps: PreparedStatement, obj: T): Unit = {
          for (i <- 1 to columnName.length) {
            ps.setObject(i, obj.asInstanceOf[Map[String, String]].getOrElse(columnName(i - 1), ""))
          }
        }
      },
      new JdbcExecutionOptions.Builder().withBatchIntervalMs(1000 * 5).build(),
      new JdbcConnectionOptions.JdbcConnectionOptionsBuilder().withUrl(url).build()
    )
  }

  def insertData[T](sql: String, tableName: String, url: String) = {
    init(url)
    var i: Int = 0
    try {
      i = statement.executeUpdate(sql)
    } catch {
      case e: Exception => Logger.getLogger("").error("insert data failed!")
    } finally {
      close()
    }
    i
  }

  def init(url: String): Unit = {
    if (conn == null) {
      try {
        Class.forName("ru.yandex.clickhouse.ClickHouseDriver")
        conn = DriverManager.getConnection(url)
        statement = conn.createStatement()
        Logger.getLogger("").info("connected clickhouse successFully!")
      } catch {
        case e: Exception => Logger.getLogger("").error("clickhouse connection failed!")
      }
    } else {
      Logger.getLogger("").warn("clickhouse connected!")
    }
  }

  def getColName(tableName: String) = {
    val columnName: ListBuffer[String] = ListBuffer[String]()
    try {
      val rs: ResultSet = statement.executeQuery(s"desc ${tableName}")
      while (rs.next()) {
        columnName += rs.getString(1)
      }
      rs.close()
    } catch {
      case e: Exception => Logger.getLogger("").error(s"${tableName} :: table not found!")
    }
    columnName.toList
  }

  def close() = {
    if (statement != null) {
      try {
        statement.close()
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
    if (conn != null) {
      try {
        conn.close()
        Logger.getLogger("").info("connection closed!")
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }
}
