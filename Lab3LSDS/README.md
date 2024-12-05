Manvir Kaur u199321

Laia Tomàs u198723

Eric Berenguer u199894

Lab 3 

1. Stateless: joining a static RDD with a real time stream

spark-submit --master local[2] --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:/log4j.properties --class edu.upf.MastodonStateless target/lab3-mastodon-1.0-SNAPSHOT.jar /src/main/resources/map.tsv

Output:

(English,192)

(French,40)

(German,18)

(Japanese,11)

(Chinese,8)

(Spanish; Castilian,7)

(Russian,7)

(Italian,4)

(Dutch; Flemish,4)

(Portuguese,3)

1. Spark Stateful transformations with windows

spark-submit --master local[2] --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:/log4j.properties --class edu.upf.MastodonWindows target/lab3-mastodon-1.0-SNAPSHOT.jar /Users/manvirkaur/Desktop/finalDistributedSystems/LSDS/LSDS2024-seed/lab3-mastodon\_noanswer/src/main/resources/map.tsv

BATCH

English : 71

German : 16

French : 3

Japanese : 3

Portuguese : 2

Spanish; Castilian : 2

Italian : 2

Korean : 1

Chinese : 1

Catalan;Valencian : 1

Russian : 1

Swedish : 1

BATCH

English : 113

German : 24

French : 8

Spanish; Castilian : 6

Italian : 5

Japanese : 5

Russian : 4

Turkish : 2

Swedish : 2

Korean : 1

Catalan; Valencian : 1

Czech : 1

Windows: (SUM OF PREV)

English : 184

German : 40

French : 11

Spanish; Castilian : 8

Japanese : 8

Italian : 7

Russian : 5

Swedish : 3

Portuguese : 2

Turkish : 2

Korean : 2

Catalan; Valencian : 2

Chinese : 1

1. Spark Stateful transformations with state variables

spark-submit --master local[2] --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:/log4j.properties --class edu.upf.MastodonWithState target/lab3-mastodon-1.0-SNAPSHOT.jar en

We know this is not filtered by language as we have commented this line:

//.filter(pair -> pair.\_2.\_2.equals("targetLanguage"))

In the previous lab, we encountered an issue where the equals function filtered out all the tweets, resulting in an empty output. Unfortunately, we are facing the same problem in this lab as well. Despite reaching out to the teacher for assistance, we have not received any response to our inquiry. Nonetheless, we believe our approach is fundamentally sound, though we continue to struggle with this recurring issue. 

This is the output we obtained:

(8,GamersLiveFR)

(6,Political Gadgets)

(4,Fragefix :verified:)

(4,Tchoum)

(2,HC Sparta Praha 🤖)

(2,exame)

(2,GastroHistory)

(2,schildi333😷💙💊)

(2,GunChleoc)

(2,wrack)







\*\*DynamoDB\*\*

We created the DynamoDB table LsdsTwitterHashtags with the primary key: HashTag.

What we should run for 5 min:

spark-submit --master local[2] --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:/log4j.properties --class edu.upf.MastodonHashtags target/lab3-mastodon-1.0-SNAPSHOT.jar

(Service: AmazonDynamoDBv2; Status Code: 400; Error Code: AccessDeniedException)

This error means that the AWS credentials used by your application do not have the necessary permissions to perform the requested operation on DynamoDB. To solve this we tried to create a role in IAM but we didn’t have permissions even for that, so at the end we don’t have any output as we couldn’t be able to run the code because of the permissions.

spark-submit --master local[2] --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:/log4j.properties --class edu.upf.MastodonHashtagReader target/lab3-mastodon-1.0-SNAPSHOT.jar en





