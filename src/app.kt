import java.util.*
import kotlin.Comparator

object app {

    // Didn't work without JvmStatic.
    // See: https://stackoverflow.com/documentation/kotlin/490/getting-started-with-kotlin#t=201709031903059136567
    @JvmStatic
    fun main(args: Array<String>) {
        println(joinOptions(listOf("dima", "nemets")))

        println(containsEven(listOf(1, 2, 3)))
        println(containsEven(listOf(1, 3, 5)))

        println(getPeople())

        sendMessageToClient(Client(PersonalInfo("dimanemets@gmail.com")),
                "Hello",
                object : Mailer {
                    override fun sendMessage(email: String, message: String) {
                        println(email + ": " + message)
                    }
                })

        println(getList())

        var date1 : MyDate = MyDate(2017, 9, 6)
        var date2 : MyDate = MyDate(2017, 9, 12)
        iterateOverDateRange(date1, date2, {date -> println(date)})

        val city1 = City("Rosh HaAyin")
        val product1 = Product("Salmon", 30.0)
        val order1 = Order(listOf(product1), false)
        val customer1 = Customer("Dima", city1, listOf(order1))
        val shop = Shop("Ikea", listOf(customer1))
        println(shop.getSetOfCustomers())
        println(shop.getCitiesCustomersAreFrom())
        println(shop.getCustomersFrom(city1))
    }

    // Default parameters (joinToString)
    fun joinOptions(options: Collection<String>): String {
        // joinToString is an example of default parameter values
        return options.joinToString(", ", "[", "]")
    }

    // Lambdas
    fun containsEven(collection: Collection<Int>): Boolean {
        // Note "any" method param is a lambda expr: predicate: (T) -> Boolean
        return collection.any { value -> value % 2 == 0 }
    }

    // Data Class
    data class Person(val name: String, val age: Int)

    fun getPeople(): List<Person> {
        return listOf(Person("Alice", 29), Person("Bob", 31))
    }

    // Null Safety:
    // http://kotlinlang.org/docs/reference/null-safety.html
    class Client(val personalInfo: PersonalInfo?)
    class PersonalInfo(val email: String?)
    interface Mailer {
        fun sendMessage(email: String, message: String)
    }

    fun sendMessageToClient(client: Client?, message: String?, mailer: Mailer) {
        if (client?.personalInfo?.email != null && message != null) {
            mailer.sendMessage(client.personalInfo.email, message)
        }
    }

    // Smart Casts - simply happens automatically
    // http://kotlinlang.org/docs/reference/typecasts.html#smart-casts
    fun eval(expr: Expr): Int =
            when (expr) {
                is Num -> expr.value
                is Sum -> eval(expr.left) + eval(expr.right)
                else -> throw IllegalArgumentException("Unknown expression")
            }

    interface Expr
    class Num(val value: Int) : Expr
    class Sum(val left: Expr, val right: Expr) : Expr

    // Extension Functions
    // http://kotlinlang.org/docs/reference/extensions.html
    // Adding methods to Int and Pair which return a rational number
    fun Int.r(): RationalNumber = RationalNumber(this, 1)
    fun Pair<Int, Int>.r(): RationalNumber = RationalNumber(first, second)
    data class RationalNumber(val numerator: Int, val denominator: Int)

    // Object expressions
    // http://kotlinlang.org/docs/reference/object-declarations.html
    // Similar to anonymous classes in Java.
    fun getList(): List<Int> {
        val arrayList = arrayListOf(1, 5, 2)
        Collections.sort(arrayList, object : Comparator<Int> {
            override fun compare(o1: Int?, o2: Int?): Int {
                return if (o1 != null && o2 != null) {
                    o2.compareTo(o1)
                } else {
                    0
                }
            }
        })
        return arrayList
    }

    // Ranges, Operators, Iterables and For Loops
    // https://try.kotlinlang.org/#/Kotlin%20Koans/Conventions/For%20loop

    class DateRange(val start: MyDate, val end: MyDate): Iterable<MyDate>{
        override fun iterator(): Iterator<MyDate> = DateIterator(this)
    }

    class DateIterator(val dateRange:DateRange) : Iterator<MyDate> {
        var current: MyDate = dateRange.start
        override fun next(): MyDate {
            val result = current
            current = current.nextDay()
            return result
        }
        override fun hasNext(): Boolean = current <= dateRange.end
    }

    // Another Lambda example
    fun iterateOverDateRange(firstDate: MyDate, secondDate: MyDate, handler: (MyDate) -> Unit) {
        for (date in DateRange(firstDate, secondDate)) {
            handler(date)
        }
    }

    // TODO didn't quite get this
    // Destructuring declarations
    // http://kotlinlang.org/docs/reference/multi-declarations.html
    fun isLeapDay(date: MyDate): Boolean {
        // SynSug for:
        // val year = date.component1()
        val (year, month, dayOfMonth) = date

        // 29 February of a leap year
        return year % 4 == 0 && month == 2 && dayOfMonth == 29
    }

    // Collectors

}