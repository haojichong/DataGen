package hjc.datagen.test

import hjc.datagen._
import hjc.datagen.util.ClickHouseUtil
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

object TestClickHouse {
  def main(args: Array[String]): Unit = {
    // genData2Clickhouse // 数据生成
    // cube // GroupBy - cube预聚合测试
    // topN // TopN（分组TopN）测试
  }

  def cube(): Unit = {
    val sql: String =
      """
        |SELECT area_id,
        |	      toYear(dt) year,
        |	      toMonth(dt) month,
        |	      substring(spu_id,1,1) spu_id,
        |	      sum(price) payment_sum
        |from order_info_all
        |group by area_id,year,month,spu_id
        |with cube;
        |""".stripMargin
    ClickHouseUtil.queryList(sql, "jdbc:clickhouse://hjc101:8123/default").foreach(println)
  }

  // 生成测试数据到 CK
  def genData2Clickhouse = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val conf = MapFieldGen(
      Map(
        "sku_id" -> StringFieldGen(2),
        "spu_id" -> StringFieldGen(2),
        "category_id" -> StringFieldGen(2),
        "order_count" -> IntFieldGen(100, 300),
        "price" -> DoubleFieldGen(0.0, 300.0),
        "payment_amount" -> DoubleFieldGen(3.0, 280.0),
        "refund_count" -> IntFieldGen(0, 8),
        "refund_price" -> DoubleFieldGen(20.0, 260.0),
        "area_id" -> IntFieldGen(1, 34),
        // 插入的数据不能超过 100个分区(默认),即 dt字段日期的范围 <=100天
        "dt" -> DateFieldGen("2020-06-01 00:00:00", "2020-07-31 23:59:59", format = "datetime")
      ),
      rule = GenRule.FULL
    )
    val input: DataStream[Map[String,Any]] = DataGenConnector(DataGenSource(conf, 100000)).createStream(env).rescale()
    val sql: String = "insert into order_info_all values(?,?,?,?,?,?,?,?,?,?)"
    val tableName: String = "order_info_all"
    val url: String = "jdbc:clickhouse://hjc101:8123/default"
    input.addSink(ClickHouseUtil.dataStreamInsertData(sql, tableName, url))
    env.execute(this.getClass.getSimpleName)
  }

  // TopN 测试
  def topN = {
    val topN: String =
      """
        | 	SELECT  dt,
        |		        price,
        |		        rk
        |	  FROM(
        |		      SELECT  dt,
        |			            groupArray(price) AS arr_price,
        |			            arrayEnumerate(arr_price) AS rk
        |		      FROM(
        |			          SELECT DISTINCT price,
        |				                        toDate(dt) dt
        |			          FROM order_info_all
        |			          WHERE dt = '2020-01-04'
        |			          ORDER BY dt, price DESC
        |		          )
        |		      GROUP BY dt
        |	        )
        |	  ARRAY JOIN
        |		arr_price AS price,
        |		rk
        |	  ORDER BY dt ASC,rk
        |""".stripMargin
    val groupTopN: String =
      """
        | SELECT area_id,
        |		   payment_amount,
        |		   rk
        |	FROM (SELECT area_id,
        |				 payment_amount,
        |				 rk
        |		  FROM (
        |				   SELECT area_id,
        |						  groupArray(payment_amount)              arr_payment_amount,
        |						  arrayEnumerateDense(arr_payment_amount) rk
        |				   FROM (
        |							SELECT DISTINCT payment_amount,
        |											area_id
        |							FROM order_info_all
        |							ORDER BY area_id, payment_amount DESC
        |						)
        |				   GROUP BY area_id
        |			   ) ARRAY JOIN
        |			   arr_payment_amount AS payment_amount,
        |			   rk
        |		  ORDER BY area_id, rk ASC
        |		 )
        |	WHERE rk <= 3
        |	ORDER BY area_id, payment_amount DESC
        |""".stripMargin
    ClickHouseUtil.queryList(topN, "jdbc:clickhouse://hjc101:8123/default").foreach(println) // TopN
    println("-" * 100)
    ClickHouseUtil.queryList(groupTopN, "jdbc:clickhouse://hjc101:8123/default").foreach(println) // 分组TopN
  }
}

/*
   分片集群建表语句
   1.
   CREATE TABLE IF NOT EXISTS order_info on CLUSTER ck_cluster
     (
      sku_id String,
      spu_id String,
      category_id String,
      payment_amount UInt32,
      price Decimal(9,3),
      payment_amount Decimal(9,3),
      refund_count UInt32,
      refund_price Decimal(9,3),
      area_id UInt32,
      dt DateTime
     )
   ENGINE = ReplicatedMergeTree('/clickhouse/tables/{shard}/order_info', '{replica}')
   partition by toYYYYMMDD(dt)
   PRIMARY KEY sku_id
   ORDER BY(sku_id,spu_id);

   2.分布式建表语句
   CREATE TABLE IF NOT EXISTS order_info_all on CLUSTER ck_cluster
     (
      spu_id String,
      sku_id String,
      category_id String,
      payment_amount UInt32,
      price Decimal(9,3),
      payment_amount Decimal(9,3),
      refund_count UInt32,
      refund_price Decimal(9,3),
      area_id UInt32,
      dt DateTime
     )
   ENGINE = Distributed(ck_cluster, default, order_info, hiveHash(sku_id));
───────────────────────────────────────────────────────────────────────────────────
                表结构
  ┌─name───────────┬─type──────────┬
  │ spu_id         │ String        │
  │ sku_id         │ String        │
  │ category_id    │ String        │
  │ order_count    │ UInt32        │
  │ price          │ Decimal(9, 3) │
  │ payment_amount │ Decimal(9, 3) │
  │ refund_count   │ UInt32        │
  │ refund_price   │ Decimal(9, 3) │
  │ area_id        │ UInt32        │
  │ dt             │ DateTime      │
  └────────────────┴───────────────┴


───────────────────────────────────────────────────────────────────────────────────
  集群分片配置文件

  <?xml version="1.0"?>
  <yandex>
    <clickhouse_remote_servers>
      <ck_cluster> <!-- 集群名称-->
        <shard>         <!--集群的第一个分片-->
          <internal_replication>true</internal_replication>
          <replica>    <!--该分片的第一个副本-->
            <host>hjc101</host>
            <port>9000</port>
          </replica>
        </shard>
        <shard>  <!--集群的第二个分片-->
          <internal_replication>true</internal_replication>
          <replica>    <!--该分片的第一个副本-->
            <host>hjc102</host>
            <port>9000</port>
          </replica>
        </shard>
        <shard>  <!--集群的第三个分片-->
          <internal_replication>true</internal_replication>
          <replica>    <!--该分片的第一个副本-->
            <host>hjc103</host>
            <port>9000</port>
          </replica>
        </shard>
      </ck_cluster>
    </clickhouse_remote_servers>

    <zookeeper-servers>
      <node index="1">
        <host>hjc101</host>
        <port>2181</port>
      </node>
      <node index="2">
        <host>hjc102</host>
        <port>2181</port>
      </node>
      <node index="3">
        <host>hjc103</host>
        <port>2181</port>
      </node>
    </zookeeper-servers>

    <!--不同机器放的分片数不一样-->
    <!--不同机器放的副本数不一样-->
    <macros>
      <shard>01</shard>
      <replica>rep_1_1</replica>
    </macros>
    <!--<macros>
      <shard>02</shard>
      <replica>rep_2_1</replica>
    </macros>-->
    <!--<macros>
      <shard>03</shard>
      <replica>rep_3_1</replica>
    </macros>-->
  </yandex>
───────────────────────────────────────────────────────────────────────────────────
   */
