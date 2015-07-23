#####Here is the test of 8363 files, subset of one million files. 

* The total 8363 files are under data/load/ directory and the size is 36M.
```
hduser@ubuntu:~/spark-code/analyse-posts/data$ du -sh load/
36M	load/
hduser@ubuntu:~/spark-code/analyse-posts/data$ ls load/* | wc -l
8363
```
![](https://cloud.githubusercontent.com/assets/13358534/8842331/bc6ff7a8-30c6-11e5-9ed9-ecf6dbc60aa4.png)

* Run ProcessPost to load the data to Cassandra tables:
`time /usr/local/spark/bin/spark-submit --class stkof.ProcessPosts --master local[4] target/scala-2.10/ProcessPosts-assembly-0.1-SNAPSHOT.jar
`
![](https://cloud.githubusercontent.com/assets/13358534/8842423/14562f7c-30c8-11e5-8f00-a0a85c90e7ea.png)

* Check the Cassandra tables

