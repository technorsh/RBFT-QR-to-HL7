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
**`SSM Parameters`** : `lambda_terraform_access_key` `lambda_terraform_secret_access_key`

#### Variables
```
    name        |               value                
-----------------------------------------------------
 filename       |   "target/QR2HL7-1.0-SNAPSHOT.jar"   <- ( FILENAME )
 handler        |   "TestMaven.App::handleRequest"  
 function_name  |   "MavenPipelineLambdaFunction"            
 runtime        |   "java11"     
 ```
 #### AWS Console 
`CodePipeline` : [Click here](https://ap-south-1.console.aws.amazon.com/codesuite/codepipeline/pipelines/rbft-qr-to-hl7-pipeline/view?region=ap-south-1 "Click to open Codepipeline") <br/>
`Lambda Function` : [Click here](https://ap-south-1.console.aws.amazon.com/lambda/home?region=ap-south-1#/functions/MavenPipelineLambdaFunction?tab=code "Click to open Lambda Function") <br/>
`Terraform state bucket` : [rbft-qr-to-hl7-terraform-state](https://s3.console.aws.amazon.com/s3/buckets/rbft-qr-to-hl7-terraform-state?region=ap-south-1&tab=objects "Terraform state bucket")

**`Important`** : If you want to edit anything, don't do it manually. Use terraform code under *code/* directory to do the changes.

```
Contact : @devops-team in slack for more information.
```
