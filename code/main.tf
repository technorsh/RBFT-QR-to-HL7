# Here the state file of this codepipeline will be stored.

terraform {
  backend "s3" {
    bucket         = "rbft-qr-to-hl7-terraform-state"
    key            = "pipeline.tfstate"
    region         = "ap-south-1"
  }
}