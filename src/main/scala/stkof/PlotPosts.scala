package stkof


import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql
import org.apache.spark.sql.{DataFrame, Strategy, SQLContext}
import org.apache.spark.sql.cassandra.CassandraSQLContext
import com.datastax.spark.connector._
import com.quantifind.charts.Highcharts._


object PlotPosts extends App {
 

   val conf = new SparkConf().setAppName("plot-posts")
                             .set("spark.cassandra.connection.host", "127.0.0.1")
                             .setMaster("local")

   val sc = new SparkContext(conf)

   val cc = new CassandraSQLContext(sc)

   cc.setKeyspace("sof_posts_data")

   

   // -------------------------------------------------------------
   // Get the data of monthly Apach Storm related posts
   // -------------------------------------------------------------
   val queryAll = "SELECT create_year, create_month, post_count " +
                      "FROM monthly_aggregate_astorm_post"

   val srdd =  cc.sql(queryAll);
   val monthData = srdd.map{ row => ((row.getString(0), row.getString(1)), row.getInt(2)) }
                       .collect()
                       .map{ case ((y,m),c) => ((y + "-" + m), c) }

 
   // -------------------------------------------------------------
   // Draw the graph
   // -------------------------------------------------------------
   val countColumns = column(monthData.map(_._2).toList)
   val axisType: com.quantifind.charts.highcharts.AxisType.Type = "category"
   val timeColumns = countColumns.copy(xAxis = countColumns.xAxis.map {
               axisArray => axisArray.map { _.copy(axisType = Option(axisType),
                                 categories = Option(monthData.map(_._1))) }
       })

   plot(timeColumns)

   //column(monthData)


}
  

