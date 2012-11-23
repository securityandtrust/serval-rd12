package hammurabi

import collection.mutable.HashMap
import util.Logger

/**
 * @author Mario Fusco
 */

class WorkingMemory(var workingSet: List[_]) extends Logger {

  def this() = this(Nil)

  val workingSetsByType = new HashMap[Class[_], List[_]]

  def all[A](clazz: Class[A]): List[A] = {
    (workingSetsByType get clazz match {
      case objects: Some[_] => objects get
      case None => {
        val t = findObjectsOfClass(clazz)
        workingSetsByType += (clazz -> t)
        t
      }
    }).asInstanceOf[List[A]]
  }

  def first[A](implicit manifest: Manifest[A]): Option[A] =
    firstOrNone(all(manifest.erasure.asInstanceOf[Class[A]]))

  def allHaving[A](clazz: Class[A])(condition: A => Boolean): List[A] = {
    all(clazz) filter condition
  }

  def firstHaving[A](condition: A => Boolean)(implicit manifest: Manifest[A]): Option[A] =
    firstOrNone(allHaving(manifest.erasure.asInstanceOf[Class[A]])(condition))

  private def firstOrNone[A](list: List[A]): Option[A] = list match {
    case x :: xs => Some(x)
    case _ => None
  }

  def +(item: Any) = {
    debug("Adding: " + item)
    addToInternalWorkingSets(item)
    this
  }

  def -(item: Any) = {
    debug("Removing: " + item)
    removeFromInternalWorkingSets(item)
    this
  }

  private def addToInternalWorkingSets(item: Any) =
    modifyInternalWorkingSets(item)((objects, item) => item :: objects)

  private def removeFromInternalWorkingSets(item: Any) =
    modifyInternalWorkingSets(item)((objects, item) => objects filter (_ != item))

  private def modifyInternalWorkingSets(item: Any)(f: (List[_], Any) => List[_]) = {
    workingSet = f(workingSet, item)
    val clazz = item.asInstanceOf[AnyRef].getClass
    workingSetsByType += (clazz ->
      (workingSetsByType get clazz match {
        case Some(objects) => f(objects, item)
        case None => findObjectsOfClass(clazz)
      })
    )
  }

  private def findObjectsOfClass[A](clazz: Class[A]) = workingSet.filter(isInstanceOf(_,clazz))

  def isInstanceOf[A](o:Any,clazz:Class[A]) : Boolean = {
   // m.erasure.isInstance(clazz)
    (o.asInstanceOf[AnyRef].getClass() == clazz) ||
    (o.asInstanceOf[AnyRef].getClass().getSuperclass == clazz) ||
      (o.asInstanceOf[AnyRef].getClass().getInterfaces.exists(it => it == clazz))
  }
}

object WorkingMemory {
  def apply() = new WorkingMemory()
  def apply(first: Any, workingSet: Any*) = new WorkingMemory(first :: workingSet.toList)
  def apply(workingSet: Traversable[_]) = new WorkingMemory(workingSet.toList)
}