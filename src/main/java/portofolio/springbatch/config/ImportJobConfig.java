package portofolio.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import portofolio.springbatch.entity.Majestic;
import portofolio.springbatch.repositories.MajesticRepository;

@Configuration
public class ImportJobConfig {

    @Autowired
    private MajesticRepository majesticRepository;

    @Bean
    public Job majesticJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemReader<Majestic> itemReader, ItemWriter<Majestic> itemWriter, TaskExecutor taskExecutor){
        return new JobBuilder("MajesticJob", jobRepository)
                .start(majesticStep(jobRepository, transactionManager, itemReader, itemWriter, taskExecutor))
                .build();
    }

    public Step majesticStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemReader<Majestic> itemReader, ItemWriter<Majestic> itemWriter, TaskExecutor taskExecutor){
        return new StepBuilder("MajesticImportStep", jobRepository)
                .<Majestic, Majestic>chunk(100, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Majestic> majesticReader(){
        return new FlatFileItemReaderBuilder<Majestic>()
                .resource(new ClassPathResource("majestic_million.csv"))
                .name("majestic-reader")
                .saveState(false)
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("GlobalRank", "TldRank", "Domain", "TLD", "RefSubNets", "RefIPs", "IDN_Domain", "IDN_TLD", "PrevGlobalRank", "PrevTldRank", "PrevRefSubNets", "PrevRefIPs")
                .targetType(Majestic.class)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<Majestic> majesticWriter(){
        return new RepositoryItemWriterBuilder<Majestic>()
                .methodName("save")
                .repository(majesticRepository)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring-batch-");
    }
}
