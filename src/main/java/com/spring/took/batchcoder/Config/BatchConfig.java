package com.spring.took.batchcoder.Config;

import com.spring.took.batchcoder.Entity.User;
import com.spring.took.batchcoder.Reposistory.UserJobRepo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Autowired
    private UserJobRepo userJobRepo;

    @Bean
    public Job jobs(JobRepository jobRepository , Step step){
        return new JobBuilder("job-1",jobRepository).start(step).build();
    }

    @Bean
    public Step step(JobRepository jobRepository , PlatformTransactionManager transactionManager){
        return new StepBuilder("step-1",jobRepository)
                .<User,User>chunk(5,transactionManager)
                .reader(reader())

                .processor(processor())
                .writer(writer())
                .build();
    }
@Bean
    public FlatFileItemReader<User> reader(){
        return new FlatFileItemReaderBuilder<User>()
                .name("UserReader")
                .resource(new ClassPathResource("people-1000.csv"))
                .linesToSkip(1)
                .lineMapper(lineMapper())
                .targetType(User.class)
                .build();
    }

    public LineMapper<User> lineMapper(){
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("Index","User Id","First Name","Last Name","Gender","Email","Phone","Date of birth","Job Title");
        tokenizer.setStrict(false);


        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public ItemProcessor<User,User> processor(){
        return new UserProcessor();
    }

    @Bean
    public RepositoryItemWriter<User> writer(){
        RepositoryItemWriter<User> writer = new RepositoryItemWriter<>();
        writer.setMethodName("save");

        writer.setRepository(userJobRepo);
        return writer;
    }
}
