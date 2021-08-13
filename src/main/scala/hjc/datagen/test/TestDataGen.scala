package hjc.datagen.test

import hjc.datagen._
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

// 测试类
object TestDataGen {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 增大并行度,生成的 随机数据 速度就会非常快
    env.setParallelism(1)
    // 字段 & 规则 (字段中可以嵌套多层的Map或者Array,生产环境中对应的也是多层的JSON嵌套)
    val conf = MapFieldGen(
      Map(
        "Name" -> StringFieldGen(pattern = "Name"),
        "IP" -> StringFieldGen(pattern = "Ip"),
        "Email" -> StringFieldGen(pattern = "Email", isPrimaryKey = Primary.YES), // 主键标识
        "Tel" -> StringFieldGen(pattern = "Tel"),
        "Password" -> StringFieldGen(8),
        "Boolean" -> BooleanFieldGen(),
        "Byte" -> ByteFieldGen(),
        "Short" -> ShortFieldGen(lower = 10, upper = 20),
        "Int" -> IntFieldGen(lower = 20, upper = 30),
        "Long" -> LongFieldGen(lower = 30, upper = 40),
        "Float" -> FloatFieldGen(lower = 40, upper = 50),
        "Double" -> DoubleFieldGen(lower = 50, upper = 60),
        "Date" -> DateFieldGen(startDate = "2021-05-24", endDate = "2021-06-14", format = "date"),
        "Array" -> ArrayFieldGen(IntFieldGen(distribution = Option(PascalDistribution(1500, 0.25))), rule = GenRule.RANDOM_SIZE),
        // Map的嵌套
        "Map" -> MapFieldGen(
          Map(
            "Name" -> StringFieldGen(pattern = "name"),
            "Age" -> ByteFieldGen(1, 120),
            "Map" -> MapFieldGen(
              Map(
                "Gender" -> BooleanFieldGen(),
                "PID" -> StringFieldGen(8, isPrimaryKey = Primary.YES)
              ),
              rule = GenRule.FULL
            )
          ),
          rule = GenRule.RANDOM_SIZE
        ),
        "Enum(Sample)" -> EnumFieldGen((1 to 999999).toList, order = OrderRule.CIRCLE),
        "泊松分布" -> LongFieldGen(distribution = Option(PoissonDistribution(128))),
        "帕斯卡分布" -> IntFieldGen(distribution = Option(PascalDistribution(10000, 0.5))),
        "伯努利分布" -> IntFieldGen(distribution = Option(BinomialDistribution(10000, 0.5))),
        "威布尔分布" -> DoubleFieldGen(distribution = Option(WeibullDistribution(2, 16))),
        "高斯分布" -> DoubleFieldGen(distribution = Option(GaussianDistribution(4, 64))),
        "柯西分布" -> DoubleFieldGen(distribution = Option(CauchyDistribution(16, 64))),
        "指数分布" -> DoubleFieldGen(distribution = Option(ExponentialDistribution(512))),
        "伽马分布" -> DoubleFieldGen(distribution = Option(GammaDistribution(16, 48))),
        "贝塔分布" -> DoubleFieldGen(distribution = Option(BetaDistribution(4, 64))),
        "卡方分布" -> DoubleFieldGen(distribution = Option(ChiSquareDistribution(32)))
      ),
      rule = GenRule.RANDOM_SIZE, // 随机长度
      //rule = GenRule.FULL, // 饱满长度
      //min = 5
    )
    // 构建流
    val input: DataStream[Map[String, Any]] = DataGenConnector(DataGenSource(conf, 100000)).createStream(env)

    input.print("==") // 控制台打印

    //GenDataStore.saveFileSink[RichMap](input, "file:///C:/Users/lenovo/Desktop")  // 输出到文件
    env.execute(this.getClass.getSimpleName)
  }
}