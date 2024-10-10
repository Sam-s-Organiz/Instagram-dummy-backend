# Instagram-Dummy-backend



###### *If after running the project , if it dosen't create Database automatically then try this* 

*     start with Database conection*:

1. run this command in yorr terminal :```mysql -u root -p```
2. then create Database  using this :```CREATE DATABASE DummyInsta```


* ##### For Kafka Producer try runing this command : ```docker pull apache/kafka:3.8.0@sha256:c9aea96a4813e77e703541b1d8f7d58c9ee05b77353da33684db55c840548791```
* ```docker inspect kafka-server | grep "IPAddress"``` 
* capture the  Ip address and replace this in application Properties ```spring.kafka.bootstrap-servers='172.17.0.2:9092'```