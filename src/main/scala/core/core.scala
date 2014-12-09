//package core
//
//import akka.actor.{ Props, ActorRefFactory, ActorSystem }
//
//
//trait Core {
//  protected implicit def system: ActorSystem
//}
//
//
//trait BootedCore extends Core {
//  def system: ActorSystem = ActorSystem("scale-geocode")
//  def actorRefFactory: ActorRefFactory = system
//  val rootService = system.actorOf(Props(new RoutedHttpService(routes ~ staticRoutes)))
//  
//  IO(Http)(system) ! Http.Bind(rootService, "0.0.0.0", port = 8080)
//
//  sys.addShutdownHook(system.shutdown())
//
//}
//
//trait CoreActors {
//  this: Core =>
//
//  val interpolator = system.actorOf(Props[AddressInterpolator])
//  val addressSearch = system.actorOf(AddressSearch.props("192.168.59.103", 9200))
//}
