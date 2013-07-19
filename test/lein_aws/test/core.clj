(ns lein-aws.test.core
  (:use [clojure.test])
  (:require [lein-aws.core :as core]))

(deftest test-parse-config-files
  (let [flow (core/parse-flow "test/resources/job_flow.clj")]
    (is (= :my-flow (ffirst flow)))))