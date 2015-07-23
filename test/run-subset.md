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

`time /usr/local/spark/bin/spark-submit --class stkof.ProcessPosts --master local[4] target/scala-2.10/ProcessPosts-assembly-0.1-SNAPSHOT.jar`

![](https://cloud.githubusercontent.com/assets/13358534/8842423/14562f7c-30c8-11e5-8f00-a0a85c90e7ea.png)

* Check the Cassandra tables

In the 8363 files, there is only one file that contains "Apache Storm" terms. 
![](https://cloud.githubusercontent.com/assets/13358534/8842570/d04d1c6c-30c9-11e5-8d26-1aa9e68cc290.png)

Go to data/load directory to check and the result is correct. Only the file: 24750917.xml contains "Apache Storm" terms.
![](https://cloud.githubusercontent.com/assets/13358534/8842664/039af2d2-30cb-11e5-8697-61457998dc19.png)

* Before going the next step, let's mock up some data for other months by inserting some data into sof_posts_data.monthly_aggregate_astorm_post table. 
![](https://cloud.githubusercontent.com/assets/13358534/8842749/1c3fa0a2-30cc-11e5-8d80-02bc362599d5.png)

* Run ExtractPost to get the results
