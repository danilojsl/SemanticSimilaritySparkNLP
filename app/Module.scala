import com.google.inject.AbstractModule
import service.{PipelinesService, PipelinesServiceImpl}

class Module extends AbstractModule {

  override def configure() = {

    println("******** Binding PipelinesService")
    bind(classOf[PipelinesService])
      .to(classOf[PipelinesServiceImpl])
      .asEagerSingleton()

  }

}