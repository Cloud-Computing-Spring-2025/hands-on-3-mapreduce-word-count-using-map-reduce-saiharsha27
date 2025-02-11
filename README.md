# Hadoop Word Count Project

## Project Overview
This project is an implementation of the classic **Word Count** problem using **Hadoop MapReduce**. It processes a large text file, counts the occurrences of each word, and outputs the results sorted in descending order of frequency.

---

## Approach and Implementation

### Mapper Logic (WordMapper.java)
- Reads input text line by line.
- Splits each line into words using a tokenizer.
- Cleans words by removing non-alphabetic characters and converts them to lowercase.
- Emits each word with a count of **1** (`(word, 1)`).

### Reducer Logic (WordReducer.java)
- Aggregates counts for each word from the mapper.
- Uses a **TreeMap<Integer, List<String>>** to store words grouped by frequency, ensuring sorting in descending order.
- Outputs words and their counts in descending order based on frequency.

---

## Execution Steps

### 1. **Start the Hadoop Cluster**
```bash
docker compose up -d
```

### 2. **Build the Code**
Build the code using Maven:
```bash
mvn install
```

### 3. **Move JAR File to Shared Folder**
Move the generated JAR file to a shared folder for easy access:
```bash
mv target/WordCountUsingHadoop-0.0.1-SNAPSHOT.jar shared-folder/input/data/
```

### 4. **Copy JAR to Docker Container**
Copy the JAR file to the Hadoop ResourceManager container:
```bash
docker cp shared-folder/input/data/WordCountUsingHadoop-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 5. **Move Dataset to Docker Container**
Copy the dataset to the Hadoop ResourceManager container:
```bash
docker cp shared-folder/input/data/input.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 6. **Connect to Docker Container**
Access the Hadoop ResourceManager container:
```bash
docker exec -it resourcemanager /bin/bash
```

Navigate to the Hadoop directory:
```bash
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 7. **Set Up HDFS**
Create a folder in HDFS for the input dataset:
```bash
hadoop fs -mkdir -p /input/dataset
```
Copy the input dataset to the HDFS folder:
```bash
hadoop fs -put ./input.txt /input/dataset
```

### 8. **Execute the MapReduce Job**
Run your MapReduce job using the following command:
```bash
hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/WordCountUsingHadoop-0.0.1-SNAPSHOT.jar com.example.controller.Controller /input/dataset/input.txt /output
```

### 9. **View the Output**
To view the output of your MapReduce job, use:
```bash
hadoop fs -cat /output/*
```

---

## Challenges Faced & Solutions

### **1. Sorting by Word Frequency**
- **Issue:** Hadoop by default sorts keys (words) alphabetically, not by count.
- **Solution:** Used a `TreeMap<Integer, List<String>>` in the reducer, sorting by count in **descending order**.

### **2. Handling Duplicate Counts**
- **Issue:** Storing counts as keys in `TreeMap` caused overwrites.
- **Solution:** Used a `List<String>` to store multiple words having the same count.

---

## Sample Input and Output

### **Input File (input.txt)**
```
Hello world
Hello Hadoop
Hadoop is powerful
Hadoop is used for big data
```

### **Expected Output (Descending Order by Count)**
```
Hadoop 3
Hello 2
is 2
used 1
for 1
big 1
data 1
powerful 1
world 1
```

---