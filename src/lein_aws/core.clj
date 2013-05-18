(ns lein-aws.core
  (:use [amazonica.core]
        [amazonica.aws
         s3
         elasticmapreduce])
  (:require [clojure.edn :as edn]))

(defn s3-put
  "Puts the file at `path` to `bucket` with ACL `acl`."
  [project [acl bucket path]]
  (let [config (:aws project)
        f (clojure.java.io/file path)
        key (.getName f)
        acl (case acl
              "public-read" [[(:email config) "FullControl"] ["AllUsers" "Read"]]
              "private" [[(:email config) "FullControl"]]
              "public-read-write" [[(:email config) "FullControl"] ["AllUsers" "ReadWrite"]])]
    (with-credential [(:access-key config) (:secret-key config)]
      (println (format "%s -> s3://%s/%s" path bucket key))
      (put-object
       :bucket-name bucket
       :key key
       :file f
       :access-control-list
       {:grant-all acl}))))

(defn run-flow
  "Launch the elastic mapreduce jobflow `name` specified in the file at `path`"
  [project [flow-name path]]
  (let [flow-name (edn/read-string flow-name)
        config (:aws project)
        job-flows (edn/read-string (slurp path))
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
            "FAILED" (println (format "job-flow %s failed" id))))))))

(defn terminate-flow
  "Terminate the elastic mapreduce jobflows `ids`"
  [project ids]
  (let [config (:aws project)]
    (with-credential [(:access-key config) (:secret-key config)]
      (terminate-job-flows :job-flow-ids (vec ids)))
    (println "job flow(s) terminated")))
