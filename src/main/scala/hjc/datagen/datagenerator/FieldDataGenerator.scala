package hjc.datagen.datagenerator

import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.runtime.state.FunctionInitializationContext
import org.apache.flink.streaming.api.functions.source.datagen.DataGenerator
import hjc.datagen._

// 顶级类
trait FieldDataGenerator[C <: FieldGen, T] extends DataGenerator[T] {
  def conf: C

  override def open(name: String, context: FunctionInitializationContext, runtimeContext: RuntimeContext): Unit = {
  }

  override def hasNext: Boolean = true
}
