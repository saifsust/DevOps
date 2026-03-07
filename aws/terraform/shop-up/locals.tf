locals {
  name_suffix = "${var.region}-${var.environment}"
  instance={
    ami = "ami-0b6c6ebed2801a5cb"
    type = "t3.micro"
    bastion_key= "bastion"
    private_key = "private-app-key"
  }
}
