package com.bob.cloud.core;

import com.csvreader.CsvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author bobo
 * @create 2019-09-03 22:08
 */
public class CSVUtil {

    private static Logger logger = LoggerFactory.getLogger(CSVUtil.class);

    public static List<String> readCsvTitleData(CsvReader csvReader) {
        List<String> titles = new ArrayList<>();
        try{
            csvReader.readHeaders();
            titles = Arrays.asList(csvReader.getRawRecord().split(","));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return titles;
    }

    public static List<Map<String, Object>> readCsvData(CsvReader csvReader, List<String> titles) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            while (csvReader.readRecord()){
                Map<String, Object> map = new HashMap<>();
                for (String title : titles) {
                    map.put(title,csvReader.get(title));
                }
                list.add(map);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return list;
    }
}
