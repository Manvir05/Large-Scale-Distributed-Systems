Manvir Kaur u199321

Laia Tomàs u198723

Eric Berenguer u199894

**LAB1 LSDS README**

1. **Implement the Twitter filter using Spark**

**EXECUTION COMMANDS**

**TO STORE IN LOCAL**

spark-submit --master local[2] --class edu.upf.TwitterLanguageFilterApp target/spark-test-1.0-SNAPSHOT.jar XX outputXX 
inputs/Eurovision3.json
inputs/Eurovision4.json 
inputs/Eurovision5.json 
inputs/Eurovision6.json 
inputs/Eurovision7.json 
inputs/Eurovision8.json 
inputs/Eurovision9.json 
inputs/Eurovision10.json

Where XX is: es, ca, en (the language)

Inputs is our folder with all the eurovision jsons.

**TO STORE IN S3:**

**By this way we could store it from local to s3**

aws s3 sync . s3://lsds2024.lab2.output.u199321/ouputs/outputca/
aws s3 sync . s3://lsds2024.lab2.output.u199321/ouputs/outputen/
aws s3 sync . s3://lsds2024.lab2.output.u199321/ouputs/outputes/

where . is the source path:

outputXX/inputs\_EurovisionX.json

where X is 3,4,5,6,7,8,9,10 
where XX is the language (es, ca, en)


2. **Benchmark the Spark-based TwitterFilter application on EMR** 

**TO STORE IN CLUSTER:**

**The output is presnet in our bucket**

spark-submit --deploy-mode cluster --class edu.upf.TwitterLanguageFilterApp s3://lsds2024.lab2.output.u199321/jars/spark-test-1.0-SNAPSHOT.jar es s3://lsds2024.lab2.output.u199321/outputs/outputes s3://lsds2024.lab2.output.u199321/inputs/Eurovision3.json s3://lsds2024.lab2.output.u199321/inputs/Eurovision4.json s3://lsds2024.lab2.output.u199321/inputs/Eurovision5.json s3://lsds2024.lab2.output.u199321/inputs/Eurovision6.json s3://lsds2024.lab2.output.u199321/inputs/Eurovision7.json s3://lsds2024.lab2.output.u199321/inputs/Eurovision8.json s3://lsds2024.lab2.output.u199321/inputs/Eurovision9.json s3://lsds2024.lab2.output.u199321/inputs/Eurovision10.json

**BENCHMARKING OF RUNNING SPARK ON EMR**

The following table is for the execution time for all eurovision files and language:

||Eurovision tweets|
| - | - |
|Spanish|3 minutes 22 seconds|
|English|3 minutes 38 seconds|
|Catalan|3 minutes 2 seconds|



3. **Most popular bi-grams in a given language**

**EXECUTION COMMAND**

spark-submit --master local[2] --class edu.upf.spark.BiGramsApp target/spark-test-1.0-SNAPSHOT.jar XX outputbigramsXX

inputs/Eurovision3.json
inputs/Eurovision4.json 
inputs/Eurovision5.json 
inputs/Eurovision6.json 
inputs/Eurovision7.json 
inputs/Eurovision8.json 
inputs/Eurovision9.json 
inputs/Eurovision10.json

Where XX is: es, ca, en (the language)

**TOP 10 ENTRIES BIGRAMS**

Here are the results:

LANGUAGE: ES
-FIRST PARTITION

(en eurovision, 20741)
(eurovision finaleurovision, 19667)
(en el, 19284)
(de la, 16611)
(que no, 14823)

-SECOND PARTITION

(no eurovision, 11515)
(en la, 11376)
(el ao, 11337)
(de eurovision, 11256)
(nada malo, 10959)
LANGUAGE: CA

-FIRST PARTITION

(alexander rybak, 400)
(es el, 357)
(noruega eurovision, 349)
(de noruega, 347)
(eurovision httpstcob091qrmq5l, 345)

-SECOND PARTITION

(el jordi, 345)
(realmente es, 345)
(jordi hurtado, 345)
(tigrillotw 2009, 345)
(rt tigrillotw, 345)

-LANGUAGE: EN

-FIRST PARTITION

(of the, 15760)
(rt eurovision, 9746)
(rt bbceurovision, 9557)
(in eurovision, 8807)
(in the, 8473)

-SECOND PARTITION

(the uk, 8373)
(the eurovision, 7870)
(song contest, 7846)
(vote for, 7402)
(for the, 7328)


4. **Most Retweeted Tweets for Most Retweeted Users**

**TOP 10 MOST RETWEETED TWEETS FOR THE 10 MOST RETWEETED USERS**

**EXECUTION COMMAND**

spark-submit --master local[2] --class edu.upf.spark.MostRetweetedApp target/spark-test-1.0-SNAPSHOT.jar outputMostRt 

inputs/Eurovision3.json
inputs/Eurovision4.json 
inputs/Eurovision5.json 
inputs/Eurovision6.json 
inputs/Eurovision7.json 
inputs/Eurovision8.json 
inputs/Eurovision9.json 
inputs/Eurovision10.json

Here are the results:

-FIRST PARTITION

User: 15584187, Tweet: 995260727719092224, Retweets: 9864
User: 2754746065, Tweet: 995224986242646016, Retweets: 4640
User: 38381308, Tweet: 995382703649361920, Retweets: 6722
User: 437025093, Tweet: 995259101516005376, Retweets: 9678
User: 39538010, Tweet: 995379361825132544, Retweets: 6831

-SECOND PARTITION

User: 739812492310896640, Tweet: 995382455539589120, Retweets: 5291
User: 29056256, Tweet: 995381560277979136, Retweets: 4643
User: 24679473, Tweet: 995216336069562369, Retweets: 10155
User: 1501434991, Tweet: 995372692902699009, Retweets: 5167
User: 3143260474, Tweet: 995356756770467840, Retweets: 10809
