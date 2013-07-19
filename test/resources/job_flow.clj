{:my-flow
  {:name "my-job-flow"
  :log-uri "s3n://emr-logs/logs"
  :instances 
    {:instance-groups [
       {:instance-type "m1.large"
        :instance-role "MASTER"
        :instance-count 1
        :market "SPOT"
        :bid-price "0.10"}]}
  :steps [
    {:name "my-step"
     :hadoop-jar-step
       {:jar "s3n://beee0534-ad04-4143-9894-8ddb0e4ebd31/hadoop-jobs/bigml"
        :main-class "bigml.core"
        :args ["s3n://beee0534-ad04-4143-9894-8ddb0e4ebd31/data" "output"]}}]}}