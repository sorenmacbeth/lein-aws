(ns leiningen.aws
  (:require [lein-aws.s3 :refer [s3-put]]
            [lein-aws.elasticmapreduce :refer [run-flow terminate-flow]]
            [lein-aws.datapipeline :refer [list-pipelines
                                           activate-pipeline
                                           get-definition
                                           put-pipeline
                                           create-pipeline]]))

(defn aws
  "Interact with AWS"
  {:help-arglists '([s3-put run-flow terminate-flow list-pipelines
                     put-pipeline activate-pipeline get-definition
                     create-pipeline])
   :subtasks [#'s3-put #'run-flow #'terminate-flow #'list-pipelines
              #'put-pipeline #'activate-pipeline #'get-definition
              #'create-pipeline]}
  [project subtask & args]
  (case subtask
    "s3-put" (s3-put project args)
    "run-flow" (run-flow project args)
    "terminate-flow" (terminate-flow project args)
    "create-pipeline" (create-pipeline project args)
    "put-pipeline" (put-pipeline project args)
    "list-pipelines" (list-pipelines project)
    "activate-pipeline" (activate-pipeline project args)
    "get-definition" (get-definition project args)))
