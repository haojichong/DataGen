package hjc.datagen.datagenerator

import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random
import hjc.datagen._

case class MapDataGen(override val conf: MapFieldGen) extends FieldDataGenerator[MapFieldGen, Map[String, Any]] {
  val length = conf.fields.size

  // 获取 字段名,以及对应的类型生成器
  val gens: Map[String, FieldDataGenerator[FieldGen, _]] = conf.fields.map(tuple => {
    tuple._1 -> FieldDataGeneratorHelper.generator(tuple._2)
  }).toMap

  // 随即长度/Full 规则生成(每一行数据中有些列可能为空,可以配置主键)
  private lazy val exec = conf.rule match {
    case GenRule.RANDOM_SIZE =>
      @tailrec
      def gen(rt: Map[String, Any], size: Int, gens: Map[String, FieldDataGenerator[FieldGen, _]]): Map[String, Any] = {
        if (gens.isEmpty) {
          rt // jump
        } else {
          val index = Random.nextInt(gens.size)
          val key = gens.keySet.toList(index)
          if (rt.contains(key)) {
            gen(rt, size, gens)
          } else {
            if (rt.size == size - 1) {
              rt + (key -> gens(key).next())
            } else {
              gen(rt + (key -> gens(key).next()), size, gens - key)
            }
          }
        }
      }

      () => {
        val max: Int = conf.min + (Random.nextDouble() * (length - conf.min)).toInt
        if (max == gens.size) {
          gens.map(tuple => tuple._1 -> tuple._2.next()) // FULL
        } else {
          val map: Map[String, Any] = getPrimaryKeyField(gens)
          gen(map, max, if (map.isEmpty) gens else gens.--(map.map(_._1)))
        }
      }
    case GenRule.FULL => () => gens.map(tuple => tuple._1 -> tuple._2.next())
  }

  // 获取主键,标识为主键的字段不会参与随机算法来生成数据,而是先生成主键字段后才会进行其他字段的数据生成
  def getPrimaryKeyField(gens: Map[String, FieldDataGenerator[FieldGen, _]]) = {
    val map = mutable.Map[String, Any]()
    gens.map(tuple => if (tuple._2.conf.isPrimaryKey == Primary.YES) map.+=(tuple._1 -> tuple._2.next()))
    map.toMap
  }

  // 结果
  override def next(): Map[String, Any] = exec()
}
