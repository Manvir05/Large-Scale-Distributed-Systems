Manvir Kaur u199321

Laia Tomàs u198723

Eric Berenguer U199894

**LAB1 LSDS README**

**EXECUTION COMMAND**

java -cp target/lab1-1.0-SNAPSHOT-fat.jar edu.upf.TwitterFilter es outputfile lsds2024.lab1.output.u199321 Eurovision3.json Eurovision4.json Eurovision5.json  Eurovision6.json  Eurovision7.json  Eurovision8.json  Eurovision9.json  Eurovision10.json

The “es” word will be for Spanish language, “en” for English and “ca” for Catalan. “EurovisionX.json” is the file with the tweets.

**CREDENTIALS**

For the credentials, we have used the “default” one.


**BENCHMARKING**

We were able to accomplish this lab successfully. The results of the number of tweets for each language are:

||Simplified tweet|Eurovision3|Eurovision4|Eurovision5|Eurovision6|Eurovision7|Eurovision8|Eurovision9|Eurovision10|
| - | - | - | - | - | - | - | - | - | - |
|Spanish|1|23848|78433|45800|71677|71677|54969|26224|169659|
|English|0|24346|96430|50545|66596|39794|35569|18048|115275|
|Catalan|0|242|983|681|717|398|404|193|1065|

The following table is for the execution time (in ms) for each file and language, respectively:

||Simplified tweet|Eurovision3|Eurovision4|Eurovision5|Eurovision6|Eurovision7|Eurovision8|Eurovision9|Eurovision10|
| - | - | - | - | - | - | - | - | - | - |
|Spanish|34|6161|16020|16022|20069|9506|7436|6064|37224|
|English|12|7853|19553|11148|16972|11602|9340|7259|43551|
|Catalan|1|6063|15356|8874|13612|9372|7556|5946|35034|

We can clearly see that there is a big significance between the number of tweets in Catalan and the tweets in English or Spanish.





**EXTENSIONS**

- Unit testing for simplifiedTweet

To ensure the reliability and robustness of our JSON parsing logic in the  simplifiedTweet method, we introduced a comprehensive suite of unit tests. These tests were designed to cover a range of scenarios that the parser might encounter in real-world usage, including:

- Parsing a Real Tweet: Validates the method0s ability to correctly parse a well-formed JSON string representing an actual tweet. This test uses a real tweet example to ensure that essential fields are accurately extracted ans assigned.

- Parsing Invalud JSON: Assesses the parser’s robustness by feeding it a malformed JSON string. This test ensures that the parser can handle invalid inputs gracefully without causing unexpected failures.

- Parsing valid JSON with a missing Field: Tests the parser’s handling of JSON strings that are structurally correct but lack required fields. It checks the method’s ability to recognize incomplete data and respond appropriately. 

These tests collectively ensure that our parsing logic is both accurate in processing valid inputs and resilient against malformed or incomplete data. Implementing these tests has significantly contributed to the reliability of our application, ensuring that it can handle a wide variety of real-world data scenarios.

