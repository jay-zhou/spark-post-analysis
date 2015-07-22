# spark-post-analysis
It is simple application that loads amount of posts to data storage and do some analysis using Apache Spark Streaming and Cassandra. 

#The steps to run the project
###1. Break down the big XML file into small files to fit in the Spark engine
 ~/spark-post-analysis/target/scala-2.10/classes$ time scala stkof.BreakDownFile $HOME/spark-post-analysis/data/Posts1m-tail.xml $HOME/spark-post-analysis/data/load/
 
 $ du -sh load/
  1.5G	load/
  $ cd load
  $ ls | wc -l
  356454
 


