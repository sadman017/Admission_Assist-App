package com.souravpalitrana.androiduicomponents

import android.annotation.SuppressLint
import android.content.DialogInterface.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast

class TestActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var regularBtn: Button
    lateinit var imageBtn: ImageButton
    lateinit var etEmailField: EditText
    lateinit var genderGroup: RadioGroup


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AndroidUIComponents_Legacy)
        setContentView(R.layout.activity_test)
        regularBtn = findViewById(R.id.regularBtn)
        imageBtn = findViewById(R.id.imageBtn)
        etEmailField = findViewById(R.id.etEmail)
        genderGroup = findViewById(R.id.genderGroup)

        regularBtn.text = "I am changed via code"
        /*regularBtn.setOnClickListener { view ->
            Toast.makeText(this@TestActivity, "Button Clicked", Toast.LENGTH_LONG).show()
        }*/
        regularBtn.setOnClickListener(this);
        imageBtn.setOnClickListener(this);

        /*etEmailField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }
        })*/

        genderGroup.setOnCheckedChangeListener(object: OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

            }
        })

    }

    override fun onClick(view: View) {
        if (view.id == R.id.regularBtn) {
            val email = etEmailField.text.toString();
            if (email.isEmpty()) {
                etEmailField.error = "Field cannot be left blank."
                //Toast.makeText(this@TestActivity, "Email Empty", Toast.LENGTH_LONG).show()
            } else {
                var text = genderGroup.findViewById<RadioButton>(genderGroup.checkedRadioButtonId).text;
                Toast.makeText(this@TestActivity, text, Toast.LENGTH_LONG).show()
            }

        } else if (view.id == R.id.imageBtn) {
            Toast.makeText(this@TestActivity, "Image Button Clicked", Toast.LENGTH_LONG).show()
        }
    }
}