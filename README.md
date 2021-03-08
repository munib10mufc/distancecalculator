# distancecalculator
 Repository to calculate data from file, calculate distance between two points and invite customers within configurable distance range.
 I've catered producer consumer scenario to solve this issue. A producer thread reads the file and keeps on putting data into blockingQueue data structures so that if consumer is unable to process records, then producer also stops reading further data from file to save computer resources.  
 BlockingQueue size is set to 10 so that at max 10 records will be there in waiting state.  
 As soon as fileReader thread reads any record, consumer thread starts working on it so reading and processing goes in parallal to make this application time effecient.  
    
Steps to use this code are:  
1- Please clone this repository.  
2- You'll need to place the input files in the folder where jar is being executed from.  
3- To run jar file write ***java -jar distancecalculator-v1.jar***.  
4- It will as to enter file name to read and process. Sample input file is [here](https://s3.amazonaws.com/intercom-take-home-test/customers.txt)  
5- ***output.txt*** will be created in same folder where jar file is placed and it contains the names of customers to be invited.  
6- You'll need maven to run test cases by writing.  
***mvn test*** in root directory i.e ***distancecalculator***. You'll see something like this in prompt.  

![image](https://user-images.githubusercontent.com/34034084/110297149-f7cc6180-8014-11eb-86ff-89ccd286297a.png)
