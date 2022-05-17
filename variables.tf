variable "filename" {
    default = "target/QR2HL7-1.0-SNAPSHOT.jar"
}

variable "handler" {
    type = string
    default = "TestMaven.App::handleRequest"
}

variable "function_name" {
    default = "MavenLambdaFunction"
}

variable "runtime" {
    default = "java11"
}