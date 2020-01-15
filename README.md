# tsvLogReader
Reads, parses, and summarizes a tsv log.

# Dependencies
* Java 8

# Input
* tab-separated file with three columns, in the following order:
* ISO 8601 Timestamp
* User ID
* Response code

# Output
* Output is printed to console.
* Total number of requests in the log file.
* Total number of errors in the log file (error is qualified as anything other than a 200).
* The total number of days represented in the file.
* The total number of unique users represented in the file.
* A summarized daily metrics, which details the number of unique users for the range of days represented in the log file.

# How to set up
* Clone the repo
* Compile with `javac logReader.java`
