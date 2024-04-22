package com.project.DinnerMe;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CsvUtils {

    public static <T> List<T> readCsv(MultipartFile file, Class<T> clazz) throws IOException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .build();

            return csvToBean.parse();
        }
    }


        public static <T> T readrestCsv(MultipartFile file, Class<T> clazz) throws IOException {
            try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                        .withType(clazz)
                        .build();

                List<T> list = csvToBean.parse();

                return list.isEmpty() ? null : list.get(0);
            }
        }


    }