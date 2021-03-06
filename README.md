# spark-post-analysis
It is simple application that loads amount of posts to data storage and do some analysis using Apache Spark Streaming and Cassandra. 

###The steps to run the project
#####1. Break down the big XML file into small files to fit in the Spark engine
`time scala stkof.BreakDownFile $HOME/spark-post-analysis/data/Posts1m-tail.xml $HOME/spark-post-analysis/data/load/`
  ```
  du -sh load/
  cd load
  ls | wc -l
```
#####2. Start Apache Spark
 ```
cd /usr/local/spark/sbin/
./start-master.sh
./start-slaves.sh
jps
```
#####3. Start Cassandra
`sudo /usr/local/cassandra/bin/cassandra`
#####4. Create tables: sof_posts_data.sof_data and sof_posts_data.monthly_aggregate_astorm_post
```
cd ~/spark-post-analysis/data
/usr/local/cassandra/bin/cqlsh
Connected to Test Cluster at 127.0.0.1:9042.
[cqlsh 5.0.1 | Cassandra 2.1.5 | CQL spec 3.2.0 | Native protocol v3]
Use HELP for help.
cqlsh> source 'create-timeseries.cql';
cqlsh> describe keyspace sof_posts_data;
```
#####5. Load the raw data and aggregate data to Cassandra tables
`time /usr/local/spark/bin/spark-submit --class stkof.ProcessPosts --master local[4] target/scala-2.10/ProcessPosts-assembly-0.1-SNAPSHOT.jar`
#####6. Analyze the data reading from Cassandra tables
`time /usr/local/spark/bin/spark-submit --class stkof.ExtractPosts --master local[4] target/scala-2.10/ProcessPosts-assembly-0.1-SNAPSHOT.jar`

It will create the file called **post_analysis.txt** and write the results to it.

#####7. Draw the graph based the data reading from Cassandra table
`time /usr/local/spark/bin/spark-submit --class stkof.PlotPosts --master local[4] target/scala-2.10/ProcessPosts-assembly-0.1-SNAPSHOT.jar`

