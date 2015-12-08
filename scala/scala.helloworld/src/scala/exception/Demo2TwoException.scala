
package scala.exception

object Demo2TwoException {

	def main(args: Array[String]) {
		funWithException(2);
	}

	def funWithException(i: Int) {
		try {
			if (i % 2 == 0) {
				throw new IllegalArgumentException(i + " is % of 2")
			} else if (i % 3 == 0) {
				throw new RuntimeException(i + " is % of 3")
			}
		} catch {
			case e: IllegalArgumentException =>
				println("I catch a IllegalArgumentException."); println("message: " + e.getMessage)
			case re: RuntimeException =>
				println("I catch a RuntimeException."); re.printStackTrace()
		}
	}
}
