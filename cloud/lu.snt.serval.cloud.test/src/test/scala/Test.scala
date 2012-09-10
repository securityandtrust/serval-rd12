import lu.snt.serval.cloud.CloudFactory

class Test {

  @Test
  def test {

    val c = CloudFactory.createCloud
    c.getCustomers.foreach(customer =>
      print(customer.getName)
    )

    c.getPmManager match {
      case Some(manager) => {
        manager.getPhysicalMachines
      }
      case None => print("No manager defined")
    }

  }
}