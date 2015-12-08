
package scala.exception

object Demo1SimpleException {

	def main(args: Array[String]) {
		try {
			throw new RuntimeException("I am a RuntimeException")
		} catch {
			case re: RuntimeException => re.printStackTrace()
		}
	}
}
