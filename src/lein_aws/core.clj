(ns lein-aws.core)

(defn parse-flow
  [flow-definition-file]
  (load-string (slurp flow-definition-file)))
