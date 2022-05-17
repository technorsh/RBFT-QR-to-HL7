terraform {
  backend "s3" {
    bucket         = "bucket.tfstate"
    key            = "lambda_state"
    region         = "us-east-1"
  }
}

data "archive_file" "lambda_zip" {
    type        = "zip"
    source_dir  = "target"
    output_path = var.filename
}

resource "aws_lambda_function" "lambda" {
    filename      = var.filename
    function_name = var.function_name
    role          = aws_iam_role.iam_for_lambda.arn
    handler       = var.handler
    runtime       = var.runtime
    source_code_hash = filebase64sha256(var.filename) #"${data.archive_file.lambda_zip.output_base64sha256}"
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