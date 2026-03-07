resource "aws_vpc" "vpc_configuration" {
  cidr_block       = "10.0.0.0/16"
  region           = var.region
  instance_tenancy = "default"

  tags = {
    Name = "vpc-config-${local.name_suffix}"
  }
}

resource "aws_subnet" "public_subnet_a" {
  vpc_id            = aws_vpc.vpc_configuration.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "${var.region}a"
  depends_on        = [aws_vpc.vpc_configuration]

  tags = {
    Name = "public-subnet-a-${local.name_suffix}"
  }
}

resource "aws_subnet" "public_subnet_b" {
  vpc_id            = aws_vpc.vpc_configuration.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "${var.region}b"
  depends_on        = [aws_vpc.vpc_configuration]

  tags = {
    Name = "public-subnet-b-${local.name_suffix}"
  }
}

resource "aws_internet_gateway" "public_igw" {
  vpc_id     = aws_vpc.vpc_configuration.id
  region     = var.region
  depends_on = [aws_vpc.vpc_configuration, aws_subnet.public_subnet_a, aws_subnet.public_subnet_b]

  tags = {
    Name = "public-igw-${local.name_suffix}"
  }
}

resource "aws_route_table" "public_route_table" {
  vpc_id     = aws_vpc.vpc_configuration.id
  region     = var.region
  depends_on = [aws_internet_gateway.public_igw]

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.public_igw.id
  }

  tags = {
    Name = "public-route-table-${local.name_suffix}"
  }
}

resource "aws_route_table_association" "public_subnet_a" {
  subnet_id      = aws_subnet.public_subnet_a.id
  route_table_id = aws_route_table.public_route_table.id
  depends_on     = [aws_subnet.public_subnet_a, aws_route_table.public_route_table]
}

resource "aws_route_table_association" "public_subnet_b" {
  subnet_id      = aws_subnet.public_subnet_b.id
  route_table_id = aws_route_table.public_route_table.id
  depends_on     = [aws_subnet.public_subnet_b, aws_route_table.public_route_table]
}

resource "aws_subnet" "product_subnet_a" {
  vpc_id            = aws_vpc.vpc_configuration.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = "${var.region}a"
  depends_on        = [aws_vpc.vpc_configuration]

  tags = {
    Name = "product-subnet-a-${local.name_suffix}"
  }
}

resource "aws_subnet" "product_subnet_b" {
  vpc_id            = aws_vpc.vpc_configuration.id
  cidr_block        = "10.0.4.0/24"
  availability_zone = "${var.region}b"
  depends_on        = [aws_vpc.vpc_configuration]

  tags = {
    Name = "product-subnet-b-${local.name_suffix}"
  }
}

resource "aws_eip" "public_nat_eip" {
  region     = var.region
  depends_on = [aws_vpc.vpc_configuration]

  tags = {
    Name = "public-nat-eip-${local.name_suffix}"
  }
}

resource "aws_nat_gateway" "public_nat_gw" {
  allocation_id     = aws_eip.public_nat_eip.id
  subnet_id         = aws_subnet.public_subnet_a.id
  depends_on        = [aws_subnet.public_subnet_a, aws_eip.public_nat_eip]
  availability_mode = "zonal"

  tags = {
    Name = "public-nat-gw-${local.name_suffix}"
  }
}

resource "aws_route_table" "private_route_table" {
  vpc_id     = aws_vpc.vpc_configuration.id
  region     = var.region
  depends_on = [aws_nat_gateway.public_nat_gw]

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.public_nat_gw.id
  }

  tags = {
    Name = "private-route-table-${local.name_suffix}"
  }
}

resource "aws_route_table_association" "private_subnet_a" {
  subnet_id      = aws_subnet.product_subnet_a.id
  route_table_id = aws_route_table.private_route_table.id
  depends_on     = [aws_subnet.product_subnet_a, aws_route_table.private_route_table]
}

resource "aws_route_table_association" "private_subnet_b" {
  subnet_id      = aws_subnet.product_subnet_b.id
  route_table_id = aws_route_table.private_route_table.id
  depends_on     = [aws_subnet.product_subnet_b, aws_route_table.private_route_table]
}

resource "aws_lb" "product_alb" {
  name               = "product-alb-${local.name_suffix}"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.product_lb_sg.id]
  subnets            = [aws_subnet.public_subnet_a.id, aws_subnet.public_subnet_b.id]
  depends_on         = [aws_security_group.product_lb_sg, aws_subnet.public_subnet_a, aws_subnet.public_subnet_b]

  tags = {
    Name = "product-alb-${local.name_suffix}"
  }
}

resource "aws_autoscaling_group" "product_asg" {
  name             = "product-asg-${local.name_suffix}"
  max_size         = 3
  min_size         = 1
  desired_capacity = 1
  
  launch_template {
    id      = aws_launch_template.product_lt.id
    version = "$Latest"
  }
  vpc_zone_identifier = [aws_subnet.product_subnet_a.id, aws_subnet.product_subnet_b.id]
  target_group_arns    = [aws_lb_target_group.product_tg.arn]
  depends_on          = [aws_launch_template.product_lt, aws_subnet.product_subnet_a, aws_subnet.product_subnet_b]
}

resource "aws_lb_target_group" "product_tg" {
  name     = "product-tg-${local.name_suffix}"
  port     = 80
  protocol = "HTTP"
  vpc_id   = aws_vpc.vpc_configuration.id


  health_check {
    path                = "/health"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }

  tags = {
    Name = "product-tg-${local.name_suffix}"
  }
}

resource "aws_lb_listener" "product_lb_listener" {
  load_balancer_arn = aws_lb.product_alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.product_tg.arn
  }
}
