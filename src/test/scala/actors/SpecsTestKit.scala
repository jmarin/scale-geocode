package actors

import akka.actor.ActorSystem
import org.specs2.mutable._
import org.specs2.time.NoTimeConversions
import akka.testkit.{ TestKit, ImplicitSender }

abstract class SpecsTestKit
    extends TestKit(ActorSystem("test"))
    with ImplicitSender
    with After
    with NoTimeConversions {

  def after = system.shutdown()

}

