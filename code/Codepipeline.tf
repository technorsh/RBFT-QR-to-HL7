resource "aws_codepipeline" "main" {

    depends_on = [
        aws_s3_bucket.codepipeline_artifact_bucket
    ]
    
    name     = "${var.project_name}-pipeline"
    role_arn = "${aws_iam_role.pipeline_role.arn}"

    artifact_store {
        location = "${aws_s3_bucket.codepipeline_artifact_bucket.bucket}"
        type     = "S3"
    }

    stage {
        name = "Source"

        action {
            name             = "Source"
            category         = "Source"
            owner            = "AWS"
            provider         = "CodeStarSourceConnection"
            version          = "1"
            run_order        = 1
            output_artifacts = [
                "SourceArtifact"
            ]
            configuration = {
                ConnectionArn  = var.codestar_connection
                FullRepositoryId = var.repo_id
                OutputArtifactFormat = "CODE_ZIP"
                BranchName = var.branch
            }
        }
    }

    stage {
        name = "Build"

        action {
            name             = "Build"
            category         = "Build"
            owner            = "AWS"
            provider         = "CodeBuild"
            run_order        = 1
            version          = "1"
            input_artifacts = [
                "SourceArtifact"
            ]
            output_artifacts = [
                "BuildArtifact"
            ]

            configuration = {
                ProjectName = aws_codebuild_project.codepipline_codebuild.name
            }
        }
    }
}