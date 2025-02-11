package com.example;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class WordReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private TreeMap<Integer, List<String>> countMap = new TreeMap<>(Collections.reverseOrder()); // Sort Descending

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }

        // Store multiple words with the same count
        countMap.putIfAbsent(sum, new ArrayList<>());
        countMap.get(sum).add(key.toString());
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for (Map.Entry<Integer, List<String>> entry : countMap.entrySet()) {
            int count = entry.getKey();
            for (String word : entry.getValue()) {
                context.write(new Text(word), new IntWritable(count));
            }
        }
    }
}
