package com.example.ezImage;

import java.io.File;
import java.net.MalformedURLException;

import javax.imageio.IIOException;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.ezImage.models.ProcessedImage;
import com.example.ezImage.processors.ImageProcessor;
import com.example.ezImage.processors.ImageReader;
import com.example.ezImage.processors.ImageWriter;

@Configuration
@EnableBatchProcessing
public class ImageProcessingBatchConfiguration {

	private static final String INPUT_IMAGES_TXT = "input.images.txt";
	private static final String OUTPUT_DIR_PATH = "outputImages";
	private static final int CHUNK_SIZE = 5;
	private static final int EXCEPTION_SKIP_LIMIT = 100; // will stop after 100 Malformed URL or download timeout/error
	
	@Bean
    public ItemReader<ProcessedImage> reader() {
    	return new ImageReader(INPUT_IMAGES_TXT);
    }

    @Bean
    public ItemProcessor<ProcessedImage, ProcessedImage> processor() {
        return new ImageProcessor();
    }

    @Bean
    public ItemWriter<ProcessedImage> writer(DataSource dataSource) {
    	// create output directory
    	new File(OUTPUT_DIR_PATH).mkdir();
    	return new ImageWriter(dataSource, OUTPUT_DIR_PATH);
    }

    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1, JobExecutionListener listener) {
        return jobs.get("processImages")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<ProcessedImage> reader,
            ItemWriter<ProcessedImage> writer, ItemProcessor<ProcessedImage, ProcessedImage> processor) {
        return stepBuilderFactory.get("step1")
                .<ProcessedImage, ProcessedImage> chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(MalformedURLException.class)
                .skip(IIOException.class)
                .skipLimit(EXCEPTION_SKIP_LIMIT)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
