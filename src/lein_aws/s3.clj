(ns lein-aws.s3
  (:require [amazonica.core :refer :all]
            [amazonica.aws.s3 :refer :all]))

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