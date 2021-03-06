(ns lein-aws.elasticmapreduce
  (:require [lein-aws.core :refer [parse-flow]]
            [amazonica.core :refer :all]
            [amazonica.aws.elasticmapreduce :refer :all]))

(defn run-flow
  "Launch the elastic mapreduce jobflow `name` specified in the file at `path`"
  [project [flow-name path]]
  (let [flow-name (keyword flow-name)
        config (:aws project)
        job-flows (parse-flow path)
        flow (get job-flows flow-name)]
    (with-credential [(:access-key config) (:secret-key config)]
      (when-let [id (:job-flow-id (run-job-flow flow))]
        (println (format "job-flow %s starting..." id))
        (loop [status (describe-job-flows :job-flow-ids [id])]
          (case (-> status :job-flows first :execution-status-detail :state)
            "STARTING" (do
                         (Thread/sleep 10000)
                         (recur (describe-job-flows :job-flow-ids [id])))
            "BOOTSTRAPPING" (do
                              (println (format "job-flow %s bootstrapping..." id))
                              (Thread/sleep 10000)
                              (recur (describe-job-flows :job-flow-ids [id])))
            "RUNNING" (println (format "job-flow %s running. master public dns: %s"
                                       id
                                       (-> status :job-flows first :instances :master-public-dns-name)))
            "TERMINATED" (println (format "job-flow %s terminated" id))
            "SHUTTING_DOWN" (println (format "job-flow %s is shutting down" id))
            "FAILED" (println (format "job-flow %s failed" id))))))))

(defn terminate-flow
  "Terminate the elastic mapreduce jobflows `ids`"
  [project ids]
  (let [config (:aws project)]
    (with-credential [(:access-key config) (:secret-key config)]
      (terminate-job-flows :job-flow-ids (vec ids)))
    (println "job flow(s) terminated")))
