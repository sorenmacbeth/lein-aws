(ns lein-aws.datapipeline
  (:require [lein-aws.core :refer [parse-flow]]
            [amazonica.core :refer :all]
            [amazonica.aws.datapipeline :refer :all]))

(defn build-pipeline
  "Build a new data pipeline."
  [project [pipeline-name path]]
  (let [config (:aws project)
        pipeline-definitions (parse-flow path)
        pipeline (get pipeline-definitions (keyword pipeline-name))]
    (with-credential [(:access-key config) (:secret-key config)]
      (put-pipeline-definition pipeline))))