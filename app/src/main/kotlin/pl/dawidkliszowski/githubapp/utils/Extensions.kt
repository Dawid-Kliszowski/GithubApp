package pl.dawidkliszowski.githubapp.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Double.format(digitsAfterDot: Int): String {
    return String.format("%.${digitsAfterDot}f", this)
}