package hjc.datagen.datagenerator

import hjc.datagen._
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.runtime.state.FunctionInitializationContext
import org.apache.flink.streaming.api.functions.source.datagen.DataGenerator

// 结果输出
case class RichMapGen(conf: MapFieldGen) extends DataGenerator[Map[String, Any]] {
  private lazy val gen = MapDataGen(conf)

  override def open(name: String, context: FunctionInitializationContext, runtimeContext: RuntimeContext): Unit = {
  }

  override def hasNext: Boolean = true

  override def next(): Map[String, Any] = gen.next()
}
