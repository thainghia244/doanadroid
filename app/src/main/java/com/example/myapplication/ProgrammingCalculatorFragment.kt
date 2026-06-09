package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProgrammingCalculatorFragment : Fragment() {

    private lateinit var tvResult: TextView
    private lateinit var tvError: TextView
    private lateinit var rgEquationType: RadioGroup
    private lateinit var containerQuadratic: View
    private lateinit var containerCubic: View
    private lateinit var containerSystem2x2: View
    private lateinit var containerSystem3x3: View
    private lateinit var calculatorEngine: CalculatorEngine

    // Quadratic inputs
    private lateinit var etQuadA: EditText
    private lateinit var etQuadB: EditText
    private lateinit var etQuadC: EditText

    // Cubic inputs
    private lateinit var etCubA: EditText
    private lateinit var etCubB: EditText
    private lateinit var etCubC: EditText
    private lateinit var etCubD: EditText

    // System 2x2 inputs
    private lateinit var etSys2x2_a1: EditText
    private lateinit var etSys2x2_b1: EditText
    private lateinit var etSys2x2_c1: EditText
    private lateinit var etSys2x2_a2: EditText
    private lateinit var etSys2x2_b2: EditText
    private lateinit var etSys2x2_c2: EditText

    // System 3x3 inputs
    private lateinit var etSys3x3_a1: EditText
    private lateinit var etSys3x3_b1: EditText
    private lateinit var etSys3x3_c1: EditText
    private lateinit var etSys3x3_d1: EditText
    private lateinit var etSys3x3_a2: EditText
    private lateinit var etSys3x3_b2: EditText
    private lateinit var etSys3x3_c2: EditText
    private lateinit var etSys3x3_d2: EditText
    private lateinit var etSys3x3_a3: EditText
    private lateinit var etSys3x3_b3: EditText
    private lateinit var etSys3x3_c3: EditText
    private lateinit var etSys3x3_d3: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_programming_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calculatorEngine = arguments?.getSerializable("calculator") as? CalculatorEngine
            ?: CalculatorEngine()

        // Khởi tạo các view
        tvResult = view.findViewById(R.id.tv_result)
        tvError = view.findViewById(R.id.tv_error)
        rgEquationType = view.findViewById(R.id.rg_equation_type)

        // Containers
        containerQuadratic = view.findViewById(R.id.container_quadratic)
        containerCubic = view.findViewById(R.id.container_cubic)
        containerSystem2x2 = view.findViewById(R.id.container_system_2x2)
        containerSystem3x3 = view.findViewById(R.id.container_system_3x3)

        // Quadratic EditTexts
        etQuadA = view.findViewById(R.id.et_quad_a)
        etQuadB = view.findViewById(R.id.et_quad_b)
        etQuadC = view.findViewById(R.id.et_quad_c)

        // Cubic EditTexts
        etCubA = view.findViewById(R.id.et_cub_a)
        etCubB = view.findViewById(R.id.et_cub_b)
        etCubC = view.findViewById(R.id.et_cub_c)
        etCubD = view.findViewById(R.id.et_cub_d)

        // System 2x2 EditTexts
        etSys2x2_a1 = view.findViewById(R.id.et_sys2x2_a1)
        etSys2x2_b1 = view.findViewById(R.id.et_sys2x2_b1)
        etSys2x2_c1 = view.findViewById(R.id.et_sys2x2_c1)
        etSys2x2_a2 = view.findViewById(R.id.et_sys2x2_a2)
        etSys2x2_b2 = view.findViewById(R.id.et_sys2x2_b2)
        etSys2x2_c2 = view.findViewById(R.id.et_sys2x2_c2)

        // System 3x3 EditTexts
        etSys3x3_a1 = view.findViewById(R.id.et_sys3x3_a1)
        etSys3x3_b1 = view.findViewById(R.id.et_sys3x3_b1)
        etSys3x3_c1 = view.findViewById(R.id.et_sys3x3_c1)
        etSys3x3_d1 = view.findViewById(R.id.et_sys3x3_d1)
        etSys3x3_a2 = view.findViewById(R.id.et_sys3x3_a2)
        etSys3x3_b2 = view.findViewById(R.id.et_sys3x3_b2)
        etSys3x3_c2 = view.findViewById(R.id.et_sys3x3_c2)
        etSys3x3_d2 = view.findViewById(R.id.et_sys3x3_d2)
        etSys3x3_a3 = view.findViewById(R.id.et_sys3x3_a3)
        etSys3x3_b3 = view.findViewById(R.id.et_sys3x3_b3)
        etSys3x3_c3 = view.findViewById(R.id.et_sys3x3_c3)
        etSys3x3_d3 = view.findViewById(R.id.et_sys3x3_d3)

        // Setup radio group listener
        rgEquationType.setOnCheckedChangeListener { _, checkedId ->
            updateContainerVisibility(checkedId)
        }

        // Solve buttons
        val btnSolveQuad = view.findViewById<Button>(R.id.btn_solve_quad)
        val btnSolveCub = view.findViewById<Button>(R.id.btn_solve_cub)
        val btnSolveSys2x2 = view.findViewById<Button>(R.id.btn_solve_sys_2x2)
        val btnSolveSys3x3 = view.findViewById<Button>(R.id.btn_solve_sys_3x3)
        val btnClear = view.findViewById<Button>(R.id.btn_clear)

        btnSolveQuad.setOnClickListener { solveQuadratic() }
        btnSolveCub.setOnClickListener { solveCubic() }
        btnSolveSys2x2.setOnClickListener { solveSystem2x2() }
        btnSolveSys3x3.setOnClickListener { solveSystem3x3() }
        btnClear.setOnClickListener { clearAll() }

        // Set default visibility
        updateContainerVisibility(R.id.rb_quadratic)
    }

    private fun updateContainerVisibility(checkedId: Int) {
        containerQuadratic.visibility = if (checkedId == R.id.rb_quadratic) View.VISIBLE else View.GONE
        containerCubic.visibility = if (checkedId == R.id.rb_cubic) View.VISIBLE else View.GONE
        containerSystem2x2.visibility = if (checkedId == R.id.rb_system_2x2) View.VISIBLE else View.GONE
        containerSystem3x3.visibility = if (checkedId == R.id.rb_system_3x3) View.VISIBLE else View.GONE
        clearResult()
    }

    private fun solveQuadratic() {
        try {
            val a = etQuadA.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số a")
            val b = etQuadB.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số b")
            val c = etQuadC.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số c")

            val (result, error) = calculatorEngine.solveQuadratic(a, b, c)

            if (error != null) {
                showError(error)
            } else {
                showResult("Δ = ${calculatorEngine.formatResult(b * b - 4 * a * c)}\n\nPhương trình: ${a}x² + ${b}x + ${c} = 0\n\nKết quả:\n$result")
            }
        } catch (e: Exception) {
            showError("Lỗi: ${e.message}")
        }
    }

    private fun solveCubic() {
        try {
            val a = etCubA.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số a")
            val b = etCubB.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số b")
            val c = etCubC.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số c")
            val d = etCubD.text.toString().toDoubleOrNull() ?: return showError("Nhập hệ số d")

            val (result, error) = calculatorEngine.solveCubic(a, b, c, d)

            if (error != null) {
                showError(error)
            } else {
                showResult("Phương trình: ${a}x³ + ${b}x² + ${c}x + ${d} = 0\n\nKết quả:\n$result")
            }
        } catch (e: Exception) {
            showError("Lỗi: ${e.message}")
        }
    }

    private fun solveSystem2x2() {
        try {
            val a1 = etSys2x2_a1.text.toString().toDoubleOrNull() ?: return showError("Nhập a1")
            val b1 = etSys2x2_b1.text.toString().toDoubleOrNull() ?: return showError("Nhập b1")
            val c1 = etSys2x2_c1.text.toString().toDoubleOrNull() ?: return showError("Nhập c1")
            val a2 = etSys2x2_a2.text.toString().toDoubleOrNull() ?: return showError("Nhập a2")
            val b2 = etSys2x2_b2.text.toString().toDoubleOrNull() ?: return showError("Nhập b2")
            val c2 = etSys2x2_c2.text.toString().toDoubleOrNull() ?: return showError("Nhập c2")

            val (result, error) = calculatorEngine.solveSystem2x2(a1, b1, c1, a2, b2, c2)

            if (error != null) {
                showError(error)
            } else {
                showResult("Hệ phương trình:\n${a1}x + ${b1}y = ${c1}\n${a2}x + ${b2}y = ${c2}\n\nKết quả:\n$result")
            }
        } catch (e: Exception) {
            showError("Lỗi: ${e.message}")
        }
    }

    private fun solveSystem3x3() {
        try {
            val a1 = etSys3x3_a1.text.toString().toDoubleOrNull() ?: return showError("Nhập a1")
            val b1 = etSys3x3_b1.text.toString().toDoubleOrNull() ?: return showError("Nhập b1")
            val c1 = etSys3x3_c1.text.toString().toDoubleOrNull() ?: return showError("Nhập c1")
            val d1 = etSys3x3_d1.text.toString().toDoubleOrNull() ?: return showError("Nhập d1")

            val a2 = etSys3x3_a2.text.toString().toDoubleOrNull() ?: return showError("Nhập a2")
            val b2 = etSys3x3_b2.text.toString().toDoubleOrNull() ?: return showError("Nhập b2")
            val c2 = etSys3x3_c2.text.toString().toDoubleOrNull() ?: return showError("Nhập c2")
            val d2 = etSys3x3_d2.text.toString().toDoubleOrNull() ?: return showError("Nhập d2")

            val a3 = etSys3x3_a3.text.toString().toDoubleOrNull() ?: return showError("Nhập a3")
            val b3 = etSys3x3_b3.text.toString().toDoubleOrNull() ?: return showError("Nhập b3")
            val c3 = etSys3x3_c3.text.toString().toDoubleOrNull() ?: return showError("Nhập c3")
            val d3 = etSys3x3_d3.text.toString().toDoubleOrNull() ?: return showError("Nhập d3")

            val (result, error) = calculatorEngine.solveSystem3x3(a1, b1, c1, d1, a2, b2, c2, d2, a3, b3, c3, d3)

            if (error != null) {
                showError(error)
            } else {
                showResult("Hệ phương trình:\n${a1}x + ${b1}y + ${c1}z = ${d1}\n${a2}x + ${b2}y + ${c2}z = ${d2}\n${a3}x + ${b3}y + ${c3}z = ${d3}\n\nKết quả:\n$result")
            }
        } catch (e: Exception) {
            showError("Lỗi: ${e.message}")
        }
    }

    private fun clearAll() {
        etQuadA.text.clear()
        etQuadB.text.clear()
        etQuadC.text.clear()

        etCubA.text.clear()
        etCubB.text.clear()
        etCubC.text.clear()
        etCubD.text.clear()

        etSys2x2_a1.text.clear()
        etSys2x2_b1.text.clear()
        etSys2x2_c1.text.clear()
        etSys2x2_a2.text.clear()
        etSys2x2_b2.text.clear()
        etSys2x2_c2.text.clear()

        etSys3x3_a1.text.clear()
        etSys3x3_b1.text.clear()
        etSys3x3_c1.text.clear()
        etSys3x3_d1.text.clear()
        etSys3x3_a2.text.clear()
        etSys3x3_b2.text.clear()
        etSys3x3_c2.text.clear()
        etSys3x3_d2.text.clear()
        etSys3x3_a3.text.clear()
        etSys3x3_b3.text.clear()
        etSys3x3_c3.text.clear()
        etSys3x3_d3.text.clear()

        clearResult()
    }

    private fun showResult(message: String) {
        tvResult.text = message
        tvResult.visibility = View.VISIBLE
        tvError.visibility = View.GONE
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
        tvResult.visibility = View.GONE
    }

    private fun clearResult() {
        tvResult.text = ""
        tvError.text = ""
        tvResult.visibility = View.GONE
        tvError.visibility = View.GONE
    }

    companion object {
        fun newInstance(calculatorEngine: CalculatorEngine): ProgrammingCalculatorFragment {
            return ProgrammingCalculatorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("calculator", calculatorEngine)
                }
            }
        }
    }
}
