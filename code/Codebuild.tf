resource "aws_codebuild_project" "codepipline_codebuild" {
  name         = "${var.project_name}-codebuild"
  description  = "Codebuild for the Maven Project"
  service_role = aws_iam_role.codebuild_role.arn

  artifacts {
    type = "CODEPIPELINE"
  }

  environment {
    compute_type    = "BUILD_GENERAL1_SMALL"
    image           = "aws/codebuild/amazonlinux2-x86_64-standard:3.0"
    type            = "LINUX_CONTAINER"
    privileged_mode = true

    environment_variable {
      name = "FILENAME"
      value = var.filename    # This value should be changed according to pom.xml artifact id & version values
    }
    environment_variable {
        name = "AWS_ACCESS_KEY_ID"
        type = "PARAMETER_STORE"
        value = "amazonProperties_accessKey"
    }
    environment_variable {
        name = "AWS_SECRET_ACCESS_KEY"
        type = "PARAMETER_STORE"
        value = "amazonProperties_secretKey"
    }
  }
  source {
    type = "CODEPIPELINE"
  }
}