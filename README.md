# SPRING BATCH SAMPLE PROJECT

## 프로젝트
> SpringBatch

## Tech Stack
> - Java
> - Spring Boot
> - Spring Batch
> - Gradle
> - MyBatis
> - MySQL
> - PostgresSQL

## Installation & Execution
Clone this repository and navigate to the project directory. Run the following command to execute a specific job:

```bash
java -jar jarname --spring.batch.job.names=jobname
```

For example, to execute the `selectInsertBatchJob` or `selectDeleteBatchJob` job, run the following command:

```bash
java -jar SpringBatch-0.0.1-SNAPSHOT.jar --spring.batch.job.names=selectInsertBatchJob;
java -jar SpringBatch-0.0.1-SNAPSHOT.jar --spring.batch.job.names=selectDeleteBatchJob;