(ns leiningen.aws
  (:require [lein-aws.s3 :refer [s3-put]]
            [lein-aws.elasticmapreduce :refer [run-flow terminate-flow]]
            [lein-aws.datapipeline :refer [build-pipeline]]))

(defn aws
  "Interact with AWS"
  {:help-arglists '([s3-put run-flow terminate-flow build-pipeline])
   :subtasks [#'s3-put #'run-flow #'terminate-flow #'build-pipeline]}
  [project subtask & args]
  (case subtask
    "s3-put" (s3-put project args)
    "run-flow" (run-flow project args)
    "terminate-flow" (terminate-flow project args)
    "build-pipeline" (build-pipeline project args)))
