/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql

import org.apache.spark.{SparkContext, SparkFunSuite}

/**
 * Test cases for the builder pattern of [[SparkSession]].
 */
class SparkSessionBuilderSuite extends SparkFunSuite {

  private var initialSession: SparkSession = _

  private lazy val sparkContext: SparkContext = {
    initialSession = SparkSession.builder()
      .master("local")
      .config("spark.ui.enabled", value = false)
      .config("some-config", "v2")
      .getOrCreate()
    initialSession.sparkContext
  }

  test("create with config options and propagate them to SparkContext and SparkSession") {
    // Creating a new session with config - this works by just calling the lazy val
    sparkContext
    assert(initialSession.sparkContext.conf.get("some-config") == "v2")
    assert(initialSession.conf.get("some-config") == "v2")
    SparkSession.clearDefaultSession()
  }

  test("use global default session") {
    val session = SparkSession.builder().getOrCreate()
    assert(SparkSession.builder().getOrCreate() == session)
    SparkSession.clearDefaultSession()
  }

  test("config options are propagated to existing SparkSession") {
    val session1 = SparkSession.builder().config("spark-config1", "a").getOrCreate()
    assert(session1.conf.get("spark-config1") == "a")
    val session2 = SparkSession.builder().config("spark-config1", "b").getOrCreate()
    assert(session1 == session2)
    assert(session1.conf.get("spark-config1") == "b")
    SparkSession.clearDefaultSession()
  }

  test("use session from active thread session and propagate config options") {
    val defaultSession = SparkSession.builder().getOrCreate()
    val activeSession = defaultSession.newSession()
    SparkSession.setActiveSession(activeSession)
    val session = SparkSession.builder().config("spark-config2", "a").getOrCreate()

    assert(activeSession != defaultSession)
    assert(session == activeSession)
    assert(session.conf.get("spark-config2") == "a")
    SparkSession.clearActiveSession()

    assert(SparkSession.builder().getOrCreate() == defaultSession)
    SparkSession.clearDefaultSession()
  }

  test("create a new session if the default session has been stopped") {
    val defaultSession = SparkSession.builder().getOrCreate()
    SparkSession.setDefaultSession(defaultSession)
    defaultSession.stop()
    val newSession = SparkSession.builder().master("local").getOrCreate()
    assert(newSession != defaultSession)
    newSession.stop()
  }

  test("create a new session if the active thread session has been stopped") {
    val activeSession = SparkSession.builder().master("local").getOrCreate()
    SparkSession.setActiveSession(activeSession)
    activeSession.stop()
    val newSession = SparkSession.builder().master("local").getOrCreate()
    assert(newSession != activeSession)
    newSession.stop()
  }
}
