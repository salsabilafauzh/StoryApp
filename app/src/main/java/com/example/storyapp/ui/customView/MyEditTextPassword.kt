package com.example.storyapp.ui.customView

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R

class MyEditTextPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.toString().length < 8) {
            setError(context.getString(R.string.invalid_password), null)
        } else {
            error = null
        }
    }
}