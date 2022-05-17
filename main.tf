terraform {
  backend "s3" {
    # Replace this with your bucket name!
    bucket         = "bucket.tfstate"
    key            = "lambda_state"
    region         = "us-east-1"
    # Replace this with your DynamoDB table name!
    # dynamodb_table = "terraform-up-and-running-locks"
    # encrypt        = true
  }
}

resource "aws_lambda_function" "lambda" {
    # If the file is not in the current working directory you will need to include a 
    # path.module in the filename.
    filename      = var.filename
    function_name = var.function_name
    role          = aws_iam_role.iam_for_lambda.arn
    handler       = var.handler
    runtime       = var.runtime
}

resource "aws_iam_role" "iam_for_lambda" {
  name = "${var.function_name}_lambda_role"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_policy" "policy" {
  name        = "${var.function_name}_lambda_policy"
  description = "Lambda policy"

  # Terraform's "jsonencode" function converts a
  # Terraform expression result to valid JSON syntax.
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
            "logs:CreateLogGroup",
            "logs:CreateLogStream",
            "logs:PutLogEvents",
            "s3:ListStorageLensConfigurations",
            "s3:ListAccessPointsForObjectLambda",
            "s3:GetAccessPoint",
            "s3:PutAccountPublicAccessBlock",
            "s3:GetAccountPublicAccessBlock",
            "s3:ListAllMyBuckets",
            "s3:ListAccessPoints",
            "s3:PutAccessPointPublicAccessBlock",
            "s3:ListJobs",
            "s3:PutStorageLensConfiguration",
            "s3:ListMultiRegionAccessPoints",
            "s3:CreateJob"
        ]
        Effect   = "Allow"
        Resource = "*"
      },
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach_policy" {
  role       = aws_iam_role.iam_for_lambda.name
  policy_arn = aws_iam_policy.policy.arn
}