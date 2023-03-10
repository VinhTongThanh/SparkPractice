package spark.transformation

import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object CommandMap extends App {
  val spark = SparkSession.builder()
    .appName("...")
    .master("local")
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  /*
  map on rdd
   */
//  Create a list
  val data = Seq("Project",
    "Gutenberg’s",
    "Alice’s",
    "Adventures",
    "in",
    "Wonderland",
    "Project",
    "Gutenberg’s",
    "Adventures",
    "in",
    "Wonderland",
    "Project",
    "Gutenberg’s")
//  Create RDD from above list
  val rdd=spark.sparkContext.parallelize(data)
//  rdd map
  val rdd2 = rdd.map(f => (f, 1))
  rdd2.foreach(println)

  println("\n_____________________________________________________________________________________\n")
  /*
  map on dataframe
   */
  val structureData = Seq(
    Row("James", "", "Smith", "36636", "NewYork", 3100),
    Row("Michael", "Rose", "", "40288", "California", 4300),
    Row("Robert", "", "Williams", "42114", "Florida", 1400),
    Row("Maria", "Anne", "Jones", "39192", "Florida", 5500),
    Row("Jen", "Mary", "Brown", "34561", "NewYork", 3000)
  )

  val structureSchema = new StructType()
    .add("firstname", StringType)
    .add("middlename", StringType)
    .add("lastname", StringType)
    .add("id", StringType)
    .add("location", StringType)
    .add("salary", IntegerType)

  val df2 = spark.createDataFrame(
    spark.sparkContext.parallelize(structureData), structureSchema)
  df2.printSchema()
  df2.show(false)

  import spark.implicits._

  val df3 = df2.map(row => {
    val fullName = row.getString(0) + row.getString(1) + row.getString(2)
    (fullName, row.getString(3), row.getInt(5))
  })
  val df3Map = df3.toDF("fullName", "id", "salary")

  df3Map.printSchema()
  df3Map.show(false)
}
