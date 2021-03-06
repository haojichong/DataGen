package hjc.datagen.util

import org.reflections.Reflections

import scala.util.Try
import scala.util.matching.Regex

// 这个类的作用就是 获取到某个类的所有子类,并进行实例化
object ClassUtils {
  class ClassUtils

  final def find[T](underlying: Class[T], name: String = "hjc.datagen"): Option[Class[_ <: T]] = {
    import scala.collection.JavaConverters._
    val reflects = new Reflections(name)
    reflects.getSubTypesOf(underlying).asScala.headOption
  }

  final def subClass[T](underlying: Class[T], params: Int = 1, name: String = "hjc.datagen", filter: Option[String] = None): Map[Class[_], Class[_ <: T]] = {
    import scala.collection.JavaConverters._
    val reflects = new Reflections(name)
    val data = reflects.getSubTypesOf(underlying).asScala.toSeq
    val classes = data.filter(t => {
      !t.isInterface && t.getConstructors.length > 0 && t.getConstructors()(0).getParameterTypes.length >= params
    }).map(t => (t.getConstructors()(0).getParameterTypes()(0), t))
    filter match {
      case Some(t) => classes.filter(_._2.getName.contains(t)).toMap
      case _ => classes.toMap
    }
  }


  object ParamType {
    final val regex: Regex = """.*\(\w+: ([^,)]*).*""".r

    def unapply(str: String): Option[Class[_]] = {
      regex.unapplySeq(str) match {
        case Some(name :: Nil) => Try(Class.forName(name)).toOption
        case _ => None
      }
    }
  }

}

