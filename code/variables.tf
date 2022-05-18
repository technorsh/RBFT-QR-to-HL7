variable "region" {
    type = string
    default = "ap-south-1"
}

variable "project_name" {
    type = string
    default = "rbft-qr-to-hl7"
}

# When we want to update Pipeline we need to update this value here to set it in CodeBuild environment variable

variable "filename" {
    type = string
    default = "target/QR2HL7-1.0-SNAPSHOT.jar"
}

# Connection Name : AWS-DTDH-Github

variable "codestar_connection" {
    type = string
    default = "arn:aws:codestar-connections:ap-south-1:185832631511:connection/23910a11-a998-4345-8d65-01381398c53e" 
}

# ----------------
#  GitHub Details 
# ----------------

# Repository Name

variable "repo_id" {
    type = string
    default = "daytodayhealth/RBFT-QR-to-HL7"
}

# Branch Name

variable "branch" {
    type =  string
    default = "master"
}