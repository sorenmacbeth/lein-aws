(ns leiningen.aws
  (:use [lein-aws.core :only (s3-put run-flow terminate-flow)]))

(defn aws
  "Interact with AWS"
  {:help-arglists '([s3-put run-flow terminate-flow])
   :subtasks [#'s3-put #'run-flow #'terminate-flow]}
  [project subtask & args]
  (case subtask
    "s3-put" (s3-put project args)
    "run-flow" (run-flow project args)
    "terminate-flow" (terminate-flow project args)))
