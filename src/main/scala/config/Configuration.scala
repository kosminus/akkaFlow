package config

import com.typesafe.config.{Config, ConfigFactory}

object Configuration {

  val inputConfig: Config = ConfigFactory.load().getConfig("input").resolve()
  val outputConfig: Config = ConfigFactory.load().getConfig("output").resolve()

}
