package org.larik.three.infra.batch.reader.partitioner;

import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class TransactionPartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        HashMap<String, ExecutionContext> executionContextHashMap = new HashMap<>();

        String[] formats = {"CSV", "JSON", "XML"};

        for (int i = 0; i < formats.length; i++) {
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.putString("format", formats[i]);
            executionContextHashMap.put("partition-" + formats[i], executionContext);
        }

        return  executionContextHashMap;
    }
}
