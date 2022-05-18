# This value will be set during runtime ( The value inside FILENAME, CodeBuild Environment variable )

variable "filename" {
    type = string
    default = "target/QR2HL7-1.0-SNAPSHOT.jar"
}

# -------------------------
#  Lambda Function Details 
# -------------------------

# Lambda Function Handler 

variable "handler" {
    type = string
    default = "TestMaven.App::handleRequest"
}

# Function Name of Lambda function 

variable "function_name" {
    type = string
    default = "MavenPipelineLambdaFunction"
}

# Using Java v11 for Lambda runtime

variable "runtime" {
    type = string
    default = "java11"
}