variable "filename" {
    type = string
    default = "target/QR2HL7-1.0-SNAPSHOT.jar"
}

variable "handler" {
    type = string
    default = "TestMaven.App::handleRequest"
}

variable "function_name" {
    type = string
    default = "MavenLambdaFunction"
}

variable "runtime" {
    type = string
    default = "java11"
}