resource "aws_security_group" "bastion_sg" {
  name        = "bastion-sg-${local.name_suffix}"
  description = "Security group for bastion host"
  vpc_id      = aws_vpc.vpc_configuration.id
  region      = var.region
  depends_on  = [aws_vpc.vpc_configuration]

  ingress {
    description      = "Allow SSH from anywhere"
    from_port        = 22
    to_port          = 22
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    self             = false
    prefix_list_ids  = []
    security_groups  = []
  }

  egress {
    description      = "Allow all outbound traffic"
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    self             = false
    prefix_list_ids  = []
    security_groups  = []
  }
}

resource "aws_security_group" "product_sg" {
  name        = "private-sg-${local.name_suffix}"
  description = "Security group for private instances"
  vpc_id      = aws_vpc.vpc_configuration.id
  region      = var.region
  depends_on  = [aws_vpc.vpc_configuration]

  ingress = [
    {
      description      = "Allow SSH from bastion host"
      from_port        = 22
      to_port          = 22
      protocol         = "tcp"
      cidr_blocks      = [aws_subnet.public_subnet_a.cidr_block, aws_subnet.public_subnet_b.cidr_block]
      ipv6_cidr_blocks = []
      self             = false
      prefix_list_ids  = []
      security_groups  = []
    },
    {
      description      = "Allow all inbound traffic from load balancer"
      from_port        = 80
      to_port          = 80
      protocol         = "tcp"
      cidr_blocks      = ["0.0.0.0/0"]
      ipv6_cidr_blocks = []
      self             = false
      prefix_list_ids  = []
      security_groups  = []
    },
    {
      description      = "Allow all inbound traffic from load balancer"
      from_port        = 443
      to_port          = 443
      protocol         = "tcp"
      cidr_blocks      = ["0.0.0.0/0"]
      ipv6_cidr_blocks = []
      self             = false
      prefix_list_ids  = []
      security_groups  = []
    }
  ]

  egress {
    description      = "Allow all outbound traffic"
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    self             = false
    prefix_list_ids  = []
    security_groups  = []
  }
}

resource "aws_security_group" "product_lb_sg" {
  name        = "product-lb-sg-${local.name_suffix}"
  description = "Security group for product load balancer"
  vpc_id      = aws_vpc.vpc_configuration.id
  region      = var.region
  depends_on  = [aws_vpc.vpc_configuration]

  ingress = [
    {
      description      = "Allow HTTP from anywhere"
      from_port        = 80
      to_port          = 80
      protocol         = "tcp"
      cidr_blocks      = ["0.0.0.0/0"]
      ipv6_cidr_blocks = ["::/0"]
      self             = false
      prefix_list_ids  = []
      security_groups  = []
    },
    {
      description      = "Allow HTTPS from anywhere"
      from_port        = 443
      to_port          = 443
      protocol         = "tcp"
      cidr_blocks      = ["0.0.0.0/0"]
      ipv6_cidr_blocks = ["::/0"]
      self             = false
      prefix_list_ids  = []
      security_groups  = []
    }
  ]

  egress {
    description      = "Allow all outbound traffic"
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    self             = false
    prefix_list_ids  = []
    security_groups  = []
  }
}
