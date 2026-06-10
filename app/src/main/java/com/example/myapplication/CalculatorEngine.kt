package com.example.myapplication

import kotlin.math.*
import java.io.Serializable

/**
 * CalculatorEngine - Xử lý tất cả logic tính toán
 * Hỗ trợ cơ bản, khoa học và lập trình
 */
class CalculatorEngine : Serializable {

    private var firstNumber = 0.0
    private var secondNumber = 0.0
    private var operation = ""
    private var isOperationPressed = false
    private var lastError: String? = null

    // Getter methods
    fun getFirstNumber() = firstNumber
    fun getSecondNumber() = secondNumber
    fun getOperation() = operation
    fun isOperationPressed() = isOperationPressed
    fun getLastError() = lastError

    /**
     * Thêm số vào kết quả
     */
    fun appendNumber(number: String, currentText: String): String {
        return when {
            isOperationPressed -> {
                isOperationPressed = false
                number
            }
            currentText == "0" && number != "." -> number
            currentText.contains(".") && number == "." -> currentText
            currentText.length >= 15 -> currentText // Giới hạn độ dài
            else -> currentText + number
        }
    }

    /**
     * Thêm dấu thập phân
     */
    fun appendDecimal(currentText: String): String {
        return if (!currentText.contains(".")) {
            currentText + "."
        } else {
            currentText
        }
    }

    /**
     * Xử lý khi nhấn phép toán
     */
    fun onOperationPressed(op: String, currentText: String): Pair<String, String> {
        val currentNumber = currentText.toDoubleOrNull() ?: 0.0
        lastError = null

        // Nếu đã có phép toán trước đó, tính kết quả trước
        if (operation.isNotEmpty() && !isOperationPressed) {
            secondNumber = currentNumber
            val result = calculateResult(firstNumber, secondNumber, operation)
            firstNumber = result
            isOperationPressed = true
            return Pair(formatResult(result), "$firstNumber $operation")
        }

        firstNumber = currentNumber
        operation = op
        isOperationPressed = true

        return Pair(currentText, "$firstNumber $operation")
    }

    /**
     * Xử lý khi nhấn bằng
     */
    fun onEqual(currentText: String): Triple<String, String, String?> {
        if (operation.isEmpty()) {
            return Triple(currentText, "", null)
        }

        val secondNum = currentText.toDoubleOrNull() ?: 0.0
        secondNumber = secondNum
        val result = calculateResult(firstNumber, secondNumber, operation)

        val expression = "$firstNumber $operation $secondNumber"
        val resultStr = formatResult(result)

        firstNumber = result
        operation = ""
        isOperationPressed = true

        return Triple(resultStr, expression, lastError)
    }

    /**
     * Tính toán cơ bản + lũy thừa
     */
    private fun calculateResult(num1: Double, num2: Double, op: String): Double {
        return when (op) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "/" -> {
                if (num2 != 0.0) {
                    num1 / num2
                } else {
                    lastError = "Không thể chia cho 0"
                    0.0
                }
            }
            "^" -> num1.pow(num2)
            "%" -> num1 % num2
            else -> 0.0
        }
    }

    /**
     * Các hàm lượng giác
     */
    fun sin(value: Double, isDegree: Boolean = true): Double {
        val radians = if (isDegree) Math.toRadians(value) else value
        return kotlin.math.sin(radians)
    }

    fun cos(value: Double, isDegree: Boolean = true): Double {
        val radians = if (isDegree) Math.toRadians(value) else value
        return kotlin.math.cos(radians)
    }

    fun tan(value: Double, isDegree: Boolean = true): Double {
        val radians = if (isDegree) Math.toRadians(value) else value
        return kotlin.math.tan(radians)
    }

    /**
     * Các hàm logarit
     */
    fun log(value: Double): Double {
        return if (value > 0) log10(value) else {
            lastError = "Log chỉ hỗ trợ số dương"
            0.0
        }
    }

    fun ln(value: Double): Double {
        return if (value > 0) kotlin.math.ln(value) else {
            lastError = "Ln chỉ hỗ trợ số dương"
            0.0
        }
    }

    /**
     * Hàm lũy thừa
     */
    fun power(base: Double, exponent: Double): Double {
        return base.pow(exponent)
    }

    /**
     * Hàm căn bậc 2
     */
    fun sqrt(value: Double): Double {
        return if (value >= 0) kotlin.math.sqrt(value) else {
            lastError = "Căn bậc 2 chỉ hỗ trợ số không âm"
            0.0
        }
    }

    /**
     * Hàm giai thừa
     */
    fun factorial(n: Double): Double {
        val intN = n.toInt()
        if (intN < 0 || intN.toDouble() != n) {
            lastError = "Giai thừa chỉ hỗ trợ số nguyên không âm"
            return 0.0
        }

        var result = 1.0
        for (i in 2..intN) {
            result *= i
        }
        return result
    }

    /**
     * Hàm phần trăm
     */
    fun percentage(value: Double): Double {
        return value / 100
    }

    /**
     * Giải phương trình bậc 2: ax² + bx + c = 0
     */
    fun solveQuadratic(a: Double, b: Double, c: Double): Pair<String, String?> {
        if (a == 0.0) {
            return Pair("0", "Hệ số a phải khác 0")
        }

        val discriminant = b * b - 4 * a * c

        return when {
            discriminant < 0 -> {
                Pair("Vô nghiệm", null)
            }
            discriminant == 0.0 -> {
                val x = -b / (2 * a)
                Pair("x = ${formatResult(x)}", null)
            }
            else -> {
                val sqrtDis = kotlin.math.sqrt(discriminant)
                val x1 = (-b + sqrtDis) / (2 * a)
                val x2 = (-b - sqrtDis) / (2 * a)
                Pair(
                    "x₁ = ${formatResult(x1)}\nx₂ = ${formatResult(x2)}",
                    null
                )
            }
        }
    }

    /**
     * Giải phương trình bậc 3: ax³ + bx² + cx + d = 0
     * Sử dụng công thức Cardano
     */
    fun solveCubic(a: Double, b: Double, c: Double, d: Double): Pair<String, String?> {
        if (a == 0.0) {
            return Pair("0", "Hệ số a phải khác 0")
        }

        // Normalize: chia cho a
        val b1 = b / a
        val c1 = c / a
        val d1 = d / a

        // Chuyển đổi sang dạng chuẩn
        val p = c1 - b1 * b1 / 3
        val q = 2 * b1 * b1 * b1 / 27 - b1 * c1 / 3 + d1

        // Tính toán đơn giản cho phương trình bậc 3
        return try {
            val discriminant = b1 * b1 - 3 * c1
            val result = when {
                discriminant >= 0 -> {
                    val u = cbrt(-q / 2 + kotlin.math.sqrt((q * q / 4) + (p * p * p / 27)))
                    val v = cbrt(-q / 2 - kotlin.math.sqrt((q * q / 4) + (p * p * p / 27)))
                    val x = u + v - b1 / 3
                    "x ≈ ${formatResult(x)}"
                }
                else -> "Có 3 nghiệm phức"
            }
            Pair(result, null)
        } catch (e: Exception) {
            Pair("0", "Lỗi tính toán")
        }
    }

    /**
     * Giải hệ phương trình bậc 2 ẩn:
     * a1*x + b1*y = c1
     * a2*x + b2*y = c2
     */
    fun solveSystem2x2(a1: Double, b1: Double, c1: Double,
                       a2: Double, b2: Double, c2: Double): Pair<String, String?> {
        val det = a1 * b2 - a2 * b1

        return when {
            det == 0.0 && a1 * c2 == a2 * c1 -> {
                Pair("Hệ có vô số nghiệm", null)
            }
            det == 0.0 -> {
                Pair("Hệ vô nghiệm", null)
            }
            else -> {
                val x = (c1 * b2 - c2 * b1) / det
                val y = (a1 * c2 - a2 * c1) / det
                Pair(
                    "x = ${formatResult(x)}\ny = ${formatResult(y)}",
                    null
                )
            }
        }
    }

    /**
     * Giải hệ phương trình bậc 3 ẩn (Cramer's Rule):
     * a1*x + b1*y + c1*z = d1
     * a2*x + b2*y + c2*z = d2
     * a3*x + b3*y + c3*z = d3
     */
    fun solveSystem3x3(
        a1: Double, b1: Double, c1: Double, d1: Double,
        a2: Double, b2: Double, c2: Double, d2: Double,
        a3: Double, b3: Double, c3: Double, d3: Double
    ): Pair<String, String?> {
        // Tính định thức chính
        val det = a1 * (b2 * c3 - b3 * c2) -
                b1 * (a2 * c3 - a3 * c2) +
                c1 * (a2 * b3 - a3 * b2)

        return when {
            det == 0.0 -> {
                Pair("Hệ vô nghiệm hoặc có vô số nghiệm", null)
            }
            else -> {
                // Tính x
                val detX = d1 * (b2 * c3 - b3 * c2) -
                        b1 * (d2 * c3 - d3 * c2) +
                        c1 * (d2 * b3 - d3 * b2)
                val x = detX / det

                // Tính y
                val detY = a1 * (d2 * c3 - d3 * c2) -
                        d1 * (a2 * c3 - a3 * c2) +
                        c1 * (a2 * d3 - a3 * d2)
                val y = detY / det

                // Tính z
                val detZ = a1 * (b2 * d3 - b3 * d2) -
                        b1 * (a2 * d3 - a3 * d2) +
                        d1 * (a2 * b3 - a3 * b2)
                val z = detZ / det

                Pair(
                    "x = ${formatResult(x)}\ny = ${formatResult(y)}\nz = ${formatResult(z)}",
                    null
                )
            }
        }
    }

    /**
     * Format kết quả
     */
    fun formatResult(result: Double): String {
        return when {
            result.isNaN() || result.isInfinite() -> "Lỗi"
            result == result.toLong().toDouble() -> result.toLong().toString()
            else -> String.format("%.6f", result).trimEnd('0').trimEnd('.')
        }
    }

    /**
     * Xóa tất cả
     */
    fun clear() {
        firstNumber = 0.0
        secondNumber = 0.0
        operation = ""
        isOperationPressed = false
        lastError = null
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
