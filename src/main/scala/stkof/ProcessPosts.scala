package stkof


import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import com.datastax.spark.connector._
import scala.collection.JavaConversions._


object ProcessPosts {
 
   // (year, month, body)
   case class Post(year: String, month: String, body: String)


   // Parse the line to get the attribute values
   def parsePost(sc: SparkContext, postFile: RDD[String]): RDD[(String,
       String, String, String, String, String, Int, Int, String, String, String, String, String, Int, Int)] = {

      sc.parallelize(postFile.collect()).map {  line =>  {

         var id: String = null
         var post_type_id: String = null
         var parent_id: String =  "" 
         var create_date: String = null
         var score: Int = 0
         var view_count: Int  = 0
         var body: String = null
         var owner_user_id: String = null
         var last_activity_date: String = null
         var title: String = null
         var tags: String = null
         var answer_count: Int = 0
         var comment_count: Int = 0


         val (f1, f2) = line.split("\" ")
                            .map( field => {
                               val fds = field.split("=\"")
                               if (fds.length > 1) {
                                  (fds(0), fds(1)) 
                               } 
                               else {
                                  ("", "")  // this is for last field "/>"
                               }
                            })
                            .unzip
 
         for ( i <- 0 until f1.length) {
            f1(i).trim() match {
               case "<row Id" =>  id = f2(i)
               case "PostTypeId" => post_type_id = f2(i)
               case "ParentId" => parent_id = f2(i)
               case "CreationDate" => create_date = f2(i)
               case "Score" => score = f2(i).toInt
               case "ViewCount" => view_count = f2(i).toInt
               case "Body" => body = f2(i)
               case "OwnerUserId" => owner_user_id = f2(i)
               case "LastActivityDate" => last_activity_date = f2(i)
               case "Title" => title = f2(i)
               case "Tags" => tags = f2(i)
               case "AnswerCount" => answer_count = f2(i).toInt
               case "CommentCount" => comment_count = f2(i).toInt
               case _ => ""     // Ignore other cases
             }
         }


         (id, post_type_id, parent_id, create_date, create_date.substring(0,4),
          create_date.substring(5,7), score, view_count, body, owner_user_id, 
          last_activity_date, title, tags, answer_count, comment_count) 

         }
      } 
   }



   // Parse the line to get three attribute values
   def extractAttributesFromPost(sc: SparkContext, postFile: RDD[String]): RDD[Post] = {

      sc.parallelize(postFile.collect()).map {  line =>  {

         var create_date: String = null
         var body: String = null

         val (f1, f2) = line.split("\" ")
                            .map( field => {
                               val fds = field.split("=\"")
                               if (fds.length > 1) {
                                  (fds(0), fds(1))
                               }
                               else {
                                  ("", "")  // this is for last field "/>"
                               }
                            })
                            .unzip

         for ( i <- 0 until f1.length) {
            f1(i).trim() match {
               case "CreationDate" => create_date = f2(i)
               case "Body" => body = f2(i)
               case _ => ""  // Ingore other cases
            }
         }


         Post(create_date.substring(0,4).toString ,     // Create year
              create_date.substring(5,7).toString ,     // Create month
              body.toString )                           // Body
       }}
   }


   // Main function 
   def main(args: Array[String]): Unit = {

      val conf = new SparkConf().setAppName("process-posts")
                     .set("spark.cassandra.connection.host", "127.0.0.1")

      val sc = new SparkContext(conf)
      val distFile: RDD[String] = sc.textFile("/home/hduser/spark-code/analyse-posts/data/load/*.xml")
 
     
      // Write row data to cassandra table
      parsePost(sc, distFile)
            .saveToCassandra("sof_posts_data", "sof_post", 
                 SomeColumns("id", "post_type_id", "parent_id", "create_date", 
                 "create_year", "create_month", "score", "view_count", "body", 
                 "owner_user_id", "last_activity_date", "title", "tags",
                 "answer_count", "comment_count"))


      // Write data to aggregation table     
      extractAttributesFromPost(sc, distFile)
      // Testing  .filter(_.body.toLowerCase().indexOf("declare") >= 0)
            .filter(_.body.toLowerCase().indexOf("apache storm") >= 0)
            .map( post  => ((post.year, post.month),1) )
            .reduceByKey(_ + _)
            .map{ case (_1, _2) => (_1._1, _1._2, _2 ) }
            .saveToCassandra("sof_posts_data", "monthly_aggregate_astorm_post",
                 SomeColumns("create_year", "create_month", "post_count"))   
          

      sc.stop

   }
}

