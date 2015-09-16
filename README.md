# car-advert

## run your local dynamo db
* We're using Amazon Dynamo DB, but feel free to use different storage as long as we will be able to run it without doing excessive configuration work.
  * If you decide to use local Amazon Dynamo DB, which could be downloaded from Amazon as a [tar.gz](http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.tar.gz) or [zip](http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.zip) archive or installed with **homebrew** if you're using MacOS: ```brew install dynamodb-local```, try to not store data in Dynamo DB as a string blob, try to either use a Dynamo DB document model or store every field as a separate attribute.
  
`java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb`

http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.DynamoDBLocal.html

