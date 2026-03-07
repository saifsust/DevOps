resource "aws_instance" "bastion" {
  ami                         = local.instance.ami
  instance_type               = local.instance.type
  key_name                    = local.instance.bastion_key
  subnet_id                   = aws_subnet.public_subnet_a.id
  associate_public_ip_address = true
  vpc_security_group_ids      = [aws_security_group.bastion_sg.id]
  region                      = var.region
  depends_on                  = [aws_security_group.bastion_sg, aws_subnet.public_subnet_a]
  tags = {
    Name = "bastion-${local.name_suffix}"
  }
}
