# Output the codepipeline name

output "codepipeline" {
    value = aws_codepipeline.main.name
}