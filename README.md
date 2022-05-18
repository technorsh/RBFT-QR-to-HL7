# RBFT-QR-to-HL7

#### CodeBuild Environment Variables
**`FILENAME`** : This value depands upon **pom.xml** *artifactId*, *version* values. <br/>
```
Example :-

artifactId = QR2HL7
version = 1.0-SNAPSHOT
FILENAME = QR2HL7-1.0-SNAPSHOT
```
```
Note : If values are changed, need to update in Environment variable of CodeBuild as well.
```
**`SSM Parameters`** : `amazonProperties_accessKey` `amazonProperties_secretKey`

#### Variables
```
    name        |               value                
-----------------------------------------------------
 filename       |   "target/QR2HL7-1.0-SNAPSHOT.jar"   <- ( FILENAME )
 handler        |   "TestMaven.App::handleRequest"  
 function_name  |   "MavenLambdaFunction"            
 runtime        |   "java11"     
 ```
 #### AWS Console 
`CodePipeline` : [Click here](https://www.google.com "Google's Homepage") <br/>
`Lambda Function` : [Click here](https://www.google.com "Google's Homepage") <br/>
`Terraform state bucket` : [rbft-qr-to-hl7-terraform-state](https://s3.console.aws.amazon.com/s3/buckets/rbft-qr-to-hl7-terraform-state?region=ap-south-1&tab=objects "Terraform state bucket")
