package models

case class Product(upc: Int, product_name: String, selling_cost: Float)

object Product {

    def all() = Nil

    def create(product_name: String, selling_cost: Float) = {}

}
