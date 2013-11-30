package com.lucidchart.piezo

import org.quartz.utils.PoolingConnectionProvider
import java.util.Properties
import org.slf4j.LoggerFactory
import java.io._

class ConnectionProvider(props: Properties) {
  val logger = LoggerFactory.getLogger(this.getClass)
  private val dataSource = props.getProperty("org.quartz.jobStore.dataSource")
  private val provider = if(dataSource != null) {
    Some(new PoolingConnectionProvider(
      props.getProperty("org.quartz.dataSource." + dataSource + ".driver"),
      props.getProperty("org.quartz.dataSource." + dataSource + ".URL"),
      props.getProperty("org.quartz.dataSource." + dataSource + ".user"),
      props.getProperty("org.quartz.dataSource." + dataSource + ".password"),
      props.getProperty("org.quartz.dataSource." + dataSource + ".maxConnections").toInt,
      props.getProperty("org.quartz.dataSource." + dataSource + ".validationQuery")
    ))
  } else {
    logger.info("No job store found in config")
    None
  }

  def getConnection = provider.get.getConnection
}
