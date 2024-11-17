package com.souravpalitrana.androiduicomponents

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.souravpalitrana.androiduicomponents.databinding.ActivityCalculatorBinding
import com.souravpalitrana.androiduicomponents.ui.theme.AndroidUIComponentsTheme
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {

    lateinit var binding: ActivityCalculatorBinding

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var loginbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        email = findViewById(R.id.editTextTextEmailAddress)
        password = findViewById(R.id.editTextPassword)
        loginbutton = findViewById(R.id.loginbtn)
        fun showToastMessage(message: String) {
            Toast.makeText(applicationContext, "Login Successful!", Toast.LENGTH_SHORT).show()
        }
        loginbutton.setOnClickListener {
//            Toast.makeText(applicationContext,"Login Successful!", Toast.LENGTH_SHORT).show()
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()

            if ( passwordInput.length >= 8) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnClr.setOnClickListener{
            binding.InputText.text=""
            binding.outputText.text=""
        }
        binding.btn0.setOnClickListener{
            binding.InputText.append("0")
        }
        binding.one.setOnClickListener{
            binding.InputText.append("1")
        }
        binding.two.setOnClickListener{
            binding.InputText.append("2")
        }
        binding.three.setOnClickListener{
            binding.InputText.append("3")
        }
        binding.four.setOnClickListener{
            binding.InputText.append("4")
        }
        binding.five.setOnClickListener{
            binding.InputText.append("5")
        }
        binding.six.setOnClickListener{
            binding.InputText.append("6")
        }
        binding.seven.setOnClickListener{
            binding.InputText.append("7")
        }
        binding.eight.setOnClickListener{
            binding.InputText.append("8")
        }
        binding.nine.setOnClickListener{
            binding.InputText.append("9")
        }
        binding.dot.setOnClickListener{
            binding.InputText.append(".")
        }
        binding.leftbracket.setOnClickListener{
            binding.InputText.append(" ( ")
        }
        binding.rightbracket.setOnClickListener{
            binding.InputText.append(" ) ")
        }
        binding.plus.setOnClickListener{
            binding.InputText.append(" + ")
        }
        binding.minus.setOnClickListener{
            binding.InputText.append(" - ")
        }
        binding.multiply.setOnClickListener{
            binding.InputText.append(" * ")
        }
        binding.divide.setOnClickListener{
            binding.InputText.append(" / ")
        }
        binding.ans.setOnClickListener{
            var expression= ExpressionBuilder(binding.InputText.text.toString()).build()
            var result = expression.evaluate()
            val longresult = result.toLong()

            if(result == longresult.toDouble()){
                binding.outputText.text = longresult.toString()
            }else{
                binding.outputText.text = result.toString()
            }
        }
//        setContent {
//            AndroidUIComponentsTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidUIComponentsTheme {
        Greeting("Android")
    }
}