(ns lein-aws.datapipeline
  (:require [clojure.pprint :refer [pprint]]
            [lein-aws.core :refer [parse-flow]]
            [amazonica.core :refer :all]
            [amazonica.aws.datapipeline :as aws]))

(defn get-definition
  [project [pipeline-id]]
  (let [config (:aws project)]
    (with-credential [(:access-key config) (:secret-key config)]
      (pprint (aws/get-pipeline-definition {:pipeline-id pipeline-id})))))

(defn activate-pipeline
  [project [pipeline-id]]
  (let [config (:aws project)]
    (with-credential [(:access-key config) (:secret-key config)]
      (aws/activate-pipeline {:pipeline-id pipeline-id}))))

(defn list-pipelines
  [project]
  (let [config (:aws project)]
    (with-credential [(:access-key config) (:secret-key config)]
      (println (aws/list-pipelines)))))

(defn create-pipeline
  "Create a new data pipeline with `name` and `unique-id`."
  [project [name unique-id]]
  (let [config (:aws project)]
    (with-credential [(:access-key config) (:secret-key config)]
      (aws/create-pipeline {:name name :unique-id unique-id}))))

(defn put-pipeline
  "Build a new data pipeline."
  [project [pipeline-name path]]
  (let [config (:aws project)
        pipeline-definitions (parse-flow path)
        pipeline (get pipeline-definitions (keyword pipeline-name))]
    (with-credential [(:access-key config) (:secret-key config)]
      (aws/put-pipeline-definition pipeline))))