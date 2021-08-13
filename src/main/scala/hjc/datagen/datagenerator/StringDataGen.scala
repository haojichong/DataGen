package hjc.datagen.datagenerator

import org.apache.commons.math3.random.RandomDataGenerator
import hjc.datagen._
import hjc.datagen.util.RandomInfoUtil

case class StringDataGen(override val conf: StringFieldGen) extends FieldDataGenerator[StringFieldGen, String] {
  private lazy val rndGenerator: RandomDataGenerator = new RandomDataGenerator()
  private lazy val pattern: String = conf.pattern.toLowerCase

  // 配置前后缀只需要在 next方法里做拼接就可以
  override def next(): String = {
    pattern match {
      case "default" => rndGenerator.nextHexString(conf.length)
      case "email" => RandomInfoUtil.getEmail(4, 18)
      case "ip" => RandomInfoUtil.getIpAddr()
      case "name" => RandomInfoUtil.getChineseName()
      case "tel" => RandomInfoUtil.getTelephone()
      case _ => rndGenerator.nextSecureHexString(conf.length)
    }
  }
}