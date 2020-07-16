package com.jetbrains.handson.mpp.mobile

import android.widget.AutoCompleteTextView

class AutoValidator:AutoCompleteTextView.Validator {
    var valid_list= listOf<String>()
    override fun fixText(p0: CharSequence?): CharSequence {
        //TODO - actually fix the text
        return ""
    }

    override fun isValid(p0: CharSequence?): Boolean {
        return valid_list.contains(p0.toString())
    }
}