package com.shaan.androiduicomponents
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.shaan.androiduicomponents.Login
import com.shaan.androiduicomponents.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
    }

    private fun startAnimations() {
        val iconAnimation = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.splashIcon, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(binding.splashIcon, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(binding.splashIcon, "alpha", 0f, 1f)
            )
            duration = 1000
            interpolator = OvershootInterpolator()
        }

        val textAnimation = ObjectAnimator.ofFloat(binding.appNameText, "alpha", 0f, 1f).apply {
            duration = 800
            startDelay = 500
        }

        AnimatorSet().apply {
            playTogether(iconAnimation, textAnimation)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this@SplashActivity, Login::class.java))
                        finish()
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }, 500)
                }
            })
            start()
        }
    }
} 