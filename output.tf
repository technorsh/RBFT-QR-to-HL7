# Output the Lambda Function ARN

output "Lambda_ARN" {
    value = aws_lambda_function.lambda.arn
}