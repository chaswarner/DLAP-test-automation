export HBASE_PATH=`hbase classpath`

export HADOOP_PATH=`hadoop classpath`

export CLASSPATH=$CLASSPATH:$HADOOP_PATH

export CLASSPATH=$CLASSPATH:/opt/cloudera/parcels/CDH-5.12.1-1.cdh5.12.1.p0.3/lib/hadoop/client/hadoop-auth.jar:/opt/cloudera/parcels/CDH-5.12.1-1.cdh5.12.1.p0.3/lib/hadoop/client/hadoop-common.jar:/opt/cloudera/parcels/CDH-5.12.1-1.cdh5.12.1.p0.3/lib/hive/lib/hive-jdbc-standalone.jar

export HADOOP_HOME=/opt/cloudera/parcels/CDH

java -cp .:./hbase-1.0-SNAPSHOT.jar:/opt/cloudera/parcels/CDH-5.12.1-1.cdh5.12.1.p0.3/lib/hive/lib/hive-jdbc-standalone.jar:/opt/cloudera/parcels/CDH-5.12.1-1.cdh5.12.1.p0.3/lib/hadoop/client/hadoop-common-2.6.0-cdh5.12.1.jar:/opt/cloudera/parcels/CDH-5.12.1-1.cdh5.12.1.p0.3/lib/hadoop/client-0.20/hadoop-core-2.6.0-mr1-cdh5.12.1.jar:/opt/cloudera/parcels/CDH-5.12.1-1.cdh5.12.1.p0.3/lib/hadoop/client/hadoop-auth-2.6.0-cdh5.12.1.jar:/opt/cloudera/parcels/CDH-5.12.2-1.cdh5.12.2.p0.4/lib/hbase/*:$HADOOP_PATH:$HBASE_PATH com.prft.cif.test.RunCukesTest


