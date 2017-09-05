// Collections
data class Shop(val name: String, val customers: List<Customer>) {

    fun getSetOfCustomers(): Set<Customer> = customers.toSet()

    // Filter example
    // Return the set of cities the customers are from
//    fun getCitiesCustomersAreFrom(): Set<City> = (customers.map { customer -> customer.city }).toSet()
    fun getCitiesCustomersAreFrom(): Set<City> = (customers.map { it.city }).toSet()

    // Return a list of the customers who live in the given city
    fun getCustomersFrom(city: City): List<Customer> = (customers.filter { it.city == city })

    // Return true if all customers are from the given city
    fun checkAllCustomersAreFrom(city: City): Boolean = customers.all { it.city == city }

    // Return true if there is at least one customer from the given city
    fun hasCustomerFrom(city: City): Boolean = customers.any({ it.city == city })

    // Return the number of customers from the given city
    fun countCustomersFrom(city: City): Int = customers.count({ it.city == city })

    // Return a customer who lives in the given city, or null if there is none
    fun findAnyCustomerFrom(city: City): Customer? = customers.find { it.city == city }

    // Return all products that were ordered by at least one customer
    val allOrderedProducts: Set<Product> get() {
        return (customers.flatMap { it.orderedProducts }).toSet()
    }

    // Return a customer whose order count is the highest among all customers
    fun getCustomerWithMaximumNumberOfOrders(): Customer? = customers.maxBy { it.orders.size }

    // Return a list of customers, sorted by the ascending number of orders they made
    fun getCustomersSortedByNumberOfOrders(): List<Customer> = customers.sortedBy { it.orders.size }

    // Return a map of the customers living in each city
    fun groupCustomersByCity(): Map<City, List<Customer>> = customers.groupBy { it.city }

    // Return customers who have more undelivered orders than delivered
    fun getCustomersWithMoreUndeliveredOrdersThanDelivered(): Set<Customer> {
        return customers.filter {
            val (delivered, undelivered) = it.orders.partition { it.isDelivered }
            undelivered.size > delivered.size
        }.toSet()
    }
}

data class Customer(val name: String, val city: City, val orders: List<Order>) {
    override fun toString() = "$name from ${city.name}"

    // Return all products this customer has ordered
    val orderedProducts: Set<Product> get() {
        return (orders.flatMap { it.products }).toSet()
    }

    // This is getEventsGroupedBySessionId :)
    fun groupOrderedProductsByPrice(): Map<Double, Product> {
        return (orderedProducts.map { it.price to it }).toMap()
    }

    // Return the most expensive product which has been ordered
    fun getMostExpensiveOrderedProduct(): Product? = orderedProducts.maxBy { it.price }

    // Return the sum of prices of all products that a customer has ordered.
    // Note: the customer may order the same product for several times.
    fun getTotalOrderPrice(): Double = orderedProducts.sumByDouble { it.price }
}

data class Order(val products: List<Product>, val isDelivered: Boolean)

data class Product(val name: String, val price: Double) {
    override fun toString() = "'$name' for $price"
}

data class City(val name: String) {
    override fun toString() = name
}