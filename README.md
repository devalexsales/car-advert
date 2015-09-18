# car-advert

## Tech Stack

- Scala 2.11
- Play 2.4
- AWS Dynamo DB

## run your local dynamo db

* We're using Amazon Dynamo DB, but feel free to use different storage as long as we will be able to run it without doing excessive configuration work.
* If you decide to use local Amazon Dynamo DB, which could be downloaded from Amazon as a [tar.gz](http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.tar.gz) or [zip](http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.zip) archive or installed with **homebrew** if you're using MacOS: ```brew install dynamodb-local```, try to not store data in Dynamo DB as a string blob, try to either use a Dynamo DB document model or store every field as a separate attribute.
  
`java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -inMemory`

http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.DynamoDBLocal.html



## REST APIs

-  View all car adverts
```
    method - GET
    url - http://localhost:9000/carAdverts
```

- View car advert by id
```
    method - GET
    url - http://localhost:9000/carAdverts/<id>
```

-  Create a new car advert
```
    method - POST
    url - http://localhost:9000/carAdverts
    content-type - application/x-www-form-urlencoded
    param: guid=&title=test&fuel=gasoline&price=5607&isNew=true&mileage=-1&firstRegistration=
```

-  Update a car advert
```
    method - PUT
    url - http://localhost:9000/carAdverts
    content-type - application/x-www-form-urlencoded
    param: guid=<id>&title=test&fuel=gasoline&price=5607&isNew=true&mileage=-1&firstRegistration=
```

-  Remove a car advert by id
```
    method - DELETE
    url - http://localhost:9000/carAdverts/<id>
```

