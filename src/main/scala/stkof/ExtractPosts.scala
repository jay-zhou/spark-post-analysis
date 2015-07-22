package stkof


import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql
import org.apache.spark.sql.{DataFrame, Strategy, SQLContext}
import org.apache.spark.sql.cassandra.CassandraSQLContext
import com.datastax.spark.connector._
import scala.collection.JavaConversions._
import java.io._
//import sqlContext.implicits._


object ExtractPosts extends App {
 
   // (year, month, body)
   case class Post(year: String, month: String, body: String)


   val conf = new SparkConf().setAppName("extract-posts")
                             .set("spark.cassandra.connection.host", "127.0.0.1")
                             .setMaster("local")

   val sc = new SparkContext(conf)
//   val connector = CassandraConnector(conf)

   val cc = new CassandraSQLContext(sc)
   //val cc = new org.apache.spark.sql.SQLContext(sc)

   cc.setKeyspace("sof_posts_data")

   
   // -------------------------------------------------------------
   // 1. Get the data for July, 2014
   // -------------------------------------------------------------
   val queryPostsJuly = "SELECT id " +
                        "FROM sof_post " +
                        "WHERE create_year = '2014' " +
                        " AND  create_month = '07' "    // Testing

   val srdd = cc.sql(queryPostsJuly);
  
   val resultPostsJuly = srdd.map ( row => row.getString(0)).count()


   // -------------------------------------------------------------
   // 2. Get the total count for Apach Storm related posts
   // -------------------------------------------------------------
   val queryCountAS = "SELECT post_count " +
                      "FROM monthly_aggregate_astorm_post"

   val srdd2 =  cc.sql(queryCountAS);
   val resultCountAS = srdd2.map( row => row.getInt(0)).sum().toInt


   // -------------------------------------------------------------
   // 3. Get the most popular month of Apach Storm related posts
   // -------------------------------------------------------------
   val queryAll = "SELECT create_year, create_month, post_count " +
                      "FROM monthly_aggregate_astorm_post"

   val srdd3 =  cc.sql(queryAll);
   val monthData = srdd3.map{ row => ((row.getString(0), row.getString(1)), row.getInt(2)) }
                        .collect()
   val mostCount = monthData.maxBy(_._2)._2

   val mostMonths = monthData.filter( _._2.toInt == mostCount )
                             .map{ case ((y,m),c) => (y + "-" + m) }

 
      // -------------------------------------------------------------
   // 4. Write results to file
   // -------------------------------------------------------------
   val msg1 = "1. The total count of posts in July, 2014: " + resultPostsJuly
   val msg2 = "2. The total count of posts related to Apache Storm: " + resultCountAS
   val msg3 = "3. The most popular month is: " + mostMonths.mkString(", ") + "  with posts: " + mostCount

   val writer = new PrintWriter(new File("post_analysis.txt"))
   writer.write(msg1 + "\n" + msg2 + "\n" + msg3 + "\n")
   writer.close()


}


