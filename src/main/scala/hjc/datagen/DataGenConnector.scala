package hjc.datagen

import java.util.concurrent.TimeUnit

import hjc.datagen.datagenerator.{FieldDataGenerator, RichMapGen}
import hjc.datagen.util.ClassUtils
import org.apache.flink.api.common.ExecutionConfig.ClosureCleanerLevel
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.api.common.typeinfo.TypeHint
import org.apache.flink.connector.file.sink.FileSink
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy
import org.apache.flink.streaming.api.functions.source.datagen.DataGeneratorSource

object FieldDataGeneratorHelper {
  private lazy val clazz: Map[Class[_], Class[_ <: FieldDataGenerator[FieldGen, _]]] = ClassUtils.subClass(classOf[FieldDataGenerator[FieldGen, _]])

  // 获取 不同类型的数据生成器
  def generator(fg: FieldGen): FieldDataGenerator[FieldGen, _] = {
    val cls: Class[_ <: FieldGen] = fg.getClass
    clazz(cls).getConstructor(cls).newInstance(fg)
  }
}

object GenDataStore {
  // 写到本地文件中
  def saveFileSink[T](input: DataStream[T], savePath: String, rolloverInterval: Int = 300, inactivityInterval: Int = 10, maxFileSize: Long = 134217000, charsetName: String = "UTF-8") = {
    input
      .sinkTo(
        FileSink
          .forRowFormat(
            new Path(savePath),
            new SimpleStringEncoder[T](charsetName)
          )
          .withRollingPolicy(
            DefaultRollingPolicy
              .builder()
              .withRolloverInterval(TimeUnit.SECONDS.toMillis(rolloverInterval))
              .withInactivityInterval(TimeUnit.SECONDS.toMillis(inactivityInterval))
              .withMaxPartSize(maxFileSize)
              .build()
          )
          .build()
      )
      .name("DataGen")
  }
}

case class DataGenConnector(datagensource: DataGenSource) {
  // 实例化 FieldDataGenerator的子类
  val clazz: Map[Class[_], Class[_ <: FieldDataGenerator[_, _]]] = ClassUtils.subClass(classOf[FieldDataGenerator[_, _]])

  def createStream(env: StreamExecutionEnvironment): DataStream[Map[String, Any]] = {
    // 设置闭包级别 防止闭包检测导致OOM
    env.getConfig.setClosureCleanerLevel(ClosureCleanerLevel.TOP_LEVEL) // Clean only the top-level class without recursing into fields.
    val genSource: DataGenSource = datagensource
    env.addSource(
      new DataGeneratorSource[Map[String, Any]](
        RichMapGen(genSource.dataFieldGen), // 字段名:生成规则
        genSource.rowsSecond, // 每秒生成的行数,默认是1000
        null)
    )
      .returns(new TypeHint[Map[String, Any]] {}) // 声明类型
  }
}

