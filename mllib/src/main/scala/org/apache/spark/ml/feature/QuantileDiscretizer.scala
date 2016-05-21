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

package org.apache.spark.ml.feature

import org.apache.spark.annotation.{Experimental, Since}
import org.apache.spark.internal.Logging
import org.apache.spark.ml._
import org.apache.spark.ml.attribute.NominalAttribute
import org.apache.spark.ml.param._
import org.apache.spark.ml.param.shared.{HasInputCol, HasOutputCol, HasSeed}
import org.apache.spark.ml.util._
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.types.{DoubleType, StructType}

/**
 * Params for [[QuantileDiscretizer]].
 */
private[feature] trait QuantileDiscretizerBase extends Params
  with HasInputCol with HasOutputCol with HasSeed {

  /**
   * Number of buckets (quantiles, or categories) into which data points are grouped. Must
   * be >= 2.
   * default: 2
   * @group param
   */
  val numBuckets = new IntParam(this, "numBuckets", "Maximum number of buckets (quantiles, or " +
    "categories) into which data points are grouped. Must be >= 2.",
    ParamValidators.gtEq(2))
  setDefault(numBuckets -> 2)

  /** @group getParam */
  def getNumBuckets: Int = getOrDefault(numBuckets)

  /**
   * Relative error (see documentation for
   * [[org.apache.spark.sql.DataFrameStatFunctions.approxQuantile approxQuantile]] for description)
   * Must be a number in [0, 1].
   * default: 0.001
   * @group param
   */
  val relativeError = new DoubleParam(this, "relativeError", "The relative target precision " +
    "for approxQuantile",
    ParamValidators.inRange(0.0, 1.0))
  setDefault(relativeError -> 0.001)

  /** @group getParam */
  def getRelativeError: Double = getOrDefault(relativeError)
}

/**
 * :: Experimental ::
 * `QuantileDiscretizer` takes a column with continuous features and outputs a column with binned
 * categorical features. The bin ranges are chosen by taking a sample of the data and dividing it
 * into roughly equal parts. The lower and upper bin bounds will be -Infinity and +Infinity,
 * covering all real values.
 */
@Experimental
final class QuantileDiscretizer(override val uid: String)
  extends Estimator[Bucketizer] with QuantileDiscretizerBase with DefaultParamsWritable {

  def this() = this(Identifiable.randomUID("quantileDiscretizer"))

  /** @group setParam */
  def setRelativeError(value: Double): this.type = set(relativeError, value)

  /** @group setParam */
  def setNumBuckets(value: Int): this.type = set(numBuckets, value)

  /** @group setParam */
  def setInputCol(value: String): this.type = set(inputCol, value)

  /** @group setParam */
  def setOutputCol(value: String): this.type = set(outputCol, value)

  /** @group setParam */
  def setSeed(value: Long): this.type = set(seed, value)

  override def transformSchema(schema: StructType): StructType = {
    SchemaUtils.checkColumnType(schema, $(inputCol), DoubleType)
    val inputFields = schema.fields
    require(inputFields.forall(_.name != $(outputCol)),
      s"Output column ${$(outputCol)} already exists.")
    val attr = NominalAttribute.defaultAttr.withName($(outputCol))
    val outputFields = inputFields :+ attr.toStructField()
    StructType(outputFields)
  }

  @Since("2.0.0")
  override def fit(dataset: Dataset[_]): Bucketizer = {
    val splits = dataset.stat.approxQuantile($(inputCol),
      (0.0 to 1.0 by 1.0/$(numBuckets)).toArray, $(relativeError))
    splits(0) = Double.NegativeInfinity
    splits(splits.length - 1) = Double.PositiveInfinity

    val bucketizer = new Bucketizer(uid).setSplits(splits)
    copyValues(bucketizer.setParent(this))
  }

  override def copy(extra: ParamMap): QuantileDiscretizer = defaultCopy(extra)
}

@Since("1.6.0")
object QuantileDiscretizer extends DefaultParamsReadable[QuantileDiscretizer] with Logging {

  @Since("1.6.0")
  override def load(path: String): QuantileDiscretizer = super.load(path)
}
