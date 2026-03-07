resource "aws_launch_template" "product_lt" {
  name_prefix   = "product-lt-${local.name_suffix}"
  image_id      = local.instance.ami
  instance_type = local.instance.type
  key_name      = local.instance.private_key

  network_interfaces {
    security_groups = [aws_security_group.product_sg.id]
  }

  depends_on = [aws_security_group.product_sg]
  user_data = filebase64("scripts/product_search.sh")
}