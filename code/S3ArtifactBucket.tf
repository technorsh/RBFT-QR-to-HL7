resource "aws_s3_bucket" "codepipeline_artifact_bucket" {
  bucket = "${var.project_name}-${var.region}-artifact"
}