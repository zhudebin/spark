/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.apache.spark.sql.execution.datasources.parquet.test.avro;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public interface CompatibilityTest {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"CompatibilityTest\",\"namespace\":\"org.apache.spark.sql.parquet.test.avro\",\"types\":[{\"type\":\"record\",\"name\":\"Nested\",\"fields\":[{\"name\":\"nested_ints_column\",\"type\":{\"type\":\"array\",\"items\":\"int\"}},{\"name\":\"nested_string_column\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]},{\"type\":\"record\",\"name\":\"ParquetAvroCompat\",\"fields\":[{\"name\":\"bool_column\",\"type\":\"boolean\"},{\"name\":\"int_column\",\"type\":\"int\"},{\"name\":\"long_column\",\"type\":\"long\"},{\"name\":\"float_column\",\"type\":\"float\"},{\"name\":\"double_column\",\"type\":\"double\"},{\"name\":\"binary_column\",\"type\":\"bytes\"},{\"name\":\"string_column\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"maybe_bool_column\",\"type\":[\"null\",\"boolean\"]},{\"name\":\"maybe_int_column\",\"type\":[\"null\",\"int\"]},{\"name\":\"maybe_long_column\",\"type\":[\"null\",\"long\"]},{\"name\":\"maybe_float_column\",\"type\":[\"null\",\"float\"]},{\"name\":\"maybe_double_column\",\"type\":[\"null\",\"double\"]},{\"name\":\"maybe_binary_column\",\"type\":[\"null\",\"bytes\"]},{\"name\":\"maybe_string_column\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}]},{\"name\":\"strings_column\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}},{\"name\":\"string_to_int_column\",\"type\":{\"type\":\"map\",\"values\":\"int\",\"avro.java.string\":\"String\"}},{\"name\":\"complex_column\",\"type\":{\"type\":\"map\",\"values\":{\"type\":\"array\",\"items\":\"Nested\"},\"avro.java.string\":\"String\"}}]}],\"messages\":{}}");

  @SuppressWarnings("all")
  public interface Callback extends CompatibilityTest {
    public static final org.apache.avro.Protocol PROTOCOL = org.apache.spark.sql.execution.datasources.parquet.test.avro.CompatibilityTest.PROTOCOL;
  }
}